import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 非阻塞式获取锁
 */
public class Account09 {

    private int balance;
    public Lock lock = new ReentrantLock();

    public void withdraw(Account09 target, int amt) {
        while(true) {
            //tryLock() 支持非阻塞方式获取锁
            //这个方法可能产生活锁。互相持有各自的锁，发现需要的对方的锁都被对方持有，就会释放当前持有的锁，导致大家都在不停持锁，释放锁，但事情还没做。
            if (this.lock.tryLock()) {
                try {
                    if (target.lock.tryLock()) {
                        try {
                            if (this.balance > amt) {
                                this.balance -= amt;
                                target.balance += amt;
                                break;
                            }
                        } finally {
                            target.lock.unlock();
                        }
                    }
                    // 新增：sleep 一个随机时间避免活锁
                    TimeUnit.SECONDS.sleep(new Random().nextInt());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    this.lock.unlock();
                }
            }
        }
    }
}
