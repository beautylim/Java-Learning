package org.example.auth.mapper;

import org.example.auth.model.entity.SysUser;

import java.util.List;

public interface SysUserMapper {

    List<SysUser> selectAll();

    SysUser selectByUsername(String username);
    SysUser selectById(Long id);

    SysUser selectByPhone(String phone);

    int addUser(SysUser sysUser);

    int updateUser(SysUser sysUser);

    int deleteByUserId(Long id);

    List<String> getPermissionsById(Long id);
}
