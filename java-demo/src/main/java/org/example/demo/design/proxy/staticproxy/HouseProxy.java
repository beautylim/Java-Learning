package org.example.demo.design.proxy.staticproxy;

public class HouseProxy implements HouseOwner{
    private HouseOwner realHouseOwner;
    public HouseProxy (HouseOwner realHouseOwner) {
        this.realHouseOwner = realHouseOwner;
    }

    @Override
    public void visit() {
        System.out.println("我是房产中介，代理房东 给客户看房: 参观开始");
        realHouseOwner.visit();
        System.out.println("我是房产中介，代理房东 给客户看房： 参观结束");

    }

    @Override
    public void contract() {
        System.out.println("我是房产中介，代理房东 给客户签合同: 签合同开始");
        realHouseOwner.contract();
        System.out.println("我是房产中介，代理房东 给客户签合同： 签合同结束");
    }
}
