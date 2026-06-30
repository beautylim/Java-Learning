package org.example.auth.service.impl;

import org.example.auth.exception.AuthException;
import org.example.auth.mapper.SysUserMapper;
import org.example.auth.model.dto.LoginRequest;
import org.example.auth.model.entity.SysUser;
import org.example.auth.service.AuthenticateService;
import org.example.auth.utils.JwtUtil;
import org.example.auth.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserPwdAuthServiceImpl implements AuthenticateService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String login(LoginRequest loginRequest) throws AuthException {
        SysUser user = sysUserMapper.selectByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "User not found");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "Password not match");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationConfiguration.getAuthenticationManager().authenticate(authenticationToken);

        User userDetail = (User) authentication.getPrincipal();
        redisUtil.setUser(userDetail.getUsername(), new HashSet<>(userDetail.getAuthorities()));
        return jwtUtil.generateToken(user.getUserId(), loginRequest.getUsername());
    }
}
