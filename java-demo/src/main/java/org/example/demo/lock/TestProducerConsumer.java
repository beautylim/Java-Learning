package org.example.demo.lock;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

public class TestProducerConsumer {

    private static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
    static void main(String[] args) throws InterruptedException {
        Thread threadProducer = new Thread(() -> {
            int i=0;
            while (i<20) {
                String producer = "Producer" + i;
                System.out.println(producer);
                try {
                    queue.put("Product " + producer);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                i++;
            }
        });

        Thread threadConsumer = new Thread(() -> {
            int i=0;
            while (i<20) {
                try {
                    System.out.println("Consume " + queue.take());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                i++;
            }
        });

        threadProducer.start();
        threadConsumer.start();

        threadProducer.join();
        threadConsumer.join();
    }
}
