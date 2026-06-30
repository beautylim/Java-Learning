package org.example.shop.clients;

import org.example.shop.model.entity.Shop;
import org.springframework.stereotype.Component;

@Component
public class ShopRedisCacheClient extends RedisCacheClient<Shop>{

    public ShopRedisCacheClient() {
        super(Shop.class);
    }
}
