package my_group;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        Account acc = new Account(0);
        // Lost update problem
        Thread tA = new Thread(() -> {
            for (int i = 0; i < 1000; i++) acc.rawDeposit(1);
        });
        Thread tB = new Thread(() -> {
            for (int i = 0; i < 1000; i++) acc.rawDeposit(2);
        });
//        tA.start();
//        tB.start();
//        tA.join();
//        tB.join();
//        System.out.println(acc.getRawBalance()); // should be 3000

        acc.safeDeposit(100);
        // Read uncommitted value (temporary value)
        Thread tA2 = new Thread(() -> {
            acc.safeWithDraw(10, true);
        });

        Thread tB2 = new Thread(() -> {
            double value = acc.getRawBalance();
            System.out.println("Your balance is " + value);
        });
        tA2.start();
        tB2.start();
    }
}
