## 概念

SpringBoot有两个核心：

1. 起步依赖：将常用的包打包到一起
1. 自动配置

主程序启动时，会自动扫描同目录下和子目录下的所有类，加载到容器中。

## 热部署

引入依赖：spring-boot-devtools

idea 开启自动编译：build project automatically

## 配置文件

### 加载顺序

application.yml > application.yaml > application.properties > application*.yml

application.yml 中放入通用的配置

```yml
server:
  port: 8888 #指定端口
 
#值为数组或者列表，有两种写法
aaa:
  bbb: [t1,t2,t2]
  ccc:
    - c1
    - c1
    - c3
#值为map的两种写法
stu:
  map: {k1: v1, K2: v2}
  map:
    k1: v1
    k2: v2
```

application-dev.yml 放入 dev 环境的配置。需要添加启动参数：`spring.active.profile=dev`

### 读取配置文件的值

引入依赖：`spring-boot-configuration-processor`。这个依赖的作用是当我们编写配置文件时，可以自动提示。

```yml
index:
  studentGreetingMessageList:
    - 今日事要今日毕哦
    - 还有作业未交，及时提交哦
  advertisementList:
    - advertisementImageUrl: 
        https://homework-webfront.oss-cn-beijing.aliyuncs.com/MiniproStudent/images/banner/home_banner_2.png
      advertisementType: 1
      path: https://mp.weixin.qq.com/s/RmaqD8zpJoyNz-V_fvCeBg
      extraData:
        subscribeGuide: true
    - advertisementImageUrl: 
        https://homework-webfront.oss-cn-beijing.aliyuncs.com/MiniproStudent/images/banner/home_banner_1.png
      advertisementType: 1
      path: https://mp.weixin.qq.com/s/ZD-BeGoIdqWiYghcT4QEpQ

```

配置类：

```java
@Configuration
@Data
@ConfigurationProperties(prefix = "index") //将配置文件中以index开头的属性注入到类中
public class IndexConfig {

    private List<String> studentGreetingMessageList;

    private List<AdvertisementDTO> advertisementList;
}

```

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AdvertisementDTO {

    // 滚动图片路径
    private String advertisementImageUrl;

    // 广告类型
    private Integer advertisementType;

    // 跳转url
    private String path;

    // 小程序appId
    private String appId;

    // 其他配置数据
    private JSONObject extraData = new JSONObject();

}
```

或者使用@Value

### 读取自定义的配置文件

使用 @PropertySource 引入自定义配置文件，然后使用 @ConfigurationPeoperties 读取值

### 配置随机值

SpringBoot 有一个 RandomValuePropertySource 类，常用来对一些测试类注入随机值，配置类这么写：

```yaml
a:
  b: ${random.int}
  c: ${random.uuid}
  d: ${random.value}
```

### 参数间引用

后面的值可以引用前面的值：

```yaml
app:
  name: MyApp
  description: ${app.name} is a Spring Application
```



## 传参方式

HTTP协议的四种传参方式

| HTTP协议组成         | 协议内容示例                                     | 对应Spring注解 |
| :------------------- | :----------------------------------------------- | :------------- |
| path info传参        | /articles/12 (查询id为12的文章，12是参数)        | @PathVariable  |
| URL Query String传参 | /articles?id=12                                  | @RequestParam  |
| Body 传参            | Content-Type: multipart/form-data                | @RequestParam  |
| Body 传参            | Content-Type: application/json，或其他自定义格式 | @RequestBody   |
| Headers 传参         |                                                  | @RequestHeader |

## 路径问题

1. Controller 的路径
   1. 使用@RequestMapping() 来进行映射
      1. /Class层级/方法层级
      2. 比如：/user/userInfo/update
   2. 网站根目录
      1. 在 SpringBoot 中是：localhost:8080
      2. 在 SpringMVC 中是：localhost:8080/项目名称
2. 静态资源的路径（resources/static)
   1. 映射关系：`localhost:8080/` ——> `resources/static/` ，默认找 index.html 页面
   2. 可以自己配置静态资源的映射，实现 WebMvcConfigurer 接口。

 ## jackson的使用

https://www.jianshu.com/p/201943f8d579

### 日期格式

字符串日期和 Java Date 类型的序列化，反序列化：

```yml
spring:
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
```

上面的是全局配置，还可以在实体类中自定义日期格式：

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Integer id;
    
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
```

也可以在 controller 方法的入参前面加上注解：

```java
public String hello(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("createTime") Date createTime)
```

注解@JsonFormat主要是后台到前台的时间格式的转换

注解@DateTimeFormat主要是前后到后台的时间格式的转换



### 个性化返回格式

```java
@Data
@Builder
//指定返回给前端的字段顺序
@JsonPropertyOrder(value = {"id", "author", "title", "createTime"})
public class Book {

    //指定返回或接收的字段名
    @JsonProperty(value = "bookId")
    private Integer id;

    //敏感数据不返回、不接收
    @JsonIgnore
    private String password;
    
    private String title;

    //为null时，不返回这个字段
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String author;
	
    //指定返回或接收的日期格式
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
```

