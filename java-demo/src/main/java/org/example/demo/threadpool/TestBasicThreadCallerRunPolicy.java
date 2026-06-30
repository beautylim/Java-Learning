package org.example.demo.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestBasicThreadCallerRunPolicy {

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
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        execute(threadPoolExecutor, "123", 2000);
        execute(threadPoolExecutor, "456", 1000);
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("All threads executed completely, shutdown main thread now!");

//        Current thread is: main
//        Current thread is: apache-thread-5
//        Current thread is: apache-thread-2
//        Current thread is: apache-thread-3
//        Current thread is: apache-thread-1
//        Current thread is: apache-thread-4
//        Current thread is: apache-thread-3
//        Current thread is: apache-thread-2
//        Current thread is: main
//        Current thread is: apache-thread-5
//        All threads executed completely, shutdown main thread now!
    }

    public static void execute(ThreadPoolExecutor threadPoolExecutor, String traceId, long timeout) {
        InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
        threadLocal.set(traceId);
        for (int i=0; i< 10; i++) {
            try {

                threadPoolExecutor.submit(() -> {
                    try {
                        Thread.sleep(timeout);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("[" + threadLocal.get() + "]" + "Current thread is: " + Thread.currentThread().getName());
                });
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

    }
}
