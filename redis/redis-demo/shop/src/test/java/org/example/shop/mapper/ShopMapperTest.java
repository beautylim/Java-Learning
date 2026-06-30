package org.example.shop.mapper;

import org.example.shop.model.entity.Shop;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShopMapperTest {

    @Autowired
    private ShopMapper shopMapper;

    @Test
    public void testFindById() {
        Shop shop = shopMapper.getShopById(1L);
        System.out.println(shop);
    }

    @Test
    public void testFindAllShops() {
        List<Shop> shops = shopMapper.findAllShops();
        for (Shop shop : shops) {
            System.out.println(shop);
        }
    }

    @Test
    public void testFindShopsByName() {
        List<Shop> shops = shopMapper.findShops("金姐老火锅", null);
        for (Shop shop : shops) {
            System.out.println(shop);
        }
    }

    @Test
    public void testFindShopsByAddress() {
        List<Shop> shops = shopMapper.findShops(null, "夏都");
        for (Shop shop : shops) {
            System.out.println(shop);
        }
    }

    @Test
    public void testFindShopsByTags() {
        List<Shop> shops = shopMapper.findShopsComplex("火锅", "", "夏都小镇");
        for (Shop shop : shops) {
            System.out.println(shop);
        }
    }

    @Test
    public void testUpdateShop() {
        Shop shop = shopMapper.getShopById(1L);
        System.out.println(shop);
        String tagsOrg = shop.getTags();
        //设置tags为空
        shop.setTags(null);
        int value = shopMapper.updateShop(shop);
        System.out.println("result: " + value);

        //验证tags是否为空
        shop =  shopMapper.getShopById(1L);
        System.out.println(shop);
        Assertions.assertNull(shop.getTags(),"Expected tags is null");

        //恢复数据
        shop.setTags(tagsOrg);
        shopMapper.updateShop(shop);
        System.out.println(shop);
        Assertions.assertEquals(shop.getTags(),tagsOrg,"Expected tags is recovered");
    }

    @Test
    public void testUpdateShopIgnoreNull() {
        Shop shop = shopMapper.getShopById(1L);
        System.out.println(shop);
        //设置tags为空
        shop.setTags(null);
        int value = shopMapper.updateShopIgnoreNull(shop);
        System.out.println("result: " + value);

        //验证tags是否为空
        shop =  shopMapper.getShopById(1L);
        System.out.println(shop);
        Assertions.assertNotNull(shop.getTags(),"Expected tags is not null");
    }

    @Test
    public void testFindShopOnlyFromOneCondition() {
        List<Shop> shops = shopMapper.findShopsOnlyFromOneCondition("火锅", null, "徐泾");
        Assertions.assertNotNull(shops, "Expected shops is null");
        for (Shop shop : shops) {
            System.out.println(shop);
        }

        shops = shopMapper.findShopsOnlyFromOneCondition(null, "夏都", "徐泾");
        Assertions.assertNotNull(shops, "Expected shops is null");
        for (Shop shop : shops) {
            System.out.println(shop);
        }

        shops = shopMapper.findShopsOnlyFromOneCondition(null, null, "徐泾");
        Assertions.assertNotNull(shops, "Expected shops is null");
        for (Shop shop : shops) {
            System.out.println(shop);
        }
    }

    @Test
    public void testFindShopsByIds() {
        List<Shop> shops = shopMapper.findShopsByIds(List.of(1L));
        Assertions.assertNotNull(shops, "Expected shops is null");
        for (Shop shop : shops) {
            System.out.println(shop);
        }
    }

    @Test
    public void insertShop() {
        Shop shop = new Shop();
        shop.setShopName("秋笠淮扬小馆");
        shop.setAddress("徐泾镇夏都小镇8号");
        shop.setLogoPath("/huai_yang_logo.jpg");
        shop.setCoverPath("/huai_yang_cover.jpg");
        shop.setTags("淮扬菜,徐泾,夏都小镇");
        shop.setScore(4.5F);
        shopMapper.insertShop(shop);
        Assertions.assertNotEquals(0, shop.getId(), "Expected shop id is null");
        System.out.println(shop.getId());
    }

    @Test
    public void insertShopsComplex() {
        Shop shop1 = new Shop();
        shop1.setShopName("崇明私房菜");
        shop1.setAddress("徐泾镇夏都小镇9号");
        shop1.setLogoPath("/chong_ming_logo.jpg");
        shop1.setCoverPath("/chong_ming_cover.jpg");
        shop1.setTags("崇明私房菜,徐泾,夏都小镇");
        shop1.setScore(4.0F);

        Shop shop2 = new Shop();
        shop2.setShopName("喜茶");
        shop2.setAddress("徐泾镇夏都小镇10号");
        shop2.setLogoPath("/xicha_logo.jpg");
        shop2.setCoverPath("/xicha_cover.jpg");
        shop2.setTags("奶茶,徐泾,夏都小镇");
        shop2.setScore(4.5F);

        Shop shop3 = new Shop();
        shop3.setShopName("越南米粉");
        shop3.setAddress("徐泾镇夏都小镇11号");
        shop3.setLogoPath("/yuenan_mifeng_logo.jpg");
        shop3.setCoverPath("/yuenan_mifeng_cover.jpg");
        shop3.setTags("越南米粉,徐泾,夏都小镇");
        shop3.setScore(4.6F);

        List<Shop> shops = List.of(shop1,shop2,shop3);
        shopMapper.insertShops(shops);

        List<Shop> shopList = shopMapper.findShopsComplex(null, null, "奶茶");
        Assertions.assertNotNull(shopList, "Expected shops is null");
        for (Shop shop : shopList) {
            System.out.println(shop);
        }
    }

}