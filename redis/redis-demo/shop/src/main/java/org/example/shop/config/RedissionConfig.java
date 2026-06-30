package org.example.shop.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissionConfig {

    @Bean
    public RedissonClient redissonClient() {
        //创建config
        Config config = new Config().setPassword("123456");
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }

    @Bean
    public RedissonClient redissonClient2() {
        //创建config
        Config config = new Config().setPassword("123456");
        config.useSingleServer().setAddress("redis://127.0.0.1:6380");
        return Redisson.create(config);
    }

    @Bean
    public RedissonClient redissonClient3() {
        //创建config
        Config config = new Config().setPassword("123456");
        config.useSingleServer().setAddress("redis://127.0.0.1:6381");
        return Redisson.create(config);
    }
}
