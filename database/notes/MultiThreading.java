// javac MultiThreading.java
// java MultiThreading

public class MultiThreading {
    
    static Integer count = 0;

    public static void increase() {
        for (int i = 0; i < 10000; i++) {
            count++;
            System.out.println(Thread.currentThread().getName() +  ": " + count);
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(MultiThreading::increase);
        Thread thread2 = new Thread(MultiThreading::increase);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println("Final value: " + count);

    }

}
