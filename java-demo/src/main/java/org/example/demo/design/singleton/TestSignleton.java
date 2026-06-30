package org.example.demo.design.singleton;

public class TestSignleton {

    static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            SingletonDemo singletonDemo = SingletonDemo.getInstance();
            System.out.println(Thread.currentThread().getName() + ": " + singletonDemo);
        }, "thread-1");

        Thread thread2 = new Thread(() -> {
            SingletonDemo singletonDemo = SingletonDemo.getInstance();
            System.out.println(Thread.currentThread().getName() + ": " + singletonDemo);
        }, "thread-2");

        thread1.join();
        thread2.join();

        thread1.start();
        thread2.start();
    }
}
