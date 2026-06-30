package org.example.service;

import org.example.exception.UserException;
import org.example.model.entity.SysUser;

public interface UserService {

    void registerUser(SysUser sysUser) throws UserException;
}
