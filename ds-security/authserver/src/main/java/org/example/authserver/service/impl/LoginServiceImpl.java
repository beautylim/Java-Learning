package org.example.authserver.service.impl;

import org.example.authserver.excaption.AuthException;
import org.example.authserver.mapper.SysUserMapper;
import org.example.authserver.model.dto.LoginRequest;
import org.example.authserver.model.entity.SysUser;
import org.example.authserver.service.LoginService;
import org.example.authserver.utils.JwtUtil;
import org.example.authserver.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService, UserDetailsService {
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

    @Transactional(rollbackFor = AuthException.class)
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
        redisUtil.setUser(userDetail.getUsername(), userDetail);
        return jwtUtil.generateToken(loginRequest.getUsername());
    }

    @Override
    public void logout() throws AuthException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        redisUtil.delUser(authentication.getName());
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException(username);
        }
        List<String> permissions = sysUserMapper.getPermissionsByUsername(sysUser.getId());
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        permissions.forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission)));

        return User.withUsername(sysUser.getUsername()).password(sysUser.getPassword()).authorities(grantedAuthorities).build();
    }
}
