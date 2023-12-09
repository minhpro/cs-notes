package my_group;

import java.util.concurrent.RecursiveTask;

public class SumTask extends RecursiveTask<Double> {
    final static int threshold = 100;
    double[] numbers;
    int start, end;

    public SumTask(double[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Double compute() {
        double sum = 0;
        if ((end - start) < threshold) {
            for (int i = start; i < end; i++) sum += numbers[i];
        }
        else {
            int middle = (start + end) / 2;

            SumTask left = new SumTask(numbers, start, middle);
            SumTask right = new SumTask(numbers, middle, end);

            // Start each subtask by forking
            left.fork();
            right.fork();

            // Wait for subtasks to return, and aggregate the result.
            sum = left.join() + right.join();
        }
        return sum;
    }
}
