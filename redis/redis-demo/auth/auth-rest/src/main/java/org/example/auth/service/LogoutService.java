package org.example.auth.service;

import org.example.auth.exception.AuthException;

public interface LogoutService {
    void logout() throws AuthException;
}
