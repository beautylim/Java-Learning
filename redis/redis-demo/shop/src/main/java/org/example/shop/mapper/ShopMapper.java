package org.example.shop.mapper;

import org.example.shop.model.entity.Shop;

import java.util.List;

public interface ShopMapper {

    Shop getShopById(Long id);

    List<Shop> findAllShops();

    List<Shop> findShops(String name, String address);

    List<Shop> findShopsComplex(String name, String address, String tags);

    int updateShop(Shop shop);

    int updateShopIgnoreNull(Shop shop);

    List<Shop> findShopsOnlyFromOneCondition(String name, String address, String tags);

    List<Shop> findShopsByIds(List<Long> ids);

    int insertShop(Shop shop);
    int insertShops(List<Shop> shops);
}
