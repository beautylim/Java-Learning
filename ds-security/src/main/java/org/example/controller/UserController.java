package org.example.controller;

import org.example.exception.UserException;
import org.example.model.entity.SysUser;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SysUser sysUser) throws UserException {
        userService.registerUser(sysUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("success");
    }

}
