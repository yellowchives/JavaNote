# 基本概念

https://www.cnblogs.com/dolphin0520/p/3910667.html

## 1、进程和线程

进程对应计算机里运行的程序，有着独立的内存空间，不同进程之间互不影响。

线程是比进程更小的执行单位，进程对应一个程序，线程就对应这个程序里不同的子任务。一个进程里可以有多个线程，多个线程间共同享有这个进程占有的地址空间和资源。

总结：**进程是操作系统进行资源分配的基本单位，线程是操作系统进行调度的基本单位；进程使操作系统的并发成为可能，线程使进程内的并发成为可能。**

## 2、为什么要多线程？

（1）“多”：多个线程执行不同的子任务，提高CPU的利用率；

（2）“线程”：线程可以看做是轻量级的进程，线程间的切换调度的开销远比进程间的小；

（3）“线程”之间共享数据比进程之间更简单；

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

运行在后台，为用户线程服务的线程，当所有用户线程都结束工作时，守护线程会随着JVM一起结束工作，比如垃圾回收用的守护线程。

通过setDaemon(true)将线程设置为守护线程。

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

当线程等待另一个线程通知调度器一个条件时，该线程进入等待状态（waiting），当线程得到调度器通知可以被唤醒时，线程会重新进入runnable状态。

调用如下3个方法会使线程进入等待状态：

> - Object.wait()：使当前线程处于等待状态直到另一个线程唤醒它；
> - Thread.join()：等待线程执行完毕，底层调用的是Object实例的wait方法；
> - LockSupport.park()：除非获得调用许可，否则禁用当前线程进行线程调度。

**（5）time waiting**

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

同步和异步是一组相对的概念，同步对应串行编程，异步对应并行编程。

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

# 创建线程的方法

java中创建线程，主要有3种方法：

- 继承Thread类，重写run方法
- 实现runable接口
- 实现callable接口

一般工程里不允许1、2这种直接new一个线程去做任务，这样的线程叫“野线程”，正规的是用线程池创建工作线程执行任务。

## 1、实现Runnable接口

首先看一下Runnable接口的代码：

```java
public interface Runnable {
    public abstract void run();
}
```

实现runnable接口的类只需实现run方法即可，run方法里写具体这个线程要做什么事情，run方法返回为void，无需传入参数，符合函数式接口，宜选用lambda表达式。

实现runnable接口，创建一个匿名内部类，把匿名内部类的对象作为Thread类的初始化参数，基本都用lambda表达式代替匿名内部类。

```java
new Thread(() ->
        {
            for (int i=0; i < 100; ++i)
            {
                System.out.println(i);
            }
        }).start();
```

## 2、继承Thread类

创建一个Thread类的子类，覆写run方法，在run方法里实现要完成的任务。

```java
public class MyThread extends Thread{
    private int num;

    MyThread(int num)
    {
        this.num = num;
    }

    @Override
    public void run() {
        for(int i = 0; i < 100; ++i)
        {
            this.num +=1;
        }
        System.out.println(this.num);
    }
}
```



```java
public static void main(String[] args) {
        int num = 5;
        
        MyThread myThread = new MyThread(num);
        myThread.start();
    }
```

### **run（）方法和start（）方法的区别**

run()方法是实现Runnable接口时，要实现的方法，实现的是线程具体要做的事情。

start()方法是Thread类里的方法，线程对象调用start()方法，线程进入就绪状态（runnable），待线程分到CPU的时间片时，由runnable状态进入running状态，进入running状态的线程会执行run方法里写的线程具体执行的任务。

举一个例子：

```java
public class MyThread extends Thread{
    @Override
    public void run() {
        System.out.println("ping");
    }
}
```

```java
public class ConcurrentOneMain {
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.run();
        System.out.println("pong");
    }
}
```

运行结果是：

```text
ping
pong
```

main方法里调用的是run方法，这里并没有启动线程mythread，run方法就跟普通的方法一样，程序串行执行，先打印run方法里的“ping”,再在打印“pong”。

如果把run()方法换成start()方法，意味着启动了线程mythread，程序会异步执行，先打印“ping”还是“pong"取决于mythread线程和main线程哪个先获得CPU的时间片，结果是不确定的。

**真正启动线程的是start（）方法，线程进入running状态后运行run()方法。**

## 3、实现Callable接口

这个方法确切的说是java提供的异步框架：callable接口 + Future接口/FutureTask类 + 线程池（ExecutorService接口），先看callable接口：

```java
public interface Callable<V> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    V call() throws Exception;
}
```

跟runnable接口相同的是，里面仅有一个call方法需要实现。

与runnable接口不同的是，支持传入参数，支持返回值，入参和返回值类型（接口里为泛型）相同，且支持抛出异常；加入线程池运行，runnable使用ExecutorService的execute方法，Callable使用submit方法。

举个例子如何使用：

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

有关java异步框架后面单独讲。

## 4、创建线程池

工程中其实并没有像1、2里介绍的直接new Thread来创建线程，这样创建出来的线程是“野线程”，不方便管理，且创建一个线程是要消耗不少资源的，实际工程中创建线程都是通过线程池创建的。3中已经展示了线程池的使用，给线程池递交的是Callable异步任务，下面举个给线程池递交Runnable的任务的例子：

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