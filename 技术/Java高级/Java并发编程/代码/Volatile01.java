/**
 *  多核cpu的每个核心都有自己的缓存，缓存的数据可能和内存中的最新值不同
 *  使用 volatile禁用cpu缓存
 */
public class Volatile01 {

    volatile int x = 0;

    public void write(int x) {
        this.x = x;
    }

    public int read() {
        return this.x;
    }
}
