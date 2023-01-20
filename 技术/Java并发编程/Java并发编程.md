# 基本概念

https://www.cnblogs.com/dolphin0520/p/3910667.html

## 1、进程和线程

进程对应计算机里运行的程序，有着独立的内存空间，不同进程之间互不影响。

线程是比进程更小的执行单位，进程对应一个程序，线程就对应这个程序里不同的子任务。一个进程里可以有多个线程，多个线程间共同享有这个进程占有的地址空间和资源。

总结：**进程是操作系统进行资源分配的基本单位，线程是操作系统进行调度的基本单位；进程使操作系统的并发成为可能，线程使进程内的并发成为可能。**

## 2、为什么要多线程？

（1）“多”：多个线程执行不同的子任务，提高CPU的利用率；

（2）“线程”：线程可以看做是轻量级的进程，线程间的切换调度的开销远比进程间的小；

（3）“线程”之间通信比进程之间更简单；

## 3、线程间的调度算法和多线程中的上下文切换

**调度算法**

一般线程的个数要多于CPU核的个数，任意时刻下一个CPU的核只能被一个线程使用，CPU为每个线程分配该线程对应的时间片，当一个线程的时间片用完时，该线程重新处于就绪状态，其他线程开始利用CPU核。由于CPU切换不同的线程的频率很快，导致总体看好像是多个线程在同一时刻都在运行。

**上下文切换**

当前线程在执行完对应的CPU时间片，将CPU给下一个线程使用之前会保存自己的状态，以便下次再切换到这个线程时，可以加载这个线程的状态。线程执行完对应的CPU时间片，下一个线程执行下一个线程对应的CPU时间片的过程就是一次上下文切换。

## 4、用户线程和守护线程

Java线程可以分为两类：用户线程和守护线程。

**用户线程（User）**

运行在前台，执行具体任务的线程，比如main方法所在的主线程。

**守护线程（Daemon）**

运行在后台，为用户线程服务的线程，**当所有用户线程都结束工作时，守护线程会随着JVM一起结束工作**，比如垃圾回收用的守护线程。

通过setDaemon(true) 将线程设置为守护线程。要在调用start()方法之前设置，否则线程启动之后就无法修改了。在守护线程中创建的其他线程默认都是守护线程，但可以修改成用户线程。

## 5、线程状态和优先级

https://tobebetterjavaer.com/thread/thread-state-and-method.html

**线程的状态**

**（1）new**

用new操作符创建一个线程：new Thread(r)，需要给线程分配资源（比如内存）后才能变为runnable状态。

**（2）runnable**

一旦调用了start方线程会进入runnable（就绪）状态，此时线程并没有运行，而是等待CPU临幸自己，当线程进入了CPU给它的时间片后，才真正进入了运行（running）状态。一个线程只能调用start方法一次。

> Java线程的**RUNNABLE**状态其实是包括了传统操作系统线程的**ready**和**running**两个状态的。

**（3）running**

线程的运行状态，此时线程进CPU时间片后，开始疯狂输出（做任务）。

**（4）waiting**

处于 waiting 状态的线程需要被其他线程显式地唤醒，才会进入就绪状态。

调用如下3个方法会使线程进入等待状态：

> - Object.wait()：使当前线程处于等待状态直到另一个线程唤醒它；
> - Thread.join()：等待被合并线程执行完毕就会唤醒，底层调用的是Object实例的wait方法；
> - LockSupport.park()：除非获得调用许可，否则禁用当前线程进行线程调度。唤醒方法是调用 LockSuppert.unpark(Thread)

**（5）timed waiting**

计时等待，比waiting多了个条件，当休眠的时间超过设置的时，线程也会被唤醒，重新进入runnable状态。

> 调用如下方法会使线程进入超时等待状态：
>
> - Thread.sleep(long millis)：使当前线程睡眠指定时间；
> - Object.wait(long timeout)：线程休眠指定时间，等待期间可以通过notify()/notifyAll()唤醒；
> - Thread.join(long millis)：等待当前线程最多执行millis毫秒，如果millis为0，则会一直执行；
> - LockSupport.parkNanos(long nanos)： 除非获得调用许可，否则禁用当前线程进行线程调度指定时间；
> - LockSupport.parkUntil(long deadline)：同上，也是禁止线程进行调度指定时间；

