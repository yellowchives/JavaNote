## 2.第二章JUL

### 2.1 JUL简介

​	JUC全程Java Utll Logging,它是java原生的日志框架使用时不需要另外引用第三方的类库、相对其他的日志框架使用方便，学习简单，主要是使用在小型应用中。

### 2.2 JUL组件的介绍

**Logger**:被称为记录器，应用程序通过获取Logger对象，抵用其API来发布日志信息。Logger通常被认为是访问日志系统的入口程序。

**Handler**:处理器每个Logger都会关联一个或者是一组Handler，Logger会讲日志交给关联的Handler去做处理，由Handler负责讲日志做记录，Hander具体是实现了日志的输出位置，比如可以输出到控制它或者是文件中等等。

**Filter**：过滤器，根据需要定制哪些信息会被记录，哪些信息会被忽略过。

**Formatter：**格式化组件，它扶着对日志中的数据和信息进行转换和格式化吗所以它决定了我们输出日志最终的形式。

**Level:**日志的输出级别，每条日志消息都一个关联的级别，我们根据输出级别的设置，用来展现组中所呈现的日志信息，根据不同的需求，去设置不同的级别



### 2.3实战案例

```java
package com.xxx;

import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JULTest {
  
  private static Logger logger = Logger.getLogger("com.xxx.JULTest");
    @Test
    public void test01() {   

        /* 对于日志的输出方式有两种
         *
         * 第一种方式直接调用对应级别的相关方法，方法中传递日志输出信息
         * 假设我们现在要info级别的日志
         *
         *
         * 第二种方法，
         * 调用log方法,然后在level来定义日志的级别
         * */
        logger.info("info");
        logger.log(Level.INFO, "输出info的信息");

        String name = "haoran";
        int age = 1;
        logger.log(Level.INFO, "name={0},age={1}", new Object[]{name, age});
    }
}

```



以上所有的配置相关的操作，都是以java硬编码的形式进行的我们可以是用更加清晰，更加专业的一种做法，就是使用配置文件，如果我们没有自己添加配置文件，则会使用系统默认的配置文件，这个配置文件

Owner.readPrimordialConfigration();

Java.home-->找到jre文件夹-->lib-->logging.properties

```java
 @Test
    public void test02() {

        /*
         * SEVERE:(最高级)错误级别
         * WARNING:警告级别
         * INFO:(默认级别)消息级别
         * CONFIG:配置级别
         * FINE: 详细信息（少）
         * FINER: 详细信息（中）
         * FINEST: 详细信息 (高)
         *
         * 特殊的两个级别
         * OFF 关闭所有的日志消息记录
         * ON 启动所有的日志消息记录
         *
         *
         * */


//        logger.setLevel(Level.ALL);
        logger.log(Level.SEVERE, "SEVERE");
        logger.log(Level.WARNING, "WARNING");
        logger.log(Level.INFO, "INFO");
        logger.log(Level.CONFIG, "CONFIG");
        logger.log(Level.FINE, "FINE");
        logger.log(Level.FINER, "FINER");
        logger.log(Level.FINEST, "FINEST");


    }
```



```java
@Test
    public void test03() {
        //日志记录器
        Logger logger = Logger.getLogger("com.xxx.JULTest");
        //讲默认的日志级别关闭掉
        logger.setUseParentHandlers(false);
        //处理器hander
        ConsoleHandler consoleHandler = new ConsoleHandler();
        //创建格式化器
        SimpleFormatter simpleFormatter = new SimpleFormatter();

        //在处理器中设置输出格式
        consoleHandler.setFormatter(simpleFormatter);

        //在记录器中添加处理器
        logger.addHandler(consoleHandler);

        //日志的打印级别
        //此处需要将日志记录器和处理器的级别进行统一的设置，才会达到日志显示相应的级别效果
        logger.setLevel(Level.ALL);


        logger.log(Level.SEVERE, "SEVERE");
        logger.log(Level.WARNING, "WARNING");
        logger.log(Level.INFO, "INFO");
        logger.log(Level.CONFIG, "CONFIG");
        logger.log(Level.FINE, "FINE");
        logger.log(Level.FINER, "FINER");
        logger.log(Level.FINEST, "FINEST");

    }
```



