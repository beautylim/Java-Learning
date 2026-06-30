package org.example.shop.config;

import org.example.shop.serializer.FastJsonRedisSerializer;
import org.example.shop.model.entity.Shop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ShopRedisConfig {

    @Bean
    public RedisTemplate<String, Shop> shopRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Shop> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);

        FastJsonRedisSerializer<Shop> fastSerializer = new FastJsonRedisSerializer<>(Shop.class);
        redisTemplate.setValueSerializer(fastSerializer);
        redisTemplate.setHashValueSerializer(fastSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