**（6）blocked**

阻塞状态。当线程试图访问一份资源，获得对象锁时，此时对象锁被其他线程所持有的，线程只能进入blocked状态，只有当其他线程释放对象锁，并且线程调度器允许它持有该锁时，才会有blocked状态进入runnable状态。

**（7）dead**

死亡状态。线程进入dead状态有两种情况：

- run方法正常退出而自然消亡；
- 执行run方法时抛出没有捕获的异常而非正常死亡。

![](Java并发编程.assets/thread-state-and-method-18f0d338-1c19-4e18-a0cc-62e97fc39272.png)

**线程的优先级**

- 线程的优先级用数字来表示，优先级在MIN_PRIORITY(1)和MAX_PRIORITY(10)之间，缺省时为NORM_PRIORITY(5)；
- 设置和获得线程对象的优先级：int getPriority(); void setPriority(int newPriority);
- 注意：线程优先级低只是意味着获得调度的概率低，并不是绝对先调用优先级高的线程后调用优先级低的线程。

## 6、同步和异步

同步和异步是一组相对的概念，同步对应串行编程，异步对应并行编程。需要等待结果返回才能继续执行的是同步，不需要等待结果返回就能继续执行的是异步。

这里可以先理解一下通信里的同步和异步的概念。

**串行编程和异步编程**

```java
public static void main(String[] args) {
        {
           TaskA;
        }
        ...

        {
            TaskB;
        }
    }
```

串行编程是程序从上到下按顺序执行，比如上面先执行TaskA，再执行TaskB。串行编程直观和简单，不容易出岔子，缺点是如果TaskA执行的时间过长，TaskB一直要等待TaskA执行完才能执行，效率很低。

异步编程是执行TaskA的过程中，同时再执行TaskB，TaskA和TaskB是同时进行的，有种并行的感觉。相比于串行编程，TaskB无需等待TaskA，效率较高，缺点是容易出错，会出现同步等问题。比如在TaskA里起一个线程池执行一个线程任务，TaskB所在的线程就是主线程。Java里有专门的异步编程框架CompletableFuture，这个后续再详细介绍。

**同步**

当两个或者两个以上的线程访问一份共享的资源，需要某种方法确定同一时刻内资源仅被一个线程占用，达到此目的过程叫同步。

线程同步是在线程互斥的基础上实现的，java实现同步有synchronized关键字、Lock、volatile关键字、线程变量（ThreadLocal）和原子类，这些后续详细介绍。

# 线程的基本信息

Java 中使用 Thread 实例来描述线程。

<img src="Java并发编程.assets/1-5.jpg" style="zoom: 67%;" />

```java
public class Client0 {

    public static void main(String[] args) {
        //线程基本信息
        fun1();
        Thread thread = Thread.currentThread();
        System.out.println(thread.getId());
        System.out.println(thread.getName());
        System.out.println(thread.getState());
        System.out.println(thread.getPriority());
    }

    private static void fun1() {
        int a = 1;
        int b = 1;
        int c = a/b;
    }
}
```

Thread的内部静态枚举类State用于定义Java线程的所有状态。线程创建出来之后就是 NEW 状态，调用 start() 方法后，JVM会将线程交给操作系统去管理，这是状态变成 Runnable。**OS线程区分就绪状态和运行状态，但是在JVM里都是同一种状态RUNNABLE。**
```java
public enum State {
        NEW,
        RUNNABLE,
        BLOCKED,
        WAITING,
        TIMED_WAITING,
        TERMINATED;
    }
```

# 创建线程的方法

创建线程的几种方法都和 Thread 类有关。

- 继承Thread类，重写run方法
- 实现runable接口
- 使用 callable 和 FutureTask
- 创建线程池

一般工程里不允许1、2这种直接new一个线程去做任务，这样的线程叫“野线程”，正规的是用线程池创建工作线程执行任务。

## 继承Thread类

Thread 类内部有一个空方法 run，需要重写。

## 实现Runnable接口

看一下 Thread 类源码：

```java
 package java.lang;
 public class Thread implements Runnable {
 ...
 private Runnable target; //执行目标
 public void run() {
 if(this.target != null) {
 this.target.run(); //调用执行目标的
run()方法
 }
 }
 public Thread(Runnable target) { //包含执行目标的构
造器
 init(null, target, "Thread-" +
nextThreadNum(), 0);
 }
 }
```

