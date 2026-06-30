package org.example.shop.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShopRedisUtilsTest {

    @Autowired
    private ShopRedisUtils shopRedisUtils;

    @Test
    public void testLoadShopCache() throws JsonProcessingException {
        shopRedisUtils.loadShopCache(1L, 600L);
    }

}