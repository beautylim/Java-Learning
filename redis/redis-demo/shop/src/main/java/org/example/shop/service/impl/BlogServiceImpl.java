package org.example.shop.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.auth.LoginUser;
import org.example.auth.model.entity.SysUser;
import org.example.shop.clients.RedisBlogClient;
import org.example.shop.feign.SysUserFeignClient;
import org.example.shop.mapper.BlogMapper;
import org.example.shop.mapper.FollowMapper;
import org.example.shop.model.entity.Blog;
import org.example.shop.model.entity.ScrollData;
import org.example.shop.service.BlogService;
import org.example.shop.utils.UserUtil;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class BlogServiceImpl implements BlogService {

    private static final String BLOG_LIKE_REDIS_KEY = "blog:liked:userId:";
    private static final String BLOG_REDIS_KEY = "blog:";
    private static final String BLOG_FEED_KEY = "blog:feed:";

    @Autowired
    private RedisBlogClient redisBlogClient;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private SysUserFeignClient sysUserFeignClient;

    @Autowired
    private FollowMapper followMapper;

    @Override
    public Long post(Blog blog) {
        LoginUser loginUser = userUtil.getCurrentUser();
        blog.setUserId(loginUser.getUserId());
        blog.setUserName(loginUser.getUsername());
        blogMapper.insert(blog);
        List<Long> userIds = followMapper.getAllUserId(loginUser.getUserId());
        userIds.forEach(userId -> {
            stringRedisTemplate.opsForZSet().add(BLOG_FEED_KEY + userId, blog.getId().toString(), System.currentTimeMillis());
        });
        return blog.getId();
    }

    @Override
    public Blog selectById(Long id) throws JsonProcessingException {
        Blog blog = redisBlogClient.queryByIdMutex(id, BLOG_REDIS_KEY, blogMapper::selectById);
        LoginUser loginUser = userUtil.getCurrentUser();
        blog.setLike(isBlogLiked(id, loginUser.getUserId()));
        return blog;
    }

    @Override
    public List<Blog> selectAll() {
        return List.of();
    }

    @Override
    public void like(Long id) {
        LoginUser loginUser = userUtil.getCurrentUser();
        if (loginUser == null) {
            System.out.println("用户未登录不能点赞");
            return;
        }
        Double score = stringRedisTemplate.opsForZSet().score(BLOG_LIKE_REDIS_KEY + id, loginUser.getUserId().toString());
        if (score == null) {
            int res = blogMapper.addLike(id);
            if (res > 0) {
                stringRedisTemplate.opsForZSet().add(BLOG_LIKE_REDIS_KEY + id, loginUser.getUserId().toString(), System.currentTimeMillis());
            }
        } else {
            int res = blogMapper.cancelLike(id);
            if (res > 0) {
                stringRedisTemplate.opsForZSet().remove(BLOG_LIKE_REDIS_KEY + id, loginUser.getUserId().toString());
            }
        }
        redisBlogClient.deleteKey(BLOG_REDIS_KEY + id);
    }

    @Override
    public List<SysUser> getBlogLikedLoginUsers(Long id, int size) {
        Set<String> range = stringRedisTemplate.opsForZSet().range(BLOG_LIKE_REDIS_KEY + id, 0, size-1);
        List<SysUser> lists = range.stream()
                .map(userId -> sysUserFeignClient.getUserById(Long.parseLong(userId)).getBody())
                .toList();
        return lists;
    }

    @Override
    public List<Blog> selectByUserId(Long userId) {
        return blogMapper.selectByUserId(userId);
    }

    @Override
    public ScrollData getFollowBlog(Long minTime, Integer offset, Integer count) {
        Long userId = userUtil.getCurrentUser().getUserId();
        String key = BLOG_FEED_KEY + userId;
//        zrevrangebyscore key max min withscores limit offset count
        Set<ZSetOperations.@NonNull TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, 0, minTime, offset, count);
        if (typedTuples == null || typedTuples.isEmpty()) {
            return null;
        }
        List<Blog> blogs = typedTuples.stream().map(blogId -> {
            Blog blog = blogMapper.selectById(Long.valueOf(blogId.getValue()));
            blog.setLike(isBlogLiked(blog.getId(), userId));
            return blog;
        }).toList();
        Double currentMinTimeDouble = typedTuples.parallelStream().map(ZSetOperations.TypedTuple::getScore).min((o1, o2) -> (int) (o1 - o2)).get();
        long currentOffset = typedTuples.parallelStream().filter(t -> Objects.equals(t.getScore(), currentMinTimeDouble)).count();
        ScrollData scrollData = new ScrollData();
        scrollData.setOffset(currentOffset);
        scrollData.setMinTime(currentMinTimeDouble.longValue());
        scrollData.setList(blogs);
        return scrollData;
    }

    private boolean isBlogLiked(Long id, Long userId) {
        return stringRedisTemplate.opsForZSet().score(BLOG_LIKE_REDIS_KEY + id, userId.toString()) != null;
    }

}
