package org.example.shop.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.internal.StringUtil;
import org.example.shop.exception.ShopException;
import org.example.shop.model.entity.RedisData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class RedisCacheClient <T extends RedisData>{

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExecutorService executorService;

    private Class<T> clazz;

    public RedisCacheClient(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T queryWithLogicExpire(Long id, String keyPrefix, Function<Long, T> loadData, Long expireInSeconds) throws JsonProcessingException {
        // 拿缓存
        String cacheStr = stringRedisTemplate.opsForValue().get(keyPrefix + id);

        //拿不到返回异常
        if (cacheStr == null) {
            throw new RuntimeException("数据不存在");
        }

        //拿到检查过期时间
        T dataObject = objectMapper.readValue(cacheStr, clazz);
        //过期时间正常，直接返回
        if (dataObject.getExpireTime().isAfter(LocalDateTime.now())) {
            return dataObject;
        }

        // 过期了，拿互斥锁
        if (tryLock("lock:" + keyPrefix +id)) {
            // 拿到开启新线程更新，然后返回过期数据
            executorService.submit(() -> {
                try{
                   T dataNew = loadData.apply(id);
                   if (dataNew != null) {
                       dataNew.setExpireTime(LocalDateTime.now().plusSeconds(expireInSeconds));
                       stringRedisTemplate.opsForValue().set(keyPrefix + id, objectMapper.writeValueAsString(dataNew));
                   }
                } catch (Exception e){
                    System.out.println(e.getMessage());
                } finally {
                    unlock("lock:" + keyPrefix +id);
                }
            });
        }

        // 拿不到直接返回旧数据
        return dataObject;
    }

    public T queryByIdMutex(Long id, String keyPrefix, Function<Long, T> loadData) throws JsonProcessingException {

        //从redis查
        String dataStr = stringRedisTemplate.opsForValue().get(keyPrefix+id);
        //有就返回
        if (dataStr != null) {
            if (StringUtil.isNullOrEmpty(dataStr)) { //为了解决缓存穿透问题，检查是否是合理数据
                throw new RuntimeException("Data not found");
            }
            T dataObject = objectMapper.readValue(dataStr, clazz);
            return dataObject;
        }
        //没有，从数据库查
        // 为了防止缓存击穿问题，怕大量请求来请求数据库 加缓存，要用互斥锁
        // 先拿互斥锁
        T dataObject = null;
        try {
            if (!tryLock("lock:" + keyPrefix +id)) {
                // 没拿到，一直等待互斥锁释放，并拿到缓存
                Thread.sleep(1000);
                return queryByIdMutex(id, keyPrefix, loadData);
            }

            dataObject = loadData.apply(id);
            if (dataObject == null) { //防止缓存穿透，不断从数据库查询不存在的数据，所以缓存一个空字符串给不存在的id
                stringRedisTemplate.opsForValue().set(keyPrefix+id, "", 2, TimeUnit.MINUTES);
                throw new RuntimeException("Data not found");

            }
            stringRedisTemplate.opsForValue().set(keyPrefix+id, objectMapper.writeValueAsString(dataObject), 10, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted");
        } finally {
            unlock("lock:" + keyPrefix +id);
        }
        return dataObject;
    }

    public void update(Long id, String keyPrefix, Function<Long, T> loadData) throws JsonProcessingException {
        T dataObject = loadData.apply(id);
        if (dataObject == null) { //防止缓存穿透，不断从数据库查询不存在的数据，所以缓存一个空字符串给不存在的id
            stringRedisTemplate.opsForValue().set(keyPrefix+id, "", 2, TimeUnit.MINUTES);
            throw new RuntimeException("Data not found");

        }
        stringRedisTemplate.opsForValue().set(keyPrefix+id, objectMapper.writeValueAsString(dataObject), 10, TimeUnit.SECONDS);

    }

    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(flag);
    }

    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    public void deleteKey(String key) {
        stringRedisTemplate.delete(key);
    }
}
