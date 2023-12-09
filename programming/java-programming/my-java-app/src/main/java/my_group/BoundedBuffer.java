package my_group;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer<T> {
    final Lock lock;
    final Condition notFull;
    final Condition notEmpty;
    final T[] items;
    final int length;
    int putIndex, takeIndex, count;

    public BoundedBuffer(int length) {
        this.length = length;
        this.items = (T[]) new Object[length];
        this.lock = new ReentrantLock();
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
        putIndex = 0; takeIndex = 0; count = 0;
    }

    public void put(T x) throws InterruptedException {
        lock.lock();
        try {
            while (count == length)
                notFull.await();
            items[putIndex] = x;
            if (++putIndex == length) putIndex = 0;
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();
            T x = items[takeIndex];
            if (++takeIndex == length) takeIndex = 0;
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}
