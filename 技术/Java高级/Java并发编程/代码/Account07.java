import java.util.ArrayList;
import java.util.List;

/**
 * 一次性获取两把锁
 */
public class Account07 {

    // actr 应该为单例
    private Allocator actr;
    private int balance;

    public void withdraw(Account07 target, int amt) {

        // 一次性申请转出账户和转入账户，直到成功
        actr.apply(this, target);

        try {
            // 锁定转出账户
            synchronized (this) {
                // 锁定转入账户
                synchronized (target) {
                    if (this.balance > amt) {
                        this.balance -= amt;
                        target.balance += amt;
                    }
                }
            }
        } finally {
            actr.free(this, target);
        }

    }

    class Allocator {
        private List<Object> als = new ArrayList<>();

        // 一次性申请所有资源
        public synchronized void apply(Object from, Object to) {
            if (als.contains(from) || als.contains(to)) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            als.add(from);
            als.add(to);
        }

        //归还资源
        public synchronized void free(Object from, Object to) {
            als.remove(from);
            als.remove(to);
            notifyAll();
        }
    }
}