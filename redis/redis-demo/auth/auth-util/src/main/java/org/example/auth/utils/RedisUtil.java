package org.example.auth.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.example.auth.constant.Constants.REDIS_LOGIN;

@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void setUser(String key, Set<GrantedAuthority> authorities) {
        String redisKey = REDIS_LOGIN + key;
        redisTemplate.delete(redisKey);

        // 只存储权限的角色字符串
        Set<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        for (String role : roles) {
            redisTemplate.opsForSet().add(redisKey, role);
        }
        redisTemplate.expire(redisKey, 60, TimeUnit.MINUTES);
    }

    public Set<GrantedAuthority> getUser(String key) {
        String redisKey = REDIS_LOGIN + key;
        Set<String> roles = redisTemplate.opsForSet().members(redisKey);
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    public  void delUser(String key) {
        redisTemplate.delete(REDIS_LOGIN + key);
    }

    public String getPhoneCode(String phoneNumber) {
        if (redisTemplate.hasKey("phone:"+phoneNumber)) {
            return redisTemplate.opsForValue().get("phone:"+phoneNumber).toString();
        }
       return null;
    }

    public void setPhoneCode(String phoneNumber, String code) {
        redisTemplate.opsForValue().set("phone:"+phoneNumber, code, 2, TimeUnit.MINUTES);
    }

}
