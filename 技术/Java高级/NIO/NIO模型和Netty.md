## 通信和IO模型

> [NIO从入门到踹门 (qq.com)](https://mp.weixin.qq.com/s/GfV9w2B0mbT7PmeBS45xLw)

![](NIO模型和Netty.assets/Snipaste_2023-05-06_11-43-19.png)

传统服务器开发：

方式一：单线程一次处理一个socket、blockIO

方式二：多线程一个线程处理一个socket、blockIO

方式三：创建线程池，一个线程处理一个socket、blockIO

**同步异步是通信模式，阻塞、非阻塞是线程处理模式。**

![](NIO模型和Netty.assets/Snipaste_2023-05-06_15-53-13.png)

简单来说，可以将同步与异步看成 发起IO请求的两种方式。同步IO是指用户空间（进程或者线程）是主 动发起IO请求的一方，系统内核是被动接收方。异步IO则反过来，系 统内核是主动发起IO请求的一方，用户空间是被动接收方。

阻塞和非阻塞的区别是什么呢？阻塞是指用户进程（或者线程） 一直在等待，而不能做别的事情；非阻塞是指用户进程（或者线程） 获得内核返回的状态值就返回自己的空间，可以去做别的事情。

阻塞式IO：只有一个线程处理 socket，处理完了才能接收下一个连接请求。

![](NIO模型和Netty.assets/Snipaste_2023-05-06_15-58-17.png)

非阻塞IO：创建新线程去执行任务，原来的线程可以继续干其他事情。第一个阶段不是阻塞的，但是第二阶段需要不断轮询 kernel 数据好了没有，依然是阻塞的。

![](NIO模型和Netty.assets/Snipaste_2023-05-06_15-59-37.png)

IO 多路复用：也称事件驱动IO，就是一个线程同时监控多个 socket，通过 select 和 poll 轮询负责的所有 socket，某个 socket 有数据到达了，就通知用户线程。IO 复用本质和非阻塞 IO 相同，只是由内核来负责轮询。看似比非阻塞 IO 还多了一个系统调用的开销，但是因为支持多路 IO，总体提高了效率。

epoll 目前代理了 select + poll 的方式：内核与用户空间共享一块内存，通过回调解决遍历的问题

![](NIO模型和Netty.assets/Snipaste_2023-05-06_16-00-10.png)

## BIO、NIO、AIO

IO 是 Java 中比较重要，且比较难的知识点，主要是因为随着 Java 的发展，目前有三种 IO 共存。分别是 BIO、NIO 和 AIO。

Block-IO 是一种**同步且阻塞**的通信模式。模式简单，使用方便。但并发处理能力低，耗时长。

Non-Block IO ，是一种**非阻塞同步**的通信模式。NIO 与原来的 I/O 有同样的作用和目的, 他们之间最重要的区别是数据打包和传输的方式。原来的 I/O 以流的方式处理数据，而 NIO 以块的方式处理数据。

面向流的 I/O 系统一次一个字节地处理数据。一个输入流产生一个字节的数据，一个输出流消费一个字节的数据。面向块的 I/O 系统以块的形式处理数据。每一个操作都在一步中产生或者消费一个数据块。按块处理数据比按(流式的)字节处理数据要快得多。但是面向块的 I/O 缺少一些面向流的 I/O 所具有的优雅性和简单性。

注意：Java NIO 不是NIO模型。IO模型包括：阻塞IO（BIO）、非阻塞IO（NIO）、多路复用IO、信号量、异步IO，**Java NIO 是基于Reactor模式的，Reactor模式是多路复用IO不是NIO模型**。

Java AIO，全程 Asynchronous IO，是**异步非阻塞**的 IO。是一种非阻塞异步的通信模式。在 NIO 的基础上引入了新的异步通道的概念，并提供了异步文件通道和异步套接字通道的实现。

### 三种 IO 的区别

首先，我们站在宏观的角度，重新画一下重点：

**BIO （Blocking I/O）：同步阻塞 I/O 模式。**

**NIO （New I/O）：同步非阻塞模式。**

**AIO （Asynchronous I/O）：异步非阻塞 I/O 模型。**

同步阻塞模式：这种模式下，我们的工作模式是先来到厨房，开始烧水，并坐在水壶面前一直等着水烧开。

同步非阻塞模式：这种模式下，我们的工作模式是先来到厨房，开始烧水，但是我们不一直坐在水壶前面等，而是回到客厅看电视，然后每隔几分钟到厨房看一下水有没有烧开。

异步非阻塞 I/O 模型：这种模式下，我们的工作模式是先来到厨房，开始烧水，我们不一直坐在水壶前面等，也不隔一段时间去看一下，而是在客厅看电视，水壶上面有个开关，水烧开之后他会通知我。

### 适用场景

BIO 方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，并发局限于应用中，JDK1.4 以前的唯一选择，但程序直观简单易理解。

NIO 方式适用于连接数目多且连接比较短（轻操作）的架构，比如聊天服务器，并发局限于应用中，编程比较复杂，JDK1.4 开始支持。

AIO 方式适用于连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用 OS 参与并发操作，编程比较复杂，JDK7 开始支持。

### 使用方式

BIO：

```java
//Initializes The Object
User1 user = new User1();
user.setName("wanger");
user.setAge(23);
System.out.println(user);

//Write Obj to File
ObjectOutputStream oos = null;
try {
    oos = new ObjectOutputStream(new FileOutputStream("tempFile"));
    oos.writeObject(user);
} catch (IOException e) {
    e.printStackTrace();
} finally {
    IOUtils.closeQuietly(oos);
}

//Read Obj from File
File file = new File("tempFile");
ObjectInputStream ois = null;
try {
    ois = new ObjectInputStream(new FileInputStream(file));
    User1 newUser = (User1) ois.readObject();
    System.out.println(newUser);
} catch (IOException e) {
    e.printStackTrace();
} catch (ClassNotFoundException e) {
    e.printStackTrace();
} finally {
    IOUtils.closeQuietly(ois);
    try {
        FileUtils.forceDelete(file);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

```

NIO：

```java
static void readNIO() {
    String pathname = "C:\\Users\\adew\\Desktop\\jd-gui.cfg";
    FileInputStream fin = null;
    try {
        fin = new FileInputStream(new File(pathname));
        FileChannel channel = fin.getChannel();

        int capacity = 100;// 字节
        ByteBuffer bf = ByteBuffer.allocate(capacity);
        int length = -1;

        while ((length = channel.read(bf)) != -1) {

            bf.clear();
            byte[] bytes = bf.array();
            System.out.write(bytes, 0, length);
            System.out.println();
        }

        channel.close();

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (fin != null) {
            try {
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

static void writeNIO() {
    String filename = "out.txt";
    FileOutputStream fos = null;
    try {

        fos = new FileOutputStream(new File(filename));
        FileChannel channel = fos.getChannel();
        ByteBuffer src = Charset.forName("utf8").encode("你好你好你好你好你好");
        int length = 0;

        while ((length = channel.write(src)) != 0) {
            System.out.println("写入长度:" + length);
        }

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

```

AIO：

```java
public class ReadFromFile {
  public static void main(String[] args) throws Exception {
    Path file = Paths.get("/usr/a.txt");
    AsynchronousFileChannel channel = AsynchronousFileChannel.open(file);

    ByteBuffer buffer = ByteBuffer.allocate(100_000);
    Future<Integer> result = channel.read(buffer, 0);

    while (!result.isDone()) {
      ProfitCalculator.calculateTax();
    }
    Integer bytesRead = result.get();
    System.out.println("Bytes read [" + bytesRead + "]");
  }
}
class ProfitCalculator {
  public ProfitCalculator() {
  }
  public static void calculateTax() {
  }
}

public class WriteToFile {

  public static void main(String[] args) throws Exception {
    AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
        Paths.get("/asynchronous.txt"), StandardOpenOption.READ,
        StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    CompletionHandler<Integer, Object> handler = new CompletionHandler<Integer, Object>() {

      @Override
      public void completed(Integer result, Object attachment) {
        System.out.println("Attachment: " + attachment + " " + result
            + " bytes written");
        System.out.println("CompletionHandler Thread ID: "
            + Thread.currentThread().getId());
      }

      @Override
      public void failed(Throwable e, Object attachment) {
        System.err.println("Attachment: " + attachment + " failed with:");
        e.printStackTrace();
      }
    };

    System.out.println("Main Thread ID: " + Thread.currentThread().getId());
    fileChannel.write(ByteBuffer.wrap("Sample".getBytes()), 0, "First Write",
        handler);
    fileChannel.write(ByteBuffer.wrap("Box".getBytes()), 0, "Second Write",
        handler);

  }
}
```



## Netty

> [超详细Netty入门，看这篇就够了！ - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/181239748)

网络应用开发框架，支持几乎所有应用层协议

1. 异步
2. 事件驱动
3. 基于NIO