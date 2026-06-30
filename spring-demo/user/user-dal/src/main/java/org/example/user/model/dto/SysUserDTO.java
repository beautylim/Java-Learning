package org.example.user.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.user.model.entity.SysUser;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserDTO extends SysUser {
}
