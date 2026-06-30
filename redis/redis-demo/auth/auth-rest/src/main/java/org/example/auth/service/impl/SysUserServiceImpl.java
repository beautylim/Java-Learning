package org.example.auth.service.impl;

import lombok.extern.java.Log;
import org.example.auth.LoginUser;
import org.example.auth.mapper.SysUserMapper;
import org.example.auth.model.entity.SysUser;
import org.example.auth.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser getUserById(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public SysUser getUserProfile() {
        UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) userDetails.getPrincipal();
        return getUserById(loginUser.getUserId());
    }
}
