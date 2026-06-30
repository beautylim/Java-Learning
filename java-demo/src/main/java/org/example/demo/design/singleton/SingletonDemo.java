package org.example.demo.design.singleton;

public class SingletonDemo {

    private static volatile SingletonDemo instance;
    private SingletonDemo() {

    }
    public static SingletonDemo getInstance() {
        if(instance == null) {
            synchronized (SingletonDemo.class) {
                if (instance == null) {
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }
}
