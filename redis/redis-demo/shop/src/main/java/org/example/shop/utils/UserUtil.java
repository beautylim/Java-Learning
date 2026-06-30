package org.example.shop.utils;

import org.example.auth.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

    public LoginUser getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
            return null;
        }
        return (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal() ;
    }

}
