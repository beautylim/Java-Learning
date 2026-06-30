package org.example.demo.design.proxy.staticproxy;

public class RealHouseOwner implements HouseOwner{

    String address;

    public RealHouseOwner(String address) {
        this.address = address;
    }

    @Override
    public void visit() {
        System.out.println("欢迎来参观 " + address);
    }

    @Override
    public void contract() {
        System.out.println("这栋房子：" + address + ". 就卖给你了");
    }
}
