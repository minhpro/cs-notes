# Threads and Locks

References from Java SE spec: https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html

## Synchronization

Trong Java, một các cơ bản để đồng bộ các threads khi truy cập shared data là sử dụng `synchronization`. Mỗi object trong Java sẽ được gán kèm với một **monitor**, thread có thể *lock* hay *unlock* monitor. Tại một thời điểm, chỉ có nhiều nhất một thread có thể hold một lock trên một monitor, các thread khác khi attempt to lock monitor bị blocked cho tới khi obtain a lock trên monitor. 

The `synchronized` keyword được dùng để acquire lock trên monitor của object và tự động release lock của monitor khi kết thúc hàm (hay statement, block).

## Wait Sets and Notification

Mỗi object đều có một *wait set** đi kèm. Một wait set là một tập các threads.

### Wait action

Dùng để suspend thread với một điều kiện nhất định, thread sẽ chờ đến hết điều kiện này thì tiếp tục xử lý.

- Gọi hàm `wait()` (hay timed form `wait(long millisecs, int nanosecs)`)
- Trước khi `wait` on object monitor thì cần phải lock nó (`synchronized`), nếu ko sẽ throws `IllegalMonitorStateException`: current thread is not owner
- Thread được thêm vào wait set of của object, và đồng thời unlock monitor của object
- Thread sẽ không chạy tiếp cho đến khi được remove khỏi wait set của object bởi một trong các sự kiện sau
  - Action **notify** trên object và thread được chọn để remove từ wait set của object.
  - Action **notifyAll** trên object.
  - Thread bị interrupt
  - Nếu là timed wait thì hết thời gian wait, thread sẽ được remove khỏi wait set.
  - Trường hợp *spurious wake-ups* từ OS, trường hợp này chúng ta không dự đoán được nên có thể khi wake up mà điều kiện wait chưa hết thì thread lại wait tiếp.

### Notification action

Notify action được dùng để remove thread khỏi wait set của object, bằng cách gọi hàm `notify` hay `notifyAll` của object.

- Để gọi notify trên một object thì trước đó cần acquire được monitor's lock của object đó.
- Một thread (notifyAll là tất cả thread) trong wait set của object được remove, và do đó resume execution.

Dù tất cả thread trong wait set được remove, nhưng tại một thời điểm thì một trong số chúng acquire được lock của monitor của object và tiếp tục xử lý.

Cấu trúc xử dụng object **monitor** và **wait set** sẽ có kiểu như sau

```Java
synchronized(this) {
  // before wait
  while(condition) {
    wait();
  }
  // update shared data
  notify();
}
// something else
```

## Volatile variables

Java cung cấp một cách thức weaker form of synchronization là `volatile` variables. Khi một variable được khai báo là `volatile`, lúc này compliler và runtime sẽ biết là biến này sẽ được shared và các operation trên nó không nên bị sai thứ tự so với các thao tác memory. Volatile variables sẽ **không được cached** trong **registers** hay **caches** (L1, L2, ...), là những nơi mà chúng có thể bị ẩn giữa các processors, do đó việc đọc (read) một biến volatile luôn luôn trả về giá trị được viết (write) gần nhất bởi bất kỳ thread nào.

Có thể hình dung biến volatile như class `SynchronizedInteger` như sau:

```Java
public class SynchronizedInteger {
  int value;

  public synchronized int get() { return this.value; }

  public synchronized void set(int newValue) { this.value = newValue; }
}
```

Tuy nhiên việc dùng biến volatile sẽ không cần sử dụng tới cơ chế `locking`. Writing a volatile variable and reading a volatile variable is like entering a `synchronized block`. Tuy vậy, **không khuyến khích** sử dụng biến volatile, vì nó khó hiểu và không rõ ràng như sử dụng locking. Trong đại đa số trường hợp, thì chúng ta nên dùng **Lock** hay **Atomic variable** thay vì biến volatile.

> Locking can guarantee both visibility and atomicity; volatile variables can only guarantee visibility.

# Concurrency Problems

Let take an example about bank account withdrawing and depositing functions.

