import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture可以方便的操作多个异步任务
 * 使它们可以串行、并行、组合执行
 */
public class CompletableFuture14 {
    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        // 任务 1：洗水壶 -> 烧开水
        CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> {
            System.out.println("T1: 洗水壶...");
            sleep(1, TimeUnit.SECONDS);

            System.out.println("T1: 烧开水...");
            sleep(15, TimeUnit.SECONDS);
        }, threadPool); //默认使用ForkJoinPool，是JVM启动时创建的，但是推荐使用自定义的线程池
        // 任务 2：洗茶壶 -> 洗茶杯 -> 拿茶叶
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("T2: 洗茶壶...");
            sleep(1, TimeUnit.SECONDS);

            System.out.println("T2: 洗茶杯...");
            sleep(2, TimeUnit.SECONDS);

            System.out.println("T2: 拿茶叶...");
            sleep(1, TimeUnit.SECONDS);
            return " 龙井 ";
        }, threadPool);
        // 任务 3：任务 1 和任务 2 完成后执行：泡茶
        CompletableFuture<Void> cf3 = cf1.thenCombine(cf2, (unused, s) -> {
            System.out.println("T1: 拿到茶叶:" + s);
            System.out.println("T1: 泡茶...");
            System.out.println(" 上茶:" + s);
            return null;
        });
        cf3.join();

        threadPool.shutdown();
    }

    static void sleep(int t, TimeUnit u) {
        try {
            u.sleep(t);
        }catch(InterruptedException e){}
    }
}
