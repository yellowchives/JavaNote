import java.util.Random;
import java.util.concurrent.*;

/**
 * CompletionService将线程池和阻塞队列结合起来
 * 可以批量提交异步任务，先完成的任务的返回值Future对象会被先加到队列中
 */
public class CompletionService15 {

    static int count = 1;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3, r -> {
            Thread thread = new Thread(r);
            thread.setName("线程" + count);
            count++;
            return thread;
        });
        // 创建 CompletionService
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executorService);

        Random random = new Random();
        //提交3个异步任务，执行时间是随机数
        for (int i = 0; i < 3; i++) {
            cs.submit(() -> {
                int num = random.nextInt(10);
                Thread.sleep(TimeUnit.SECONDS.toMillis(num));
                System.out.println(Thread.currentThread().getName() + "执行完毕，耗时：" + num);
                return num;
            });
        }

        //获取执行结果
        for (int i = 0; i < 3; i++) {
            Future<Integer> future = cs.take();
            System.out.println(future.get());
        }

        executorService.shutdown();
    }

    /**
     * 使用阻塞队列批量保存异步任务的结果
     * @throws ExecutionException
     * @throws InterruptedException
     */
    void test() throws ExecutionException, InterruptedException {
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3, r -> {
            Thread thread = new Thread(r);
            thread.setName("线程" + count);
            count++;
            return thread;
        });
        //阻塞队列
        BlockingQueue<Integer> bq = new LinkedBlockingQueue<>();

        Random random = new Random();
        //提交3个异步任务，执行时间是随机数
        //返回值保存到阻塞队列中
        bq.put(executorService.submit(() -> {
            int num = random.nextInt(10);
            Thread.sleep(TimeUnit.SECONDS.toMillis(num));
            System.out.println(Thread.currentThread().getName() + "执行完毕，耗时：" + num);
            return num;
        }).get());

        for (Integer integer : bq) {
            System.out.println(integer);
        }
    }
}
