package org.example.user.service;

import org.example.user.model.entity.SysUser;
import reactor.core.publisher.Mono;

public interface UserService {

    /** 注册 */
    Mono<SysUser> register(SysUser sysUser);

    /** 登录，返回JWT */
    Mono<String> login(SysUser sysUser);
}
