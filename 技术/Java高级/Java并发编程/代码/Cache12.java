import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

/**
 * 使用StampedLock实现缓存
 * <p>
 * 支持三种模式：写锁、悲观读锁和乐观读，注意乐观读是无锁
 * 因为支持乐观读，所以性能优于读写锁。但是是不可重入的
 * StampedLock加锁成功之后，都会返回一个stamp；然后解锁的时候，需要传入这个 stamp。
 */
public class Cache12<K, V> {
    final Map<K, V> m = new HashMap<>();
    final StampedLock stampedLock = new StampedLock();

    V get(K key) {
        // 乐观读
        long stamp = stampedLock.tryOptimisticRead();
        try {
            // 读入局部变量，
            // 读的过程数据可能被修改
            V v = m.get(key);
            // 判断执行读操作期间，
            // 是否存在写操作，如果存在，
            // 则 sl.validate 返回 false
            if (!stampedLock.validate(stamp)) {
                // 升级为悲观读锁
                stamp = stampedLock.readLock();
                v = m.get(key);
            }
            return v;
        } finally {
            //释放锁
            stampedLock.unlock(stamp);
        }
    }

    // 写缓存
    V put(K key, V v) {
        long stamp = stampedLock.writeLock();
        try {
            return m.put(key, v);
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }
}
