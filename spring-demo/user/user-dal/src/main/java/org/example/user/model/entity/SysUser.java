package org.example.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor // 必须加！否则 MyBatis/反射会报错
@AllArgsConstructor // 必须加！否则 Builder 无法生成
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long userId;

    private String username;

    private String password;

    private String email;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
