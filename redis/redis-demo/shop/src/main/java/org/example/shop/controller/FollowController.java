package org.example.shop.controller;

import org.example.auth.model.entity.SysUser;
import org.example.shop.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @PostMapping("/{id}/{isFollow}")
    public ResponseEntity<String> follow(@PathVariable Long id, @PathVariable Boolean isFollow) {
        return ResponseEntity.status(HttpStatus.CREATED).body("\"result:\"" + followService.follow(id, isFollow));
    }

    @GetMapping("/is/not/{id}")
    public ResponseEntity<String> isFollow(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body("\"result:\"" + followService.isFollow(id));

    }

    @GetMapping("/common/{id}")
    public ResponseEntity<List<SysUser>> commonFollow(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(followService.commonFollow(id));
    }
}
