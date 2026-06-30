package org.example.authserver.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.example.authserver.constant.Constants.REDIS_LOGIN;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    public void setUser(String key, User user) {
        redisTemplate.opsForValue().set(REDIS_LOGIN + key, user, 15*60, TimeUnit.SECONDS);
    }

    public User getUser(String key) {
        Object user = redisTemplate.opsForValue().get(REDIS_LOGIN + key);
        return user == null ? null : (User) user ;
    }

    public  void delUser(String key) {
        redisTemplate.delete(REDIS_LOGIN + key);
    }
}
