package org.example.shop.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.java.Log;
import org.example.shop.clients.RedisCacheClient;
import org.example.shop.exception.ShopException;
import org.example.shop.mapper.ShopMapper;
import org.example.shop.model.entity.Shop;
import org.example.shop.service.ShopService;
import org.redisson.api.geo.GeoSearchArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    private static final int PAGE_SIZE = 3;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private RedisTemplate<String, Shop> redisTemplate;

    @Autowired
    private RedisCacheClient<Shop> redisCacheClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Shop findShopById(Long id) {

        try {
            return findShopByIdMutex(id);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public Shop findShopByLogicExpire(Long id) throws JsonProcessingException {

        return redisCacheClient.queryWithLogicExpire(id, "shop:", shopMapper::getShopById, 600L);
    }


    public Shop findShopByIdMutex(Long id) throws JsonProcessingException {

        return redisCacheClient.queryByIdMutex(id, "shop:"+id, shopMapper::getShopById);
    }

    @Override
    @Transactional
    public Shop updateShop(Long id, Shop shop) {
        if (id == null) {
            throw new ShopException(HttpStatus.BAD_REQUEST, "Shop id is null");
        }
        shop.setId(id);
        shopMapper.updateShopIgnoreNull(shop);

        redisTemplate.delete("shop:"+shop.getId());
        return shopMapper.getShopById(id);
    }

    @Override
    public List<Shop> searchByGEO(Double x, Double y, int pageNo, int distance) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = stringRedisTemplate.opsForGeo().search(
                "shop:geo",
                GeoReference.fromCoordinate(new Point(x, y)),
                new Distance(distance),
                RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeDistance().limit(PAGE_SIZE*3)
        );
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = results.getContent();
        if (content == null || content.isEmpty()) {
            return null;
        }
        int from = (pageNo-1) *  PAGE_SIZE;
        List<Shop> shops = content.stream().skip(from).map(geoResult -> {
            String shopIdStr = geoResult.getContent().getName();
            Distance distanceFar = geoResult.getDistance();
            Shop shop = shopMapper.getShopById(Long.parseLong(shopIdStr));
            shop.setDistance(distanceFar.getValue());
            return shop;
        }).limit(PAGE_SIZE).toList();
        return shops;
    }


}
