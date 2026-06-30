package org.example.auth.service.impl;

import org.example.auth.exception.AuthException;
import org.example.auth.mapper.SysUserMapper;
import org.example.auth.model.dto.LoginRequest;
import org.example.auth.model.entity.SysUser;
import org.example.auth.service.AuthenticateService;
import org.example.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService, UserDetailsService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private AuthStrategyFactory authStrategyFactory;

    @Transactional(rollbackFor = AuthException.class)
    @Override
    public String login(LoginRequest loginRequest) throws AuthException {
        AuthenticateService authenticateService = authStrategyFactory.getAuthenticateService(loginRequest.getLoginType());
        return authenticateService.login(loginRequest);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException(username);
        }
        List<String> permissions = sysUserMapper.getPermissionsById(sysUser.getUserId());
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        permissions.forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission)));

        return User.withUsername(sysUser.getUsername()).password(sysUser.getPassword()).authorities(grantedAuthorities).build();
    }
}
