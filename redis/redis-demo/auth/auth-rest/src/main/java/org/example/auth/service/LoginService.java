package org.example.auth.service;

import org.example.auth.exception.AuthException;
import org.example.auth.model.dto.LoginRequest;

public interface LoginService {

    String login(LoginRequest loginRequest) throws AuthException;
}
