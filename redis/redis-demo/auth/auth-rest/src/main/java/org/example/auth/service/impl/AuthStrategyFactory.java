package org.example.auth.service.impl;

import org.example.auth.exception.AuthException;
import org.example.auth.service.AuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthStrategyFactory {
    @Autowired
    private ApplicationContext applicationContext;

    public AuthenticateService getAuthenticateService(String loginType) throws AuthException {
        Object target = applicationContext.getBean(loginType + "AuthServiceImpl");
        if (target instanceof AuthenticateService) {
            return (AuthenticateService) target;
        }
        throw new AuthException(HttpStatus.BAD_REQUEST, "login type is wrong");
    }

}
