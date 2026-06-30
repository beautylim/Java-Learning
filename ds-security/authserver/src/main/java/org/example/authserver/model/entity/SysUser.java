package org.example.authserver.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class SysUser {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
}
