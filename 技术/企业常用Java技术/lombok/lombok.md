# lombok

## 使用步骤

1. 导入依赖

   ```xml
   <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <version>1.18.22</version>
       <scope>provided</scope>
   </dependency>
   ```

2. 使用注解

   ```java
   @Data
   public class User {
       private String id;
       private String name;
       private Integer age;
   }
   ```

3. idea 中安装 lombok 插件，提供可视化功能。在 idea 的设置——build——compiler——annotation processor 勾选 enable annotation processing。

## 原理

lombok会在编译时将setter、getter等方法插入编译后的字节码文件中。在idea的target/classes下可以看到编译后的文件（也可以在structure 中看）。

因为 lombok 只在编译时生效，在项目运行时不起作用（因为运行时直接跑的是 class 文件），所以他的 scope 是 provided，这样就不会被打包到 jar 中了。

## 注解详解

1. @Data：作用在类上，提供setter、getter、hashCode、equals、toString方法。不会生成无参构造，但Java会提供默认的无参构造。

2. @Getter、@Setter、@ToString：用在类上。

3. @AllArgsConstructor：提供全参构造。通常和 @NoArgsConstructor 一起使用，因为有了全参构造 JVM 就不提供默认的无参构造了。

4. 没有办法生成指定 filed 的 setter、getter，只能用老办法手写。

5. @Accessors：用在类上，value 属性默认为 true，给类中的 setter 开启链式调用。用的不多，一般使用@Builder。

   ```java
   user.setName("小明").setId(1).setAge(11);
   //这就是setter的链式调用。
   ```

6. @Builder：将类转为建造者模式。

   ```java
   //class文件反编译后的结果
   public static User.UserBuilder builder() {
       return new User.UserBuilder();
   }
   public static class UserBuilder {
       private Integer id;
       private String name;
       private Date birthday;
   
       UserBuilder() {
       }
   
       public User.UserBuilder id(Integer id) {
           this.id = id;
           return this;
       }
   
       public User.UserBuilder name(String name) {
           this.name = name;
           return this;
       }
   
       public User.UserBuilder birthday(Date birthday) {
           this.birthday = birthday;
           return this;
       }
   
       public User build() {
           return new User(this.id, this.name, this.birthday);
       }
   
       public String toString() {
           return "User.UserBuilder(id=" + this.id + ", name=" + this.name + ", birthday=" + this.birthday + ")";
       }
   }
   //测试代码
   User user = User.builder().id(2).name("老王").build();
   //User user = new User.UserBuilder().id(2).name("老王").build();
   System.out.println(user);
   ```

7. @slf4j：用在类（一般是 controller）上，用来为类快速定义一个日志变量，相当于在类中添加了以下代码：`private Logger log = LoggerFactory.getLogger(this.getClass());`。而且提供了占位输出日志的方法，相当好用。

   ```java
   @RestController
   @slf4j
   public class UserController {
       @RequestMapping("/findAll")
       public String findAll() {
           log.error("{} 的 {} 遇到了错误。", UserController, findAll);
           return "index";
       }
   }
   ```

8. @EqualsAndHashCode：生成hashCode和equals方法。很少单独使用，因为@Data包含了。

