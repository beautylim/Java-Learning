package org.example.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.auth.LoginUser;
import org.example.auth.model.entity.SysUser;
import org.example.shop.model.entity.Blog;
import org.example.shop.model.entity.ScrollData;
import org.example.shop.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping
    public ResponseEntity<Long> post(@RequestBody Blog blog) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.post(blog));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blog> selectById(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK).body(blogService.selectById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Blog>> selectByUserId(@PathVariable Long userId) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK).body(blogService.selectByUserId(userId));
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<Void> updateLike(@PathVariable Long id) throws JsonProcessingException {
        blogService.like(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/{id}/liked") // 查询最早给blog点击的前n个用户
    public ResponseEntity<List<SysUser>> selectLikeById(@PathVariable Long id, @RequestParam("size") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogLikedLoginUsers(id, size));
    }

    @GetMapping("/of/follow")
    public ResponseEntity<ScrollData> getFollowBlog(@RequestParam(name = "minTime", required = false) Long minTime,
                                                    @RequestParam(name = "offset", defaultValue = "0") Integer offset,
                                                    @RequestParam(name = "count", defaultValue = "3") Integer count ) {
        if (minTime == null) {
            minTime = System.currentTimeMillis();
        }
        return ResponseEntity.status(HttpStatus.OK).body(blogService.getFollowBlog(minTime, offset, count));
    }


}
