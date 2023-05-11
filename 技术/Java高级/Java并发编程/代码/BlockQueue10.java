import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个Lock支持多个Condition
 * @param <T>
 */
public class BlockQueue10<T> {
    int size = 0;
    final int capacity = 10;
    final Lock lock = new ReentrantLock();
    // 条件变量：队列不满
    final Condition notFull = lock.newCondition();
    // 条件变量：队列不空
    final Condition notEmpty = lock.newCondition();

    // 入队
    void enq(T x) {
        lock.lock();
        try {
            //队列已满
            while(size == capacity) {
                // 等待队列不满时,才能继续入队
                notFull.await();
            }
            // 省略入队操作...
            // 入队后, 通知可出队
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 出队
    void deq(){
        lock.lock();
        try {
            //队列已空
            while (size == 0){
                // 等待队列不空时,才能继续出队
                notEmpty.await();
            }
            // 省略出队操作...
            // 出队后，通知可入队
            notFull.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
