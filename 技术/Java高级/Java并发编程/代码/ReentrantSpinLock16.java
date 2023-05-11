import java.util.concurrent.atomic.AtomicReference;

/**
 * 自定义一个可重入的自旋锁
 */
public class ReentrantSpinLock16 {

    //当前锁的持有线程
    //AtomicReference会出现CAS里的ABA问题,解决方案是使用AtomicStampedReference类
    private AtomicReference<Thread> owner = new AtomicReference<>();

    //重入次数
    private int count = 0;

    public void lock() {
        Thread t = Thread.currentThread();
        //重入了
        if (t == owner.get()) {
            count ++;
            return;
        }
        //自旋获取锁
        while (!owner.compareAndSet(null, t)) {
            //System.out.println("自旋了一次");
        }
    }

    public void unlock() {
        Thread t = Thread.currentThread();
        //只有持有锁的线程才能解锁
        if (t == owner.get()) {
            if (count > 0) {
                count --;
            } else {
                //此处无需CAS操作，因为没有竞争，因为只有线程持有者才能解锁
                owner.set(null);
            }
        }
    }

    public static void main(String[] args) {
        ReentrantSpinLock16 lock = new ReentrantSpinLock16();
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() + "开始获取自旋锁");
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "获取到了自旋锁");
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        };

        new Thread(runnable, "线程一").start();
        new Thread(runnable, "线程二").start();
    }
}