```java
 @Test
    public void test04() {

        //日志记录器
        Logger logger = Logger.getLogger("com.xxx.JULTest");
        logger.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        handler.setFormatter(simpleFormatter);
        handler.setLevel(Level.ALL);
        //日志处理器
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
        logger.log(Level.SEVERE, "SEVERE");
        logger.log(Level.WARNING, "WARNING");
        logger.log(Level.INFO, "INFO");
        logger.log(Level.CONFIG, "CONFIG");
        logger.log(Level.FINE, "FINE");
        logger.log(Level.FINER, "FINER");
        logger.log(Level.FINEST, "FINEST");

    }
```

```java
@Test
    public void test05() throws IOException, InterruptedException {
        /*
         * 讲日志输出到具体的磁盘文件中
         * 日志的持久化操作
         * */
        Logger logger = Logger.getLogger("com.xxx.JULTest");
        FileHandler fileHandler = new FileHandler("/Users/ran/Desktop/plan/haoran/log_learn/log/a.log");
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);
        logger.setUseParentHandlers(false);
        fileHandler.setLevel(Level.ALL);
        logger.addHandler(fileHandler);
        logger.setLevel(Level.ALL);

        for (int i = 0; i < 100; i++) {
            Thread.sleep(500);
            logger.log(Level.SEVERE, "SEVERE");
            logger.log(Level.WARNING, "WARNING");
            logger.log(Level.INFO, "INFO");
            logger.log(Level.CONFIG, "CONFIG");
            logger.log(Level.FINE, "FINE");
            logger.log(Level.FINER, "FINER");
            logger.log(Level.FINEST, "FINEST");
        }

    }
```

```java
@Test
    public void test06() throws IOException, InterruptedException {


        Logger logger1 = Logger.getLogger("com.xxx.JULTest");


        ConsoleHandler consoleHandler = new ConsoleHandler();
        FileHandler fileHandler = new FileHandler("/Users/ran/Desktop/plan/haoran/log_learn/log/a.log");
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        consoleHandler.setFormatter(formatter);
        consoleHandler.setLevel(Level.ALL);
        fileHandler.setLevel(Level.ALL);

        logger1.setUseParentHandlers(false);
        logger1.addHandler(fileHandler);
        logger1.addHandler(consoleHandler);
        logger1.setLevel(Level.ALL);


        for (int i = 0; i < 100; i++) {
            Thread.sleep(500);
            logger.log(Level.SEVERE, "SEVERE");
            logger.log(Level.WARNING, "WARNING");
            logger.log(Level.INFO, "INFO");
            logger.log(Level.CONFIG, "CONFIG");
            logger.log(Level.FINE, "FINE");
            logger.log(Level.FINER, "FINER");
            logger.log(Level.FINEST, "FINEST");
        }


    }
```

```java
@Test
    public void test07() throws IOException, InterruptedException {


        Logger logger1 = Logger.getLogger("com.xxx.JULTest");


        ConsoleHandler consoleHandler = new ConsoleHandler();
        FileHandler fileHandler = new FileHandler("/Users/ran/Desktop/plan/haoran/log_learn/log/a.log");
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        consoleHandler.setFormatter(formatter);
        consoleHandler.setLevel(Level.ALL);
        fileHandler.setLevel(Level.ALL);

        logger1.setUseParentHandlers(false);
        logger1.addHandler(fileHandler);
        logger1.addHandler(consoleHandler);
        logger1.setLevel(Level.ALL);


        Logger logger2 = Logger.getLogger("com.xxx.JULTest.test07");


        logger2.log(Level.SEVERE, "SEVERE");
        logger2.log(Level.WARNING, "WARNING");
        logger2.log(Level.INFO, "INFO");
        logger2.log(Level.CONFIG, "CONFIG");
        logger2.log(Level.FINE, "FINE");
        logger2.log(Level.FINER, "FINER");
        logger2.log(Level.FINEST, "FINEST");

    }
```

```java
@Test
    public void test08() throws IOException {

        FileInputStream fis = new FileInputStream("/Users/ran/Desktop/plan/haoran/log_learn/lib/log4j.properties");
        LogManager manager = LogManager.getLogManager();
        manager.readConfiguration(fis);

        Logger logger2 = Logger.getLogger("com.xxx.JULTest.test07");

        logger2.log(Level.SEVERE, "SEVERE");
        logger2.log(Level.WARNING, "WARNING");
        logger2.log(Level.INFO, "INFO");
        logger2.log(Level.CONFIG, "CONFIG");
        logger2.log(Level.FINE, "FINE");
        logger2.log(Level.FINER, "FINER");
        logger2.log(Level.FINEST, "FINEST");


    }
```

