/**
 * 演示死锁
 */
public class MustDeadLock implements Runnable{

    boolean flag;
    static Object o1 = new Object();
    static Object o2 = new Object();

    /**
     * 先使用jps查看pid, 再使用 jstack查看死锁的情况
     *
     * "Thread-1" #13 prio=5 os_prio=0 tid=0x00000205f5259000 nid=0x3364 waiting for monitor entry [0x000000f8644ff000]
     *    java.lang.Thread.State: BLOCKED (on object monitor)
     *         at MustDeadLock.run(MustDeadLock.java:42)
     *         - waiting to lock <0x00000000d637bd90> (a java.lang.Object)
     *         - locked <0x00000000d637bda0> (a java.lang.Object)
     *         at java.lang.Thread.run(Thread.java:750)
     *
     * "Thread-0" #12 prio=5 os_prio=0 tid=0x00000205f5258000 nid=0x1f80 waiting for monitor entry [0x000000f8643ff000]
     *    java.lang.Thread.State: BLOCKED (on object monitor)
     *         at MustDeadLock.run(MustDeadLock.java:31)
     *         - waiting to lock <0x00000000d637bda0> (a java.lang.Object)
     *         - locked <0x00000000d637bd90> (a java.lang.Object)
     *         at java.lang.Thread.run(Thread.java:750)
     * Found 1 deadlock.
     * @param args
     */
    public static void main(String[] args) {
        MustDeadLock mustDeadLock1 = new MustDeadLock();
        MustDeadLock mustDeadLock2 = new MustDeadLock();
        mustDeadLock1.flag = true;
        mustDeadLock2.flag = false;
        new Thread(mustDeadLock1).start();
        new Thread(mustDeadLock2).start();

    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "的flag为:" + flag);
        if (flag) {
            synchronized (o1) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    System.out.println(Thread.currentThread().getName() + "或得了两把锁");
                }
            }
        } else {
            synchronized (o2) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    System.out.println(Thread.currentThread().getName() + "或得了两把锁");
                }
            }
        }
    }
}