```Java
public class Account {
  private double balance;
  private final double atmFeePercent = 0.01;

  public Account(int openingBalance) {this.balance = openingBalance;}

  public double getBalance() { return this.balance; }

  public boolean rawWithDraw(int amount) {
    if (balance >= amount) {
      balance = balance - amount;
      return true;
    }
    return false;
  }

  public void rawDeposit(int amount) {
    balance = balance + amount;
  }
}
```

Xem chi tiết implementation trong file [Account](./my-java-app/src/main/java/my_group/Account.java)

## Lost Update

Xét 2 thread cùng deposit một account:
- Tạo một account với balance = 0.
- Thread 1: chạy một vòng lặp (1000 lần), mỗi vòng deposit account thêm 1$.
- Thread 2: cũng như vậy.

```Java
Account acc = new Account(0);
// Lost update problem
Thread tA = new Thread(() -> {
    for (int i = 0; i < 1000; i++) acc.rawDeposit(1);
});
Thread tB = new Thread(() -> {
    for (int i = 0; i < 1000; i++) acc.rawDeposit(1);
});
tA.start();
tB.start();
tA.join();
tB.join();
System.out.println(acc.getRawBalance()); // should be 2000
```

Kết quả mong muốn cuối cùng là balance của account = 2000. Tuy vậy mỗi lần chạy ta lại thu được kết quả khác nhau và đều là nhỏ hơn 2000. Vấn đề ở đây gọi là **Lost Update** (do you know why?).

Chúng ta nhìn vào statement của hàm `rawDeposit()`

`balance = balance + amount`

Trên thực tế thì câu statement này sẽ gồm 2 lệnh:
- Lấy giá trị của balance: READ
- Cập nhật giá trị của balance: WRITE

Xét một trường hợp thực thi có thể xảy ra đối với 2 threads như sau:

> A: READ (balance = 0)
>                                B: READ (0)
>                                B: WRITE (1)
> A: WRITE (1)

Như vậy, tác động update giá trị balance của B sẽ không có tác dụng.

Một cách giải quyết vấn đề này là sử dụng cơ chế **lock** (hay gọi là **mutual exclusion** lock - mutex), khi có nhiều thread cùng truy cập tài nguyên dùng chung **share resource** thì cần:
- Tạo ra một **lock**.
- Thread muốn truy cập resource thì cần **acquire** lock, nếu lock đã bị acquired thì thread sẽ cần chờ.
- Thực hiện thao tác với resource.
- Sau khi xong thì **release** lock để thread khác có thể sử dụng.

Trong Java, có ta thường dùng keyword **synchronize** để acquire lock và tự động release lock, cũng như notify cho các thread khác. Sau đây là version synchronized của hàm `deposit`. Các thread sẽ sử dụng hàm `safeDeposit` mới này.

```Java
public void safeDeposit(int amount) {
  synchronized(this) {
    if (balance >= amount) {
      balance = balance - amount;
      return true;
    }
  }
  return false;
}
```

Ở đây, lock chính là object hiện tại **this**. Và cụ thể cơ chế lock & notify này còn được gọi là **monitor** hay **condition variable**. Các khái niệm **mutex** hay **monitor** được trình bày trong khái niệm **concurrency** chung của lập trình. Xem thêm [Concurrecy](../../foundation/operating-system/concurrency.md)

## Read temporary value (Uncommitted read)

Xét một function withDraw sau đã sử dụng synchronize để cập nhật balance.

```Java
public boolean safeWithDraw(int amount, boolean withFee) {
  synchronized(this) {
    if (balance >= amount) {
      balance = balance - amount; // 90
      if (withFee) {
        balance = balance - amount * atmFeePercent; // 89
      }
      return true;
    }
  }
  return false;
}
```

Có một `account` có balance là 100, một thread A thực hiện `account.safeWithDraw(10, true)`, trong khi đó cũng có một thread B lấy ra balance của account như sau

```Java
double balance = account.getBalance();
System.out.println("Your balance is " + balance);
```

Nếu Thread A chạy xong, Thread B mới chạy thì thông báo balance là 89.9 (một trạng thái đó của account). Tuy nhiên nếu chạy 2 thread đồng thời thì có thể B sẽ thông báo balance của account là 90 (một trạng thái không thể tồn tại của account).

