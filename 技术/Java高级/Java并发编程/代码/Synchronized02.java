/**
 * synchronized 是互斥锁，加锁和释放是自动进行的
 * 使用了互斥锁，共享变量就不需要使用volatile了，Java可以保证上锁的代码块里做的操作一定能被其他线程看到
 */
public class Synchronized02 {

    /**
     * 修饰非静态方法的时候，锁定的是当前实例对象this
     */
    synchronized void foo() {

    }

    /**
     * 修饰静态方法的时候，锁定的是当前类的Class对象
     */
    synchronized static void bar() {

    }

    /**
     * 修改代码块
     * 自己指定的要锁定的对象
     */
    Object object = new Object();
    void baz() {
        synchronized (object) {

        }
    }
}
