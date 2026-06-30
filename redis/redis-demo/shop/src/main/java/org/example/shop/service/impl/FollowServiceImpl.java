package org.example.shop.service.impl;

import org.example.auth.LoginUser;
import org.example.auth.model.entity.SysUser;
import org.example.shop.feign.SysUserFeignClient;
import org.example.shop.mapper.FollowMapper;
import org.example.shop.model.entity.Follow;
import org.example.shop.service.FollowService;
import org.example.shop.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {
    private static final String FOLLOWED_KEY = "followed:";

    @Autowired
    private FollowMapper  followMapper;

    @Autowired
    private UserUtil  userUtil;

    @Autowired
    private SysUserFeignClient sysUserFeignClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean follow(Long followUserId, boolean isFollow) {
        LoginUser loginUser = userUtil.getCurrentUser();
        if (isFollow) {
            Follow follow = new Follow();
            follow.setUserId(loginUser.getUserId());
            follow.setFollowUserId(followUserId);
             int res = followMapper.insert(follow);
             if (res > 0) {
                 stringRedisTemplate.opsForSet().add(FOLLOWED_KEY + loginUser.getUserId(), followUserId.toString());
                 return true;
             }
        } else {
            int res = followMapper.delete(loginUser.getUserId(), followUserId);
            if (res > 0) {
                stringRedisTemplate.opsForSet().remove(FOLLOWED_KEY + loginUser.getUserId(), followUserId.toString());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isFollow(Long id) {
        LoginUser loginUser = userUtil.getCurrentUser();
        return stringRedisTemplate.opsForSet().isMember(FOLLOWED_KEY + loginUser.getUserId(), id);
    }

    @Override
    public List<SysUser> commonFollow(Long id) {
        LoginUser loginUser = userUtil.getCurrentUser();
        String userKey = FOLLOWED_KEY + loginUser.getUserId();
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(userKey, FOLLOWED_KEY + id);
        List<SysUser> sysUsers = intersect.stream()
                .map(userId -> sysUserFeignClient.getUserById(Long.parseLong(userId)).getBody())
                .toList();
        return sysUsers;
    }
}
