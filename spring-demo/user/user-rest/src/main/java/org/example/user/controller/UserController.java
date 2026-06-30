package org.example.user.controller;

import org.example.user.converter.SysUserConverter;
import org.example.user.model.dto.LoginDTO;
import org.example.user.model.dto.SysUserDTO;
import org.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SysUserConverter sysUserConverter;

    @PostMapping("/register")
    public Mono<ResponseEntity<SysUserDTO>> register(@RequestBody SysUserDTO sysUserDTO) {
        return userService.register(sysUserConverter.toEntity(sysUserDTO))
                .map(sysUserConverter::toDTO)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginDTO>> login(@RequestBody SysUserDTO sysUserDTO) {
        return userService.login(sysUserConverter.toEntity(sysUserDTO))
                .map(r -> ResponseEntity.status(HttpStatus.OK).body(LoginDTO.builder().token(r).build()));
    }
}
