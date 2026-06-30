package org.example.demo.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLock {

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition odd = lock.newCondition();
    private static Condition even = lock.newCondition();
    private static int count = 1;
    static void main(String[] args) throws InterruptedException {

        Thread oddThread = new Thread(() -> {
            while (count <=10) {
                   lock.lock();
                   try {
                       if (count % 2 == 0) {
                           System.out.println(Thread.currentThread().getName() + ": current count is " + count + " give up lock");

                           odd.await(); // odd 放弃锁，由于even线程已经在排队了，所以even线程能获得锁

                           System.out.println(Thread.currentThread().getName() + ": current count is " + count + " notified, get lock");

                       }

                       if (count <= 10) {
                           System.out.println(count);
                       }
                       count++;
                       System.out.println(Thread.currentThread().getName() + ": current count is " + count + " notify");

                       even.signal(); // 精准唤醒 even线程，让其参与锁竞争

                   } catch (InterruptedException e) {
                       throw new RuntimeException(e);
                   } finally {
                       lock.unlock();
                   }
            }
        }, "odd-thread");

        Thread evenThread = new Thread(() -> {
            while (count <=10) {
                lock.lock();
                try {
                    if (count % 2 == 1) {
                        System.out.println(Thread.currentThread().getName() + ": current count is " + count + " give up lock");

                        even.await();
                        System.out.println(Thread.currentThread().getName() + ": current count is " + count + " notified, get lock");

                    }

                    if (count <= 10) {
                        System.out.println(count);
                    }
                    count++;
                    System.out.println(Thread.currentThread().getName() + ": current count is " + count + " notify");

                    odd.signal();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }, "even-thread");
        oddThread.start();
        evenThread.start();

        oddThread.join();
        evenThread.join();
    }
}
