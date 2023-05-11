/**
 * 演示错误的锁
 */
public class FaultLock04 {

    void foo() {
        /**
         * 每次创建一个新对象作为锁，因为锁不同，无法互斥
         * 实际上编译器会把这里的加锁代码全部删除
         */
        synchronized (new Object()) {

        }
    }
}
