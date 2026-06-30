package org.example.user.converter;

import org.example.user.model.dto.SysUserDTO;
import org.example.user.model.entity.SysUser;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class SysUserConverter {
    public SysUserDTO toDTO(SysUser sysUser) {
        SysUserDTO sysUserDTO = new SysUserDTO();
        BeanUtils.copyProperties(sysUser, sysUserDTO);
        return sysUserDTO;
    }
    public SysUser toEntity(SysUserDTO sysUserDTO) {
        SysUser sysUser = new SysUserDTO();
        BeanUtils.copyProperties(sysUserDTO, sysUser);
        return sysUser;
    }
}
