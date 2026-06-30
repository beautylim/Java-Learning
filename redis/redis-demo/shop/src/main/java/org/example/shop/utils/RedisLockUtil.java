package org.example.shop.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLockUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ThreadLocal<String> lockKeyThreadLocal = new ThreadLocal<>();

    private DefaultRedisScript<Long> redisUnlockScript;

    private static final String LOCK_VALUE_TEMPLATE = "%s-%s";

    @PostConstruct
    public void init() {
        redisUnlockScript = new DefaultRedisScript<>();
        redisUnlockScript.setLocation(new ClassPathResource("redis_unlock.lua"));
        redisUnlockScript.setResultType(Long.class);
    }

    public boolean tryLock(String key, long timeoutInSeconds) {
        String lockValue = getLockValue();
        lockKeyThreadLocal.set(lockValue);
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent("lock:" + key, lockKeyThreadLocal.get(), timeoutInSeconds, TimeUnit.MINUTES);
        return Boolean.TRUE.equals(result);
    }

    public void unlock(String key){
        try {
            stringRedisTemplate.execute(redisUnlockScript, List.of("lock:" + key), lockKeyThreadLocal.get());
        } finally {
            lockKeyThreadLocal.remove();
        }
    }

    private String getLockValue(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return String.format(LOCK_VALUE_TEMPLATE, uuid, Thread.currentThread().getId());
    }
}
