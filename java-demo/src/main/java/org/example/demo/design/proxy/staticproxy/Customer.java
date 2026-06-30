package org.example.demo.design.proxy.staticproxy;

public class Customer {

    static void main(String[] args) {
        System.out.println("我是客户我在找房子");

        RealHouseOwner realHouseOwner = new RealHouseOwner("某个区某个镇某个街道");
        HouseProxy proxy = new HouseProxy(realHouseOwner);
        proxy.visit();
        proxy.contract();
    }
}
