package org.example.shop.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdGenerator {
    private static long START_TIMESTAMP = 1780074901L;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public long generateId(String keyPrefix){
        // 前31位生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long timestamp = now.toInstant(ZoneOffset.UTC).toEpochMilli() - START_TIMESTAMP;

        //用redis获取全局序列号
        String today = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        String key = "icr:" + keyPrefix + ":" + today;
        long count = stringRedisTemplate.opsForValue().increment(key);
        return timestamp << 32 | count;
    }
}
