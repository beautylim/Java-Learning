package org.example.authserver.service;

import org.example.authserver.excaption.AuthException;
import org.example.authserver.model.dto.LoginRequest;

public interface LoginService {

    String login(LoginRequest loginRequest) throws AuthException;

    void logout() throws AuthException;
}
