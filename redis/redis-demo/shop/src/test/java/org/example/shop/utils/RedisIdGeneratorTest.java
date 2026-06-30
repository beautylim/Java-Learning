package org.example.shop.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

@SpringBootTest
class RedisIdGeneratorTest {

    @Autowired
    private RedisIdGenerator redisIdGenerator;

    private ExecutorService executorService = new ThreadPoolExecutor(500, 600, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());

    @Test
    public void testGenerate() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(300);
        Runnable runnable = () -> {
            System.out.println(redisIdGenerator.generateId("shop"));
            countDownLatch.countDown();
        };

        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            executorService.submit(runnable);
        }

        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("总共花费： " + (end - start));
        executorService.shutdown();

    }
}