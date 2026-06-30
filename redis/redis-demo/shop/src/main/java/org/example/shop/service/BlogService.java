package org.example.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.auth.model.entity.SysUser;
import org.example.shop.model.entity.Blog;
import org.example.shop.model.entity.ScrollData;

import java.util.List;

public interface BlogService {

    Long post(Blog blog);

    Blog selectById(Long id) throws JsonProcessingException;

    List<Blog> selectAll();

    void like(Long id);

    List<SysUser> getBlogLikedLoginUsers(Long id, int size);

    List<Blog> selectByUserId(Long userId);

    ScrollData getFollowBlog(Long minTime, Integer offset, Integer count);
}
