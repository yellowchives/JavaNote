import java.util.concurrent.*;

public class Future13 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //Java中的线程池是一个生产者消费者模型，不能直接获取一个线程，而是把任务提交
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        //提交一个 Callable，返回一个Future，结果封装在Future中
        Future<Integer> submit = executorService.submit(() -> 1 + 2);
        Integer integer = submit.get();
        System.out.println(integer);

        //提交一个FutureTask（Runnable的实现类），不会返回Future，但是可以通过FutureTask得到结果
        FutureTask<Integer> futureTask = new FutureTask<>(() -> 1 + 2);
        executorService.submit(futureTask);
        System.out.println(futureTask.get());

        executorService.shutdown();
    }
}
