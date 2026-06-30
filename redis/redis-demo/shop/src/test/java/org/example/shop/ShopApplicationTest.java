package org.example.shop;

import org.example.shop.mapper.ShopMapper;
import org.example.shop.model.entity.Shop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ShopApplicationTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ShopMapper shopMapper;

    @Test
    public void loadShopGEOData() {
        List<Shop> shops = shopMapper.findAllShops();

        List<RedisGeoCommands.GeoLocation<String>> locations = new ArrayList<>();
        for (Shop shop : shops) {
            RedisGeoCommands.GeoLocation<String> location = new RedisGeoCommands.GeoLocation<>(shop.getId().toString(), new Point(shop.getX(), shop.getY()));
            locations.add(location);
        }
        stringRedisTemplate.opsForGeo().add("shop:geo", locations);
    }
}