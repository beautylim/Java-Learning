package org.example.service.impl;

import org.example.mapper.SysUserMapper;
import org.example.model.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

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
