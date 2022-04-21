1. 引入坐标：provider和consumer一样，都使用nacos配置中心

   ```xml
   <dependencies>
   	<dependency>
           <groupId>org.apache.dubbo</groupId>
           <artifactId>dubbo</artifactId>
           <version>2.7.6</version>
   	</dependency>
       <dependency>
           <groupId>org.apache.dubbo</groupId>
           <artifactId>dubbo-spring-boot-starter</artifactId>
           <version>2.7.6</version>
       </dependency>
       <dependency>
           <groupId>com.alibaba</groupId>
           <artifactId>dubbo-registry-nacos</artifactId>
           <version>2.7.6</version>
       </dependency>
   </dependencies>
   
   ```

2. nacos配置：以下配置需要SpringBoot环境

   ```yaml
   dubbo:
     application:
       name: fp-homework
     registry:
       address: nacos://123.57.251.70:8848?namespace=dev
       use-as-config-center: false
     consumer:
       check: false
       retries: 0
       timeout: 5000
     provider:
     	delay: 5000
     protocol:
       name: dubbo
       port: -1
   ```

   ```yaml
   dubbo:
     application:
       name: dm-homework
     registry:
       address: nacos://123.57.251.70:8848?namespace=dev
       use-as-config-center: false
     consumer: 
       check: false
       retries: 0
     protocol:
       name: dubbo
       port: -1
   ```

3. provider项目:

   1. 接口

      ```java
      public interface AssignHomeworkService {
          String sayHello(String name);
      }
      ```

   2. 实现类：需要两个注解，@DubboService标记这是一个dubbo 提供者，@Component 将对象交给Spring容器管理。

      ```java
      @DubboService
      @Component
      public class AssignHomeworkServiceImpl implements AssignHomeworkService {
      	@Override
          public String sayHello(String name) {
              return "hello " + name;
          }
      }
      ```

   3. 启动类：添加@EnableDubbo注解，指定要扫描的provider所在的包

      ```java
      @SpringBootApplication(scanBasePackageClasses = {DomainHomeworkApplication.class})
      @EnableDubbo(scanBasePackages = {"com.learnable.domain.homework.service.impl"})
      public class DomainHomeworkApplication {
          public static void main(String[] args) {
              SpringApplication springApplication = new SpringApplication(DomainHomeworkApplication.class);
              springApplication.run(args);
          }
      
      }
      ```

4. consumer项目：

   1. 接口：调用RPC服务的接口一般命名为integration

      ```java
      public interface AssignDesignedHomeworkIntegration {
      	String sayHello(String name);
      }
      ```

   2. 实现类：@DubboReference 注入RPC provider对象

      ```java
      @Component //交给spring容器管理
      public class AssignDesignedHomeworkIntegrationImpl implements AssignDesignedHomeworkIntegration {
          @DubboReference
          private AssignHomeworkService assignHomeworkService;
          
          @Override
          public String sayHello(String name) {
              return assignHomeworkService.sayHello(name);
          }
      }
      ```

   3. 启动类：添加@EnableDubbo注解，消费者不用扫描了

      ```java
      @SpringBootApplication(scanBasePackageClasses = {FrontHomeworkApplication.class})
      @EnableDubbo
      public class FrontHomeworkApplication {
          public static void main(String[] args) {
              SpringApplication springApplication = new SpringApplication(FrontHomeworkApplication.class);
              springApplication.run(args);
          }
      }
      ```

      

