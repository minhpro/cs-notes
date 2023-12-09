package my_group;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private static AtomicInteger nextAccountId = new AtomicInteger(1);
    private final int accountId;
    private double balance;
    private final double atmFreePercent = 0.01;


    Lock accountLock = new ReentrantLock();

    public Account(int openingBalance) {
        this.balance = openingBalance;
        this.accountId = nextAccountId.getAndIncrement();
    }

    public double getRawBalance() {
        return balance;
    }

    public double safeGetBalance() {
        synchronized (this) {
            return balance;
        }
    }

    public boolean rawWithDraw(int amount) {
        if (balance >= amount) {
            balance = balance - amount;
            return true;
        }
        return false;
    }

    public boolean safeWithDraw(final  int amount) {
        synchronized (this) {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
        }
        return false;
    }

    public boolean safeWithDraw(final  int amount, final boolean withFee) {
        synchronized (this) {
            if (balance >= amount) {
                balance -= amount;
                if (withFee)
                    balance = balance - amount * atmFreePercent;
                return true;
            }
        }
        return false;
    }

    public void rawDeposit(int amount) {
        balance += amount;
    }

    public void safeDeposit(final int amount) {
        synchronized (this) {
            balance += amount;
        }
    }

    public void safeDepositWithLock(final int amount) {
        accountLock.lock();
        try {
            balance += amount;
        } finally {
            accountLock.unlock();
        }
    }

    public boolean safeTransfer(Account other, int amount) {
        if (accountId == other.accountId) {
            // Can't transfer to your own account
            return false;
        }

        if (accountId < other.accountId) {
            synchronized (this) {
                if (balance >= amount) {
                    balance = balance - amount;
                    synchronized (other) {
                        other.rawDeposit(amount);
                    }
                    return true;
                }
            }
        } else {
            synchronized (other) {
                synchronized (this) {
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
}
