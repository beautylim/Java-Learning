package org.example.mapper;

import org.example.model.entity.SysUser;

import java.util.List;

public interface SysUserMapper {
    List<SysUser> selectAll();

    SysUser selectByUsername(String username);

    int addUser(SysUser sysUser);

    int updateUser(SysUser sysUser);

    int deleteByUsername(Long id);

    List<String> getPermissionsByUsername(Long id);
}
