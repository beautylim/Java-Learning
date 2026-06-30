package org.example.auth.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LoginRequest {
    private String username;
    private String password;
    private String telCode;
    private String loginType;
}
