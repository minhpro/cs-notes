package my_group;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MutexLock {
    private Sync sync = new Sync();

    public void lock() { sync.acquire(1); }
    public boolean tryLock() { return sync.tryAcquire(1);}
    public void unlock() { sync.release(1); }

    // Internal synchronizer helper class
    private static class Sync extends AbstractQueuedSynchronizer {
        public boolean tryAcquire(int arg) {
            assert arg == 1;
            return compareAndSetState(0, 1);
        }
    }
}
