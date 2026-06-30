package org.example.auth.service;

import org.example.auth.model.entity.SysUser;

public interface SysUserService {

    SysUser getUserById(Long id);

    SysUser getUserProfile();
}
