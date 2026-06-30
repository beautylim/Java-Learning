package org.example.auth.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class SysUser {
    private Long userId;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String phone;
}
