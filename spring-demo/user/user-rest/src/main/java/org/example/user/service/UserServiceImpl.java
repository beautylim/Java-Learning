package org.example.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.common.MessageEnum;
import org.example.common.exception.BizException;
import org.example.user.mapper.UserMapper;
import org.example.user.model.entity.SysUser;
import org.example.user.util.BCryptPasswordUtil;
import org.example.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public synchronized Mono<SysUser> register(SysUser sysUser) {
        SysUser current = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, sysUser.getUsername()));
        if(current != null) {
            throw new BizException(MessageEnum.USER_EXIST);
        }
        sysUser.setPassword(BCryptPasswordUtil.hashPassword(sysUser.getPassword()));
        int result = userMapper.insert(sysUser);
        if (result <= 0) {
            throw new BizException(MessageEnum.REGISTER_FAILED);
        }
        return Mono.just(sysUser);
    }

    @Override
    public Mono<String> login(SysUser sysUser) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, sysUser.getUsername()));
        if (user == null) {
            throw new BizException(MessageEnum.USER_NOT_EXIST);
        }
        if (!BCryptPasswordUtil.checkPassword(sysUser.getPassword(), user.getPassword())) {
            throw new BizException(MessageEnum.PASSWORD_ERROR);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        return Mono.just(jwtUtil.generateToken(claims));
    }
}
