package my_group;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinDemo {
    public static void main(String[] args) {
        // Create fork join/task pool service to handle the fork/join tasks
        ForkJoinPool fjp = new ForkJoinPool();

        double[] nums = new double[5000];
        // Initialize nums with values that alternate between
        // positive and negative.
        for(int i=0; i < nums.length; i++)
            nums[i] = ((i%2) == 0) ? i : -i;

        SumTask task = new SumTask(nums, 0, nums.length);
        double sum = fjp.invoke(task);

        System.out.println("Sum: " + sum);
    }
}
