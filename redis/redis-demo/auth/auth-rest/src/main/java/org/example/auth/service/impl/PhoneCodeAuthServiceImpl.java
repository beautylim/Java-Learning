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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PhoneCodeAuthServiceImpl implements AuthenticateService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String login(LoginRequest loginRequest) throws AuthException {
        SysUser sysUser = sysUserMapper.selectByPhone(loginRequest.getUsername());
        if (sysUser == null) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "User not found");
        }
        String telCode = redisUtil.getPhoneCode(loginRequest.getUsername());
        if (telCode == null) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "手机验证码失效请再发一遍");
        }

        if (!telCode.equals(loginRequest.getTelCode())) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "验证码错误,请重试");
        }

        List<String> permissions = sysUserMapper.getPermissionsById(sysUser.getUserId());
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        permissions.forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission)));

        redisUtil.setUser(sysUser.getUsername(), grantedAuthorities);

        return jwtUtil.generateToken(sysUser.getUserId(), sysUser.getUsername());
    }
}
