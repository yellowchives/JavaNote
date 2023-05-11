import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁的演示
 */
public class ReentrantLock08 {

    private int count;
    private Lock lock = new ReentrantLock();

    public void addOne() {
        //获取锁
        lock.lock();
        try {
            //get()方法也会获取锁，但这里使用的是可重入锁，同一个线程可以重复获取
            this.count = 1 + get();
        } finally {
            //释放锁
            lock.unlock();
        }

    }
    public int get() {
        lock.lock();
        try {
            return this.count;
        } finally {
            lock.unlock();
        }
    }
}
