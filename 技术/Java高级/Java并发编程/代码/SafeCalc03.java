/**
 * 使用互斥锁实现并发安全的加法
 */
public class SafeCalc03 {
    private int count = 0;

    synchronized void addOne() {
        this.count ++;
    }

    /**
     * 其实get方法最好也加上锁
     * 这里使用synchronized和使用volatile修改count效果一样
     * 都是禁用了cpu缓存，确保每次从内存中读取值
     * @return
     */
    synchronized int get() {
        return this.count;
    }
}