Lý do ở đây là Thread B không có **acquire** lock khi get balance của account, và nó có thể lấy giá trị sau khi A thực hiện dòng code `balance = balance - amount` (how to fix it?).

## Deadlock

Deadlock là vấn đề khi có nhiều Thread chờ đợi lẫn nhau và thế là không thread nào chạy tiếp cả. Xét ví dụ chuyển tiền giữa các tài khoản như sau:

```Java
public boolean naiveSafeTransferTo(Account other, int amount) {
  synchronized(this) {
    if (balance >= amount) {
      balance = balance - amount;
      synchronized(other) {
        other.rawDeposit(amount);
      }
      return true;
    }
  }
  return false;
}
```

Có 2 account A và B, nếu có 2 thread cùng thực hiện chuyển tiền từ A -> B và từ B -> A, khi đó có thể dẫn đến 2 thread này chờ lẫn nhau (why?).

Deadlock xử lý ra khi có sự chờ đợi vòng tròn.

Thread tA giữ lock A và chờ lock B được giữ bởi tB, tB lại chờ lock A, khi này tA chờ tB và tB lại chờ tA và gọi là chờ đợi vòng tròn.

Vấn đề deadlock thì xảy ra khi mỗi thread giữ một lock và chờ lock đang giữ bởi thread kia, do đó để tránh tình trạng này ta có thể giải quyết bằng cách quy định các thread luôn **acquire** locks theo **cùng một thứ tự** nhất định. Ví dụ, mỗi account được gán cho một id duy nhất gọi là **accountId**, thứ tự acquire locks theo thứ tự tăng dần accountId.

```Java
public boolean safeTransfer(Account other, int amount) {
  if (accountId == other.accountId) {
    // Can't transfer to your own account
    return false;
  }

  if (accountId < other.accountId) {
    synchronized(this) {
       if (balance >= amount) {
        balance = balance - amount;
        synchronized(other) {
          other.rawDeposit(amount);
        }
        return true;
      }
    }
  } else {
    synchronized(other) {
      synchronized(this) {
        if (balance >= amount) {
          balance = balance - amount;
          other.rawDeposit(amount);
          return true;
        }
      }
    }
  }
  
  return false;
}
```

# Atomic classess

Many concurrency problems happened when we cannot update the share data atomically. Package `java.util.concurrenct.atomic` cung cấp các classes hỗ trợ update dữ liệu một cách atomic, như vậy sẽ tránh được trường hợp read giá trị uncommited.

Ví dụ khi gán `accountId` cho mỗi account khi tạo mới ta sẽ tăng giá trị hiện tại của `nextAccountId`.

```Java
private static AtomicInteger nextAccountId = new AtomicInteger(1);
private final int accountId;

public Account(int openingBalance) {
    this.balance = openingBalance;
    this.accountId = nextAccountId.getAndIncrement();
}
```

Hàm `AtomicInteger.getAndIncrement()` đảm bảo việc cập nhật giá trị là atomic, cho phép nhiều thread gọi mà không có giá trị accountId nào trùng nhau cả.

