package org.example.demo.lock;

public class TestSynchronized {

    private static final Object lock = new Object();
    private static int count = 1;
    static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            while(count <= 10) {
                synchronized (lock) {
                    if ( count % 2 == 0) {
                        try {
                            System.out.println(Thread.currentThread().getName() + ": current count is " + count + " give up lock");
                            lock.wait(); //当thread1 进入waiting 状态，释放锁，由于thread2 已经进入等待锁的序列，thread2能立马获取锁
                            System.out.println(Thread.currentThread().getName() + ": current count is " + count + ":notified, get lock");

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (count <= 10) {
                        System.out.println(count);
                    }
                    count++;
                    System.out.println(Thread.currentThread().getName() + ": current count is " + count + " notify");

                    lock.notify(); // 唤醒了唯一在等待的thread2，但是由于thread1正在占用锁，所以锁还是被thread1抢到了，thread2进入排队等锁序列，等待锁的释放

            }
        }
        });
        thread1.setName("thread-1");

        Thread thread2 = new Thread(() -> {
            while(count<=10) {
                synchronized (lock) {
                    if (count%2==1) {
                        try {
                            System.out.println(Thread.currentThread().getName() + ": current count is " + count + " give up lock");
                            lock.wait();
                            System.out.println(Thread.currentThread().getName() + ": current count is " + count + ":notified, get lock");

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if (count <= 10) {
                        System.out.println(count);
                    }
                    count++;
                    System.out.println(Thread.currentThread().getName() + ": current count is " + count + " notify");
                    lock.notify();
                }

            }
        });
        thread2.setName("thread-2");
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
