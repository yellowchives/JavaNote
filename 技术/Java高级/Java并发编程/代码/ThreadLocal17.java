import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocal17 {

    //static代表ThreadLocal其实是进程共享的,但是每个线程有自己的value,保存在线程私有的ThreadLocalMap中
    //一个线程只有一个ThreadLocalMap,但是一个map里有多个键值对,key就是ThreadLocal变量,value是线程私有的值
    static ThreadLocal<String> threadLocalString = new ThreadLocal<String>() {
        //此方法的作用是当调用get()方法获取值为null的时候,自动初始化值
        @Override
        protected String initialValue() {
            return Thread.currentThread().getName();
        }
    };
    static ThreadLocal<Integer> threadLocalInteger = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 1000; i++) {
            final int total = i;
            executorService.submit(() -> {
                //获取当前线程私有的值
                String name = threadLocalString.get();
                Integer count = threadLocalInteger.get();
                System.out.println("正在执行第" + total + "次数," + "线程" + name + "执行第" + count + "次");
                count++;
                threadLocalInteger.set(count);
            });
        }

        executorService.shutdown();
    }

}
