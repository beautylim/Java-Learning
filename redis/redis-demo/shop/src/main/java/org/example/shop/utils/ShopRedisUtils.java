package org.example.shop.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.shop.mapper.ShopMapper;
import org.example.shop.model.entity.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShopRedisUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ObjectMapper objectMapper;

    public void loadShopCache(Long shopId, Long expireInSeconds) throws JsonProcessingException {
        Shop shop = shopMapper.getShopById(shopId);
        if (shop != null) {
            shop.setExpireTime(LocalDateTime.now().plusSeconds(expireInSeconds));
            stringRedisTemplate.opsForValue().set("shop:" + shopId, objectMapper.writeValueAsString(shop));
        }
    }
}