Nếu giá trị là object thì chúng ta có thể sử dụng `AtomicReference` class. Pattern chung cho các API của các class `Atomic` là `Compare-and-swap` operation. Tham khảo [Compare and swap](https://en.wikipedia.org/wiki/Compare-and-swap).

Ví dụ về `ConcurrentStack` sử dụng `AtomicReference` để xây dựng một thread-safe Stack như sau:

```Java
/**
 * ConcurrentStack
 *
 * Nonblocking stack using Treiber's algorithm
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class ConcurrentStack <E> {
    AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();

    public void push(E item) {
        Node<E> newHead = new Node<E>(item);
        Node<E> oldHead;

        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }

    public E pop() {
        Node<E> oldHead;
        Node<E> newHead;

        do {
            oldHead = top.get();
            if (oldHead == null)
                return null;
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));

        return oldHead.item;
    }

    private static class Node <E> {
        public final E item;
        public Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }
}
```

# Lock classess

Trước giờ chúng ta sử dụng block-structure (key word `synchronized`) để synchronization. Thực tế thì `synchronized` block sử dụng `lock` để synchronization:
- Lock được acquired ở đầu block
- Và được release ở cuối block
- Hoặc là lock được acquired hoặc là thread bị block không xác định (không lựa chọn được block như thế nào, ví dụ như timeout chẳng hạn).

Package JDK `java.util.concurrent.locks` cung cấp API sử dụng `lock` một cách trực tiếp:
- Sử dụng các loại lock khác nhau: reader hay writer locks.
- Cho phép timeout khi acquire locks hay không block khi acquire locks.

Lock có interface như sau:

```Java
public interface Lock { 
  void lock();
  void lockInterruptibly() throws InterruptedException; 
  boolean tryLock();
  boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException; 
  void unlock();
  Condition newCondition(); 
}
```

**ReentrantLock** implements `Lock` cung cấp chức năng mutual exclusion giống hệt như `synchronized` block:
- Acquire ReentrantLock giống như bắt đầu block `synchronized`.
- Release ReentrantLock giống như như kết thúc block `synchronized`.

Tuy nhiên, khác với việc dùng `synchronized` block thì ta có thể có nhiều tùy chọn hơn như là block hay không block, block acquire lock with timeout, ... Về cơ bản thì mẫu code sử dụng **ReentrantLock** như sau:

```Java
Lock lock = new ReentrantLock();
...
lock.lock();
try {
  // update share state
} finally {
  lock.unlock();
}
```

Ví dụ khi `deposite` Account sử dụng lock, mỗi account ta khai báo một **ReentranLock**.

```Java
Lock accountLock = new ReentrantLock();
...

public void safeDepositWithLock(final int amount) {
    accountLock.lock();
    try {
        balance += amount;
    } finally {
        accountLock.unlock();
    }
}
```

## Unblock and timeout lock acquisition

Hàm `Lock.lock()` sẽ block thread cho tới khi lock release, và để tránh block thread một cách không xác định, chúng ta sử dụng hàm `Lock.tryLock()` hoặc block với timeout `Lock.tryLock(timeout, timeUnit)`. Mẫu sử dụng `tryLock` như sau:

```Java
Lock lock = ...;
if (lock.tryLock()) {
  try {
    // manipulate protected state
  } finally {
    lock.unlock();
  }
} else {
  // perform alternative actions
}
```

## Condition Objects

From Java docs: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/Condition.html

> Condition factors out the Object monitor methods (wait, notify and notifyAll) into distinct objects to give the effect of having multiple wait-sets per object, by combining them with the use of arbitrary Lock implementations. Where a Lock replaces the use of synchronized methods and statements, a Condition replaces the use of the Object monitor methods.

Xét bài toán sau, có một bounded buffer để lưu trữ message, nó support 2 phương thức là `put` và `take`. Nếu gọi `take` khi buffer emtpty thì thread sẽ block đến khi có item available; nếu gọi `put` khi buffer full thì thread sẽ block đến khi space available. Chúng ta sẽ implement bounded buffer này như sau (sử dụng Lock và Condition objects).

```Java
class BoundedBuffer<E> {
  final Lock lock = new ReentrantLock();
  final Condition notFull  = lock.newCondition(); 
  final Condition notEmpty = lock.newCondition(); 

  final Object[] items = new Object[100];
  int putIndex, takeIndex, count;

  public void put(E x) throws InterruptedException {
    lock.lock();
    try {
      while (count == items.length)
        notFull.await();
      items[putIndex] = x;
      if (++putIndex == items.length) putIndex = 0;
      ++count;
      notEmpty.signal();
    } finally {
      lock.unlock();
    }
  }

  public E take() throws InterruptedException {
    lock.lock();
    try {
      while (count == 0)
        notEmpty.await();
      E x = (E) items[takeIndex];
      if (++takeIndex == items.length) takeIndex = 0;
      --count;
      notFull.signal();
      return x;
    } finally {
      lock.unlock();
    }
  }
}
```

Xem ví dụ sử dụng trong [code](./my-java-app/src/main/java/my_group/LockBaseSynchronization.java)

Thực tế thì cách implement trên được áp dụng trong class `ArrayBlockingQueue` của Java SE.

## Read-write locks

Trong nhiều bài toán concurrency, có nhiều thread chỉ đọc dữ liệu (reader) hơn là thread ghi dữ liệu (writer). Việc sửa dụng **mutual lock** như là **ReentrantLock** sẽ over then need, chúng ta có thể nới lỏng một phần locking để tăng hiệu năng của hệ thống. Ý tưởng là sử dụng một cặp **read lock** và **write lock** để concurrency access shared resource.

Trong Java, ý tưởng này nằm ở interface **ReadWriteLock**

```Java
public interface ReadWriteLock {
  Lock readLock()
  Lock writeLock()
}
```

Trước khi read resource thì thread cần acquire được **readLock**, còn nếu muốn cập nhật resource thì cần acquire được **writeLock**.

**ReadWriteLock** cho phép tại một thời điểm, một resource có thể được access hoặc bởi nhiều `readers` hoặc duy nhất một writer, không cả hai.

Xét ví dụ sử dụng **ReentrantReadWriteLock** (một implement của ReadWriteLock) để phát triển một **ConcurrencyReadWriteMap**.

```Java
public class ReadWriteMap<K, V> {
  private final Map<K, V> map;
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock r = lock.readLock();
  private final Lock w = lock.writeLock();

  public ReadWriteMap(Map<K, V> map) {
    this.map = map;
  }

  public V put(K key, V value) {
    w.lock();
    try {
      return map.put(key, value);
    } finally {
      w.unlock();
    }
  }
  // Do the same for remove(), putAll(), clear()

  public V get(K key) {
    r.lock();
    try {
      return map.get(key);
    } finally {
      r.unlock();
    }
  }
  // Do the same for other read-only Map methods
}
```

Để hiểu hơn, hãy đọc source code và docs của **ReentrantReadWriteLock**.

# Build Custom Synchronizer

Nhiều class dạng lock-based synchronization đều sử dụng `AbstractQueueSynchronizer` (AQS) trong implementation của mình, ví dụ như `ReeantrantLock`, `Semaphore` (limited concurrent accesses), `ReeantrantReadWriteLock`, `CountDownLatch`, hay `FutureTask`.

AQS-based operations are `acquire` and `release`. Acquisition là thao tác `state-dependent` và có thể bị block. Với lock và semaphore thì acquisition có nghĩa là acquire lock hay a permit -- caller có thể phải wait đến khi synchronizer ở một trạng thái (state) nào đó. Ví dụ với `CountDownLatch` thì acquire có nghĩa là "chờ đến khi latch đạt đến terminate state", với `FutureTask` là "wait util the task has completed". Release là thao tác không blocking, một release có thể kích hoạt điều kiện để threads bị blocked ở acquire tiếp tục xử lý.

Khi sử dụng, AQS sẽ có nhiệm vụ quản lý các trạng thái của **synchronizer** class, trên thực tế nó quản lý một biến integer **state**, và được protected qua các phương thức `getState`, `setState`, và `compareAndSetState`. Biến này có thể đại diện cho bất kỳ trạng thái nào của synchronizer class, ví dụ, đối với `Semaphore` thì nó được dùng để thể hiện số lượng permits còn lại, còn `FutureTask` thì nó được dùng để mô tả trạng thái của task (not yet started, running, completed, cancelled). 

AQS cung cấp `acquire` và `release` được mô tả kiểu canonical như sau:

```Java
boolean acquire() throws InterruptedException {
  while (state does not permit acquire) {
    if (blocking acquisition requested) {
      enqueue current thread if not already queued
      block current thread
    }
    else
      return failure
  }
  possibly update synchronization state
  dequeue thread if it was queued
  return success
}

void release() {
  update synchronization state
  if (new state may permit a blocked thread to acquire)
    unblock on or more queued threads
}
```

Sau đây là một implement của `MutexLock` sử dụng AQS, zero là unlocked state, and one là locked state.

```Java
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
```

# Asynchronous Programming

## Futures

Interface `Future` của `java.util.concurrent` represents the result of an asynchronous computation.

```Java
public interface Future<V> {
  /**
   * Waits if necessary for the computation to complete, and then
   * retrieves its result.
   *
   * @return the computed result
   * @throws CancellationException if the computation was cancelled
   * @throws ExecutionException if the computation threw an
   * exception
   * @throws InterruptedException if the current thread was interrupted
   * while waiting
   */
  V get() throws InterruptedException, ExecutionException;
}
```

Future có phương thức `get` để chờ lấy kết quả của task sau khi complete. Ngoài ra còn các phương thức khác như `cancel`, `get` với timeout waiting, `isCancelled`, `isDone`.

Cách lấy kết quả của một asynchronous computation thông qua `Future` được mô tả như sau:

```Java
Future<V> asyncResult = asynchronousComputation(); // asynchronous computation start here.
V value = asyncResult.get(); // wait computation complete and return the result.
```

Để tạo asynchronous computation, chúng ta sử dụng class `CompletableFuture`.

Ví dụ sử dụng CompletableFuture để tạo asynchronous computation thông qua Thread (thread based).

```Java
public static Future<Long> getNthPrime(int n) {
  var numF = new CompletableFuture<Long>();

  new Thread( () -> {
    long num = findPrime(n); // long computation here
    numF.complete(num);
  }).start();

  return numF;
}
```

Cách khác đơn giản hơn là sử dụng `supplySync`

```Java
Future<Long> asyncResult = CompletableFuture.supplyAsync(() -> findPrime(n));
```

Với cách này asynchronous computation sẽ được chạy thông qua executor và thread pool, thay vì cách sử dụng thread phía trên.

# Tasks and thread pool

Chúng ta có các loại thread pool sau:

**Single thread pool**

Kết hợp một thread để chạy một task queue (a blocking queue)

```Java
var pool = Executors.newSingleThreadExecutor();
Runnable task = () -> runTask();
pool.submit(task);
```

**Fixed thread pool**

Tạo một số lượng nhất định threads để thực hiện task, các thread này đều được reuse.

```Java
var pool = Executors.newFixedThreadPool(2);
```

**Cached thread pool**

Is unbound thread pool, threads are kept in the idle cache for 60 seconds.

```Java
var pool = Executors.newCachedThreadPool();
```

**Scheduled thread pool**

Dùng đặt lịch chạy task.

```Java
ScheduledExecutorService pool  = Executors.newScheduledThreadPool(4);
```

Các phương thức dùng để schedule task bao gồm:

```Java
/**
 * Submit một one-shot task và enabled sau khoảng given delay
 */
ScheduledFuture<?> schedule(Runnable command,
                            long delay,
                            TimeUnit unit);
/**
 * Submits a periodic action that becomes enabled first after the given initial delay, and subsequently
 * with the given period; that is, executions will commence after initialDelay, then initialDelay + 
 * period, then initialDelay + 2 * period, and so on
 */
ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                      long initialDelay,
                                      long period,
                                      TimeUnit unit);
/**
 * Submits a periodic action that becomes enabled first after the given initial delay, and subsequently
 * with the given delay between the termination of one execution and the commencement of the next
 */
ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                          long initialDelay,
                                          long delay,
                                          TimeUnit unit);
```

# Parallel Programming via the Fork/Join Framework

Fork/Join framework cung cấp phương pháp giải quyết bài toán theo hướng `Divide and conquer`, từ một task sẽ chia thành các task nhỏ để thực hiện (có thể chạy song song tăng hiệu năng), rồi gộp kết quả của các task nhỏ lại cho ra kết quả của task to. Cách chia task như vậy có thể được thực hiện đệ quy (recursive).

Xét ví dụ tính tổng của một dãy số, chúng ta sẽ chia đôi dãy số ra để tính từng phần rồi cộng lại, việc chia đôi này được thực hiện đệ quy cho đến khi số lượng số cần tính tổng < threshold (=100 chẳng hạn).

```Java
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

public void main(String[] args) {
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
```

`ForkJoinPool.invoke()` sẽ wait đến khi task được thực hiện complete và trả về kết quả. Nếu muốn asynchronous method thì sử dụng `ForkJoinPool.execute()`.


Reading:
- Java The Complate References: Parallel Programming via the Fork/Join Framework (Chaper 29)