```java
 @Test
    public void test09() throws IOException {

        FileInputStream fis = new FileInputStream("/Users/ran/Desktop/plan/haoran/log_learn/lib/log4j.properties");
        LogManager manager = LogManager.getLogManager();
        manager.readConfiguration(fis);

        Logger logger2 = Logger.getLogger("com.xxx.JULTest");

        logger2.log(Level.SEVERE, "SEVERE");
        logger2.log(Level.WARNING, "WARNING");
        logger2.log(Level.INFO, "INFO");
        logger2.log(Level.CONFIG, "CONFIG");
        logger2.log(Level.FINE, "FINE");
        logger2.log(Level.FINER, "FINER");
        logger2.log(Level.FINEST, "FINEST");


    }
```

```java

    public static void main(String[] args) throws IOException {


        Logger logger = Logger.getLogger("com.xxx.Hello");
        InputStream fis = Hello.class.getClassLoader().getResourceAsStream("log4j.properties");

        LogManager manager = LogManager.getLogManager();
        manager.readConfiguration(fis);


        logger.log(Level.SEVERE, "SEVERE");
        logger.log(Level.WARNING, "WARNING");
        logger.log(Level.INFO, "INFO");
        logger.log(Level.CONFIG, "CONFIG");
        logger.log(Level.FINE, "FINE");
        logger.log(Level.FINER, "FINER");
        logger.log(Level.FINEST, "FINEST");

    }
```

```xml
############################################################
#  	Default Logging Configuration File -- 默认的日志配置文件
#
# You can use a different file by specifying a filename 你可以使用不同的文件去指定一个文件名称
# with the java.util.logging.config.file system property.
# For example java -Djava.util.logging.config.file=myfile
############################################################
############################################################
#  	Global properties 全局配置
############################################################
# "handlers" specifies a comma separated list of log Handler
# classes.  These handlers will be installed during VM startup.
# Note that these classes must be on the system classpath.
# By default we only configure a ConsoleHandler, which will only
# show messages at the INFO and above levels.
handlers=java.util.logging.FileHandler
# To also add the FileHandler, use the following line instead.
#handlers= java.util.logging.FileHandler, java.util.logging.ConsoleHandler
# Default global logging level.
# This specifies which kinds of events are logged across
# all loggers.  For any given facility this global level
# can be overriden by a facility specific level
# Note that the ConsoleHandler also has a separate level
# setting to limit messages printed to the console.
.level=INFO
############################################################
# Facility specific properties.
# Provides extra control for each logger.
############################################################
# For example, set the com.xyz.foo logger to only log SEVERE
# messages:
com.xxx.level=ALL
com.xxx.handlers=java.util.logging.ConsoleHandler
com.xxx.useParentHandlers=false
############################################################
# Handler specific properties.
# Describes specific configuration info for Handlers.
############################################################
# default file output is in user's home directory.
java.util.logging.FileHandler.pattern=%h/java%u.log
java.util.logging.FileHandler.limit=50000
java.util.logging.FileHandler.count=1
java.util.logging.FileHandler.formatter=java.util.logging.XMLFormatter
java.util.logging.FileHandler.append=true
# Limit the message that are printed on the console to INFO and above.
java.util.logging.ConsoleHandler.level=ALL
#java.util.logging.ConsoleHandler.formatter=java.util.logging.SimpleFormatter
java.util.logging.ConsoleHandler.formatter=java.util.logging.SimpleFormatter
# Example to customize the SimpleFormatter output format
# to print one-line log message like this:
#     <level>: <log message> [<date/time>]
#
# java.util.logging.SimpleFormatter.format=%4$s: %5$s [%1$tc]%n

```

- java.util.logging.FileHandler.append 指定是否应该将 FileHandler 追加到任何现有文件上（默认为 false）。

- java.util.logging.FileHandler.pattern 为生成的输出文件名称指定一个模式。有关细节请参见以下内容（默认为 "%h/java%u.log"）。模式由包括以下特殊组件的字符串组成，则运行时要替换这些组件：

  - "/" 本地路径名分隔符

  - "%t" 系统临时目录

  - "%h" "user.home" 系统属性的值

  - "%g" 区分循环日志的生成号

  - "%u" 解决冲突的唯一号码

  - "%%" 转换为单个百分数符号"%"
