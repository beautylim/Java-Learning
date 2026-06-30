package org.example.shop.mapper;

import org.example.shop.model.entity.Blog;

import java.util.List;

public interface BlogMapper {

    int insert(Blog blog);
    int update(Blog blog);
    int addLike(Long id);
    int cancelLike(Long id);
    int delete(Blog blog);
    Blog selectById(Long id);
    List<Blog> selectAll();
    List<Blog> selectByUserId(Long userId);
}
