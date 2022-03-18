# influxDB

## 概述

最流行的时序数据库。

https://jasper-zhang1.gitbooks.io/influxdb/content/

基本概念：

https://jasper-zhang1.gitbooks.io/influxdb/content/Concepts/key_concepts.html

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
           InfluxDB influxDB = InfluxDBFactory.connect(url, user, password);
           influxDB.setDatabase(this.database);
           influxDB.setRetentionPolicy("autogen"); //使用默认的保存策略
           return influxDB;
       }
   }
   ```

4. 实体类

   ```java
   @Data
   @Builder
   @Measurement(name = "userLogin")
   public class UserLogin {
       @Column(name = "time")
       private String time;
   
       //两个tag
       @Column(name = "schoolId", tag = true)
       private String schoolId;
       @Column(name = "classId", tag = true)
       private String classId;
   
       //field
       @Column(name = "studentName", tag = false)
       private String studentName;
   }
   ```

5. Controller和Service

   ```java
   @RestController
   public class HelloController{
       @Resource
       private UserLoginService userLoginService;
   	//这个路径的每次访问都会将登录信息保存到influxDB
       @GetMapping("/login")
       public String login(String name) {
           return userLoginService.userLogin(name);
       }
   }
   ```

   ```java
   @Service
   public class UserLoginService {
       @Resource
       private InfluxDB influxDB;
   
       public String userLogin(String name) {
          	//将想要插入数据库的数据保存成一个pojo
           System.out.println(name);
           Random random = new Random();
           int schoolId = random.nextInt(4) + 1;
           int classId = random.nextInt(4) + 1;
           UserLogin build = UserLogin.builder().studentName(name).schoolId(schoolId + "").classId(classId + "").build();
           
   		//使用Point对象将pojo对象插入数据库，根据实体类的@Column注解来映射
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



