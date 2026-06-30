package org.example.auth.service.impl;

import org.example.auth.exception.AuthException;
import org.example.auth.service.LogoutService;
import org.example.auth.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void logout() throws AuthException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        redisUtil.delUser(authentication.getName());
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
