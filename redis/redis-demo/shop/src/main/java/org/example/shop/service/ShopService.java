package org.example.shop.service;

import org.example.shop.model.entity.Shop;

import java.util.List;

public interface ShopService {

    Shop findShopById(Long id);

    Shop updateShop(Long id, Shop shop);

    List<Shop> searchByGEO(Double x, Double y, int pageNo, int distance);
}
