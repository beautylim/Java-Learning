package org.example.demo.threadpool;

import java.util.concurrent.*;

public class TestCompletable {

    static void main(String[] args) throws ExecutionException, InterruptedException {
        ArrayBlockingQueue<Runnable> arrayBlockingQueue = new ArrayBlockingQueue<>(3);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                arrayBlockingQueue, new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<String>[] futures = new CompletableFuture[10];
        for (int i=0; i<10; i++) {
            int index = i;
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                return "test" + index;
            }, threadPoolExecutor);
            futures[i] = future;
        }

        CompletableFuture.allOf(futures).get();

        for (CompletableFuture<String> completableFuture : futures) {
            System.out.println(completableFuture.get());
        }

        threadPoolExecutor.shutdown();

    }
}
