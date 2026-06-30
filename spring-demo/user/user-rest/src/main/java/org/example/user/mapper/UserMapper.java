package org.example.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.user.model.entity.SysUser;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
}
