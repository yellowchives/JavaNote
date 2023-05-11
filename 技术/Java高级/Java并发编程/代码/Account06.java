public class Account06 {
    private int id;
    private int balance;


    /**
     * 转账操作怎么上锁
     * 最简单的方式是把Class对象锁住，但是性能太差
     * @param target
     * @param amt
     */
    // 转账
    void transfer(Account06 target, int amt) {
        synchronized (Account06.class) {

        }
    }

    /**
     * 第二种方式是使用两个账号对象作为锁
     * 但是可能产生死锁
     */
    void transfer02(Account06 target, int amt) {
        synchronized (this) {
            synchronized (target) {
                if (this.balance > amt) {
                    this.balance -= amt;
                    target.balance += amt;
                }
            }
        }
    }

    /**
     * 解决死锁的简单方法：使用特定的顺序来加锁
     */
    void transfer03(Account06 target, int amt) {
        Account06 left = this;
        Account06 right = target;
        if (target.id < this.id) {
            left = target;
            right = this;
        }
        synchronized (left) {
            synchronized (right) {
                if (this.balance > amt) {
                    this.balance -= amt;
                    target.balance += amt;
                }
            }
        }
    }

}
