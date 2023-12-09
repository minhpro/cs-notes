package my_group;

import java.util.concurrent.ThreadLocalRandom;

public class LockBaseSynchronization {
    public static void main(String[] args) throws InterruptedException {
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(5);
        Thread producer = new Thread(() -> {
           while (true) {
               try {
                   Integer number = ThreadLocalRandom.current().nextInt(1, 100000000 + 1);
                   buffer.put(number);
                   System.out.println("Put: " + number);
                   Thread.sleep(ThreadLocalRandom.current().nextInt(100, 1000+1));
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });
        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    Integer number = buffer.take();
                    System.out.println("Take: " + number);
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000+1));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        consumer.start();
        producer.start();
        consumer.join();
        producer.join();
        System.out.println("Finished");
    }
}
