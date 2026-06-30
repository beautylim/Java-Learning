package org.example.service.impl;

import org.example.exception.UserException;
import org.example.mapper.SysUserMapper;
import org.example.model.entity.SysUser;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = UserException.class)
    public void registerUser(SysUser sysUser) throws UserException {
        SysUser exists = sysUserMapper.selectByUsername(sysUser.getUsername());
        if (exists != null) {
            throw  new UserException(HttpStatus.CONFLICT, "Duplicate user!");
        }
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        sysUserMapper.addUser(sysUser);
    }
}
