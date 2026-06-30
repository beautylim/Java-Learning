package org.example.auth.service;

import org.example.auth.exception.AuthException;
import org.example.auth.model.dto.LoginRequest;

public interface AuthenticateService {

    String login(LoginRequest loginRequest) throws AuthException;
}
