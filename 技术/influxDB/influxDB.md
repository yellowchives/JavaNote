# influxDB

## 概述

最流行的时序数据库。

https://jasper-zhang1.gitbooks.io/influxdb/content/

基本概念：

https://jasper-zhang1.gitbooks.io/influxdb/content/Concepts/key_concepts.html

https://www.hellodemos.com/hello-influxdb/influxdb-demos.html

## 安装

如果在windows安装，需要修改保存meta、data、wal-dir 的目录，改成windows的目录样式。

[(40条消息) Windows安装InfluxDB以及使用_wen_xkang的博客-CSDN博客_influxdb安装部署windows](https://blog.csdn.net/wen_xkang/article/details/104746280)

## springBoot使用influxDB

1. 引入依赖

   ```xml
   <dependency>
       <groupId>org.influxdb</groupId>
       <artifactId>influxdb-java</artifactId>
   </dependency>
   ```

2. application.yml

   ```yml
   server:
     port: 8080
   spring:
     application:
       name: myInfluxDBProject
     influx:
       url: http://127.0.0.1:8086 # 必须加上http://
       user: admin
       password: admin
       database: mydb
   ```

3. 创建InfluxDB对象，使用配置类

   ```java
   @Configuration
   public class InfluxDBConfig {
       @Value("${spring.influx.url:''}")
       private String url;
   
       @Value("${spring.influx.user:''}")
       private String user;
   
       @Value("${spring.influx.password:''}")
       private String password;
   
       @Value("${spring.influx.database:''}")
       private String database;
   
       @Bean
       public InfluxDB getInfluxDB() {
           //建立连接
           InfluxDB influxDB = InfluxDBFactory.connect(url, user, password);
           //创建数据库，如果使用已有的数据库就不用创建了
           influxDB.query(new Query("CREATE DATABASE \"" + database + "\""));
           //使用的数据库
           influxDB.setDatabase(this.database);
   
           //设置保存策略。
           如果名字由字母或下划线组成不用加引号，短划线必须被引号包起来
           String retentionPolicyName = "two_week";
           influxDB.query(new Query("CREATE RETENTION POLICY \"" + retentionPolicyName
                   + "\" ON \"" + this.database + "\" DURATION 2w REPLICATION 1 DEFAULT"));
           influxDB.setRetentionPolicy(retentionPolicyName);
   
           // Close it if your application is terminating or you are not using it anymore.
           Runtime.getRuntime().addShutdownHook(new Thread(influxDB::close));
   
           return influxDB;
       }
   }
   ```

4. 实体类

   ```java
   @Data
   @Builder
   @Measurement(name = "userLogin")
   public class UserLoginMeasurement {
       @Column(name = "time")
       private Instant time;
   
       @Column(name = "schoolName", tag = true)
       private String schoolName;
       @Column(name = "schoolId", tag = true)
       private String schoolId;
       @Column(name = "userType", tag = true)
       private String userType;
   
       @Column(name = "uid")
       private Long uid;
       @Column(name = "phone")
       private String phone;
       @Column(name = "name")
       private String name;
   }
   ```

5. Controller和Service

   ```java
   @RestController
   public class HelloController{
       @Resource
       private InfluxDB influxDB;
       
   	//这个路径的每次访问都会将登录信息保存到influxDB
       @GetMapping("/login")
       public String login(String name) {
           //将想要插入数据库的数据保存成一个pojo
           System.out.println(name);
           Random random = new Random();
           int schoolId = random.nextInt(4) + 1;
           int classId = random.nextInt(4) + 1;
           UserLogin build = UserLogin.builder().studentName(name).schoolId(schoolId + "").classId(classId + "").build();
           
   		//使用Point对象将pojo对象插入数据库，根据实体类的@Column注解来映射
           //设置time可以省略，默认系统时间
           Point point = Point.measurementByPOJO(UserLogin.class)
                   .addFieldsFromPOJO(build)
                   .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                   .build();
           influxDB.write(point);
           
           return name + "登录成功";
       }
   }
   ```
   

influxDB记录如下：

```
> select * from userLogin
name: userLogin
time                     classId schoolId studentName
----                     ------- -------- -----------
2022-03-18T08:06:49.572Z 3       3        jack
2022-03-18T08:06:51.472Z 4       1        jack
2022-03-18T08:06:52.125Z 4       4        jack
2022-03-18T08:06:52.959Z 4       3        jack
2022-03-18T08:06:53.56Z  2       2        jack
2022-03-18T08:07:00.309Z 2       4        tom
2022-03-18T08:07:01.752Z 4       1        tom
2022-03-18T08:13:17.801Z 4       1        lalala
```

## 易错

通过 addaddFieldsFromPOJO() 来为Point插入值必须确保至少有一个 field 不为null，tag可以为null。

在设置POJO时，如果没有指定time，默认设置系统时间，精度是纳秒。

InfluxDB的write方法没有成功写入，试试 flush() 或者 close() 方法。

InfluxDB是一个无模式(schemaless)的数据库，你可以在任意时间添加measurement，tags和fields。

> 注意：如果你试图写入一个和之前的类型不一样的数据(例如，filed字段之前接收的是数字类型，现在写了个字符串进去)，那么InfluxDB会拒绝这个数据。

