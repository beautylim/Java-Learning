package org.example.demo.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestBasicThreadDiscardOldestPolicy {

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Runnable> arrayBlockingQueue = new ArrayBlockingQueue<>(3);
        ThreadFactory guavaFactory = new ThreadFactoryBuilder().setNameFormat("guava-thread-%d").build();
        ThreadFactory apacheFactory = new BasicThreadFactory.Builder().namingPattern("apache-thread-%d").build();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,
                5,
                3,
                TimeUnit.SECONDS,
                arrayBlockingQueue,
                apacheFactory,
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        for (int i=0; i< 9; i++) {
            try {
                threadPoolExecutor.submit(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Current thread is: " + Thread.currentThread().getName());
                });
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("All threads executed completely, shutdown main thread now!");

//        Current thread is: apache-thread-4
//        Current thread is: apache-thread-5
//        Current thread is: apache-thread-1
//        Current thread is: apache-thread-2
//        Current thread is: apache-thread-3
//        Current thread is: apache-thread-4
//        Current thread is: apache-thread-5
//        Current thread is: apache-thread-1
//        All threads executed completely, shutdown main thread now!

    }
}