如果target（执行目标）不为空，就 执行target属性的run()方法。而target属性是Thread类的一个实例属 性，并且target属性的类型为Runnable。

再看一下Runnable接口的代码：

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}
```

实现 runnable 接口的类只需实现run方法即可，run方法里写具体这个线程要做什么事情，run方法返回为void，无需传入参数，符合函数式接口，宜选用lambda表达式。

### 优点

实现 Runnable 接口的优点除了避免 Thread 单继承的限制，最重要的是实现了逻辑和数据的更好分离，更适合同一个资源被多段业务逻辑并行处理的场景，因为一个 Runnable 实例可以作为多个 Thread 的参数。例如：

```java
package com.learnable.userdomain.common.mq;

import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    private final static int GOODS_NUMBER = 5;//商品总数

    static class SalesThread extends Thread {
        int goodsNumber = GOODS_NUMBER;
        SalesThread(String name) {
            super(name);
        }
        @Override
        public void run() {
            for (int i = 1; i <= GOODS_NUMBER; i ++) {
                if (this.goodsNumber > 0) {
                    System.out.println(this.getName() + "卖出一件，还剩" + this.goodsNumber + "件");
                    goodsNumber --;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    static class SalesImpl implements Runnable {
        AtomicInteger goodsNumber = new AtomicInteger(GOODS_NUMBER);//原子类型

        @Override
        public void run() {
            for (int i = 1; i <= GOODS_NUMBER; i ++) {
                if (this.goodsNumber.get() > 0) {
                    System.out.println(Thread.currentThread().getName() + "卖出一件，还剩" + this.goodsNumber.getAndDecrement() + "件");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 2; i ++) {
            Thread thread;
            thread = new SalesThread("Thread商店" + i);
            thread.start();
        }

        Thread.sleep(1000);
        SalesImpl sales = new SalesImpl();
        for (int i = 0; i < 2; i ++) {
            Thread thread;
            thread = new Thread(sales, "Runnable商店" + i);
            thread.start();
        }
    }
}
//运行结果
Thread商店0卖出一件，还剩5件
Thread商店1卖出一件，还剩5件
Thread商店0卖出一件，还剩4件
Thread商店1卖出一件，还剩4件
Thread商店1卖出一件，还剩3件
Thread商店0卖出一件，还剩3件
Thread商店1卖出一件，还剩2件
Thread商店0卖出一件，还剩2件
Thread商店0卖出一件，还剩1件
Thread商店1卖出一件，还剩1件
Runnable商店0卖出一件，还剩5件
Runnable商店1卖出一件，还剩4件
Runnable商店1卖出一件，还剩3件
Runnable商店0卖出一件，还剩2件
Runnable商店0卖出一件，还剩1件
Runnable商店1卖出一件，还剩0件
```

### **run() 方法和 start() 方法的区别**

run()方法是实现Runnable接口时，要实现的方法，实现的是线程具体要做的事情。

start()方法是Thread类里的方法，线程对象调用start()方法，线程进入就绪状态（runnable），待线程分到CPU的时间片时，由runnable状态进入running状态，进入running状态的线程会执行run方法里写的线程具体执行的任务。

## 使用 callable 和 FutureTask

前面两种方式都没法获取异步执行的结果。java提供的异步框架：callable接口 + Future接口/FutureTask类 + 线程池（ExecutorService接口）解决了这个问题。

先看callable接口：

```java
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

与runnable接口不同的是，支持传入参数，支持返回值，入参和返回值类型（接口里为泛型）相同，且支持抛出异常。

```java
public class MyTask implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("开始执行异步任务");
        int sum = 0;
        for (int i = 0; i < 100; ++i)
        {
            System.out.println("正在执行异步任务, 当前i值为：" + String.valueOf(i));
            sum += i;
        }
        return sum;
    }
}
```

Future 接口有3个作用：

1. 取消异步执行的任务
2. 判断异步任务是否完成
3. 获取异步执行的结果

FutureTask 是 Future 和 Runnable 的实现类，同时 FutureTask 内部持有 Callable 的实例。所以 FutureTask 成为了 Callable 和 Thread 之间的桥梁：

![](Java并发编程.assets/1-8.jpg)

通过FutureTask类和Callable接口的联合使用可以创建能够获取异步执行结果的线程，具体步骤如下：

1. 创建一个Callable接口的实现类，并实现其call()方法。
2. 使用Callable实现类的实例构造一个FutureTask实例。
3. 使用FutureTask实例构建Thread线程实例。

4. 调用Thread实例的start()方法启动线程
5. 调用FutureTask对象的get()方法阻塞性地获得并发线程的执行结果。

```java
public class Client1 {

    public static void main(String[] args) throws Exception {
        FutureTask<String> futureTask = new FutureTask<>(() -> "hello world");
        new Thread(futureTask).start();
        String s = futureTask.get();
        System.out.println(s);
    }
}
```

调用Thread实例的start()方法启动新线程，启动新线程的 run()方法并发执行。其内部的执行过程为：启动Thread实例的run() 方法并发执行后，会执行FutureTask实例的run()方法，最终会并发执行Callable实现类的call()方法。

## 通过线程池创建

Java提供了一个静态工厂类来Executors 来创建线程池。创建固定大小的线程池：

```java
ExecutorService threadPool = Executors.newFixedThreadPool(3);
```

ExecutorService 实例负责对池中的线程进行管理和调度。向 ExecutorService 提交任务的常用方法是：

```java
//方法一：执行一个 Runnable类型的target执行目标实例，无返回
     void execute(Runnable command);
//方法二：提交一个 Callable类型的target执行目标实例, 返回一个Future异步任务实例
     <T> Future<T> submit(Callable<T> task);  
//方法三：提交一个 Runnable类型的target执行目标实例, 返回一个Future异步任务实例。这种用法主要是使用 Future 对象控制线程的执行，比如取消线程。
     Future<?> submit(Runnable task);
```

举个给线程池递交Runnable的任务的例子：

```java
public class ConcurrentOneMain {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() ->
        {
            System.out.println("线程池1开始执行异步任务");
            for (int i = 0; i < 100; ++i)
            {
                System.out.println("线程池1执行异步任务" + String.valueOf(i));
            }
        });

        executorService.execute(() ->
        {
            System.out.println("线程池2开始执行异步任务");
            for (int i = 0; i < 100; ++i)
            {
                System.out.println("线程池2执行异步任务" + String.valueOf(i));
            }
        });
        executorService.shutdown();

        System.out.println("主线程开始执行任务");
        for (int i = 0; i < 100; ++i)
        {
            System.out.println("主线程执行任务" + String.valueOf(i));
        }
    }
}
```

首先创建一个线程池，然后往线程池里提交任务（execute方法），一个线程池里可以提交多个任务，任务是实现了Runnable接口的类的对象，最后shutdown线程池。后面会详细介绍线程池。

举个递交 Callable 任务的例子：

```java
public class ConcurrentOneMain {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        MyTask myTask = new MyTask();
        Future<Integer> result = executorService.submit(myTask);
        executorService.shutdown();

        System.out.println("开始执行主线程任务");
        int sum = 0;
        for (int i = 0; i < 100; ++i)
        {
            System.out.println("正在执行主线程任务，当前i值为" + String.valueOf(i));
            sum +=i;
        }
        System.out.println("主线程执行完毕，sum的值为: " + String.valueOf(sum));

        // 获得异步任务的结果
        try
        {
            System.out.println("异步线程执行完毕，sum的值为: " + result.get());
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        catch(ExecutionException e)
        {
            e.printStackTrace();
        }
    }
}
```

实际项目中禁止使用 Executors 创建线程池。

# 线程的基本操作

## 中断

Java语言提供了stop()方法终止正在运行的线程，但是Java将Thread的stop()方法设置为过时，不建议大家使用。在程序中，我们是不能随便中断一个线程的，我们无法知道这个线程正运行在什么状态，它可能持有某把锁，强行中断线程可能导致锁不能释放的问题；或者线程可能在操作数据库，强行中断线程可能导致数据不一致的问题。正是由于调用stop()方法来终止线程可能会产生不可预料的结果，因此不推荐调用stop()方法。

一个线程什么时候可以退出呢？当然只有线程自己才能知道。所以，这里介绍一下Thread的interrupt()方法，此方法本质不是用来中断一个线程，而是将线程设置为中断状态。

当我们调用线程的interrupt()方法时，它有两个作用：

（1）如果此线程处于阻塞状态（如调用了Object.wait()方法），就会立马退出阻塞，并抛出InterruptedException异常，线程就可以通过捕获InterruptedException来做一定的处理，然后让线程退出。更确切地说，如果线程被Object.wait()、Thread.join()和Thread.sleep()三种方法之一阻塞，此时调用该线程的interrupt()方法，该线程将抛出一个InterruptedException中断异常（该线程必须事先预备好处理此异常），从而提早终结被阻塞状态。

（2）如果此线程正处于运行之中，线程就不受任何影响，继续运行，仅仅是线程的中断标记被设置为true。所以，程序可以在适当的位置通过调用isInterrupted()方法来查看自己是否被中断，并执行退出操作。

## join

线程的合并是一个比较难以说清楚的概念，什么是线程的合并呢？举一个例子，假设有两个线程A和B。现在线程A在执行过程中对另一个线程B的执行有依赖，具体的依赖为：线程A需要将线程B的执行流程合并到自己的执行流程中（至少表面如此），这就是线程合并，被动方线程B可以叫作被合并线程。这个例子中的线程A合并线程B的伪代码大致为：

```java
 class ThreadA extends Thread
 {
     void run()
     {
         Thread threadb = new Thread("thread-b");
         threadb.join();
     }
 }
```

join()方法是Thread类的一个实例方法，有三个重载版本：

1. 把当前线程变为TIMED_WAITING，直到被合并线程执行结束
2. 把当前线程变为TIMED_WAITING，直到被合并线程执行结束，或者等待被合并线程执行millis的时间
3. 把当前线程变为TIMED_WAITING，直到被合并线程执行结束，或者等待被合并线程执行millis+nanos的时间

## yield

yield() 是Thread 提供的静态方法，作用是使当前线程让出cpu的执行权限。

1. yield仅能使一个线程从运行状态转到就绪状态，而不是阻塞状态。
2. yield不能保证使得当前正在运行的线程迅速转换到就绪状态。
3. 即使完成了迅速切换，OS 下次线程调度时仍然可能选中此线程



# 线程池实战

## JUC的架构

JUC（java.util.concurrent）中线程池相关的架构图：

![](Java并发编程.assets/1-15.jpg)

Executor 接口只有一个 execute() 方法，可以接口一个 Runnable实例。

ExecutorService 接口有 execute() 方法和 submit() 方法。可以执行 Runnable 和 Callable任务。

ThreadPoolExecutor就是大名鼎鼎的“线程池”实现类。可以创建自定义的线程池。

ScheduledThreadPoolExecutor提供了“延时执行”和“周期执行”等方法。

Executors是一个静态工厂类，提供了一些快捷的创建线程池的方法。

## Executors 工厂类 here

Executors工厂类提供了4种快捷创建线程池的方法：

1. newSingleThreadExecutor()：创建只有一个线程的线程池
2. newFixedThreadPool(int)：创建固定大小的线程池
3. newCachedThreadPool()：创建不限数量的线程池，任何提交的任务都将立即执行，但是空闲线程会得到及时回收
4. newScheduledThreadPool()：创建可定时执行任务的线程池

```java
public class Client13 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建有3个线程的线程池
        ExecutorService es = Executors.newFixedThreadPool(3);
        //提交Callable获取Future对象
        for (int i = 0; i < 10; i ++) {
            Future<String> future = es.submit(new Task13());
            //从Future获取异步执行的结构
            System.out.println(future.get()); //可能阻塞

            //future.get(1, TimeUnit.SECONDS); //等待时间后获取执行结果
            //while (true) {
            //    if (future.isDone()) { //判断任务是否完成
            //        String s = future.get();
            //    }
            //}
        }
        //关闭线程池
        es.shutdown(); 
    }
}
//Callable接口比Runnable接口多个返回值
//Callable实例不能作为参数传入Thread的构造器
class Task13 implements Callable<String> {
    @Override
    public String call() throws Exception {
        try {
            System.out.println(Thread.currentThread().getName() + "running");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        return "hello";
    }
}
```

## 创建线程池的标准方法

```java
// 使用标准构造器构造一个普通的线程池
     public ThreadPoolExecutor(
       int corePoolSize,            // 核心线程数，即使线程空闲（Idle），也不会回收
       int maximumPoolSize,                 // 线程数的上限
       long keepAliveTime, TimeUnit unit,   // 线程最大空闲（Idle）时长 
       BlockingQueue<Runnable> workQueue,     // 任务的排队队列
       ThreadFactory threadFactory,                         // 新线程的产生方式
       RejectedExecutionHandler handler)    // 拒绝策略
```

1. 核心和最大线程数量：

线程池执行器将会根据corePoolSize和maximumPoolSize自动维护线程池中的工作线程。

2. 阻塞队列：

如果核心线程都很忙，新收到的异步任务会暂存起来。

3. 最大空闲时长：

keepAliveTime 用于设置池内线程最大Idle（空闲）时长（或者说保活时长），如果超过这个时间，默认情况下Idle、非Core线程会被回收。

如果池在使用过程中提交任务的频率变高，也可以调用方法setKeepAliveTime(long，TimeUnit)进行线程存活时间的动态调整，可以将时长延长。如果需要防止Idle线程被终止，可以将Idle时间设置为无限大，具体如下：

     setKeepAliveTime(Long.MAX_VALUE，TimeUnit.NANOSECONDS);

默认情况下，Idle超时策略仅适用于存在超过corePoolSize线程的情况。但若调用了allowCoreThreadTimeOut(true)，则keepAliveTime参数所设置的Idle超时策略也将被应用于核心线程。

### 线程池的任务调度流程

![](Java并发编程.assets/1-16.jpg)

线程池的任务调度流程（包含接收新任务和执行下一个任务）大致如下：

（1）如果当前工作线程数量小于核心线程数量，执行器总是优先创建一个任务线程，而不是从线程队列中获取一个空闲线程。

（2）如果线程池中总的任务数量大于核心线程池数量，新接收的任务将被加入阻塞队列中，一直到阻塞队列已满。在核心线程池数量已经用完、阻塞队列没有满的场景下，线程池不会为新任务创建一个新线程。

（3）当完成一个任务的执行时，执行器总是优先从阻塞队列中获取下一个任务，并开始执行，一直到阻塞队列为空，其中所有的缓存任务被取光。

（4）在核心线程池数量已经用完、阻塞队列也已经满了的场景下，如果线程池接收到新的任务，将会为新任务创建一个线程（非核心线程），并且立即开始执行新任务。

（5）在核心线程都用完、阻塞队列已满的情况下，一直会创建新线程去执行新任务，直到池内的线程总数超出maximumPoolSize。如果线程池的线程总数超过maximumPoolSize，线程池就会拒绝接收任务，当新任务过来时，会为新任务执行拒绝策略。

```java
public class Client20 {

    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 100, 100, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100));

        for(int i = 0; i < 5; i ++) {
            executor.execute(() -> {
                Thread thread = Thread.currentThread();
                System.out.println(thread.getName());
                try {
                    //无限睡眠
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        while(true) {
            //每秒，输出线程池的线程数
            System.out.println("活跃的线程数: " + executor.getActiveCount() + " 任务数量: " + executor.getTaskCount());
            Thread.sleep(1000);
        }
    }
}
```

代码运行结果：

```
活跃的线程数: 1 任务数量: 5
pool-1-thread-1
活跃的线程数: 1 任务数量: 5
活跃的线程数: 1 任务数量: 5
活跃的线程数: 1 任务数量: 5
活跃的线程数: 1 任务数量: 5
......
```

以上示例创建了最大线程数量maximumPoolSize为100的线程池，仅仅向其中提交了5个任务。理论上，这5个任务都会被执行到，奇怪的是示例中只有1个任务在执行，其他的4个任务都在等待。其他任务被加入到了阻塞队列中，需要等pool-1-thread-1线程执行完第一个任务后，才能依次从阻塞队列取出执行。但是，实例中的第一个任务是一个永远也没有办法完成的任务，所以其他的4个任务只能永远在阻塞队列中等待着。由于参数配置得不合理，因此出现了以上的奇怪现象。

为什么会出现上面的奇怪现象呢？因为例子中的corePoolSize为1，阻塞队列的大小为100，按照线程创建的规则，需要等阻塞队列已满，才会去创建新的线程。例子中加入了5个任务，阻塞队列大小为4（<100），所以线程池的调度器不会去创建新的线程，后面的4个任务只能等待。
