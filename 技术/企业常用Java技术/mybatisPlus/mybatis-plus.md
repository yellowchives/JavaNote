# mybatis-plus

## 快速使用

1. 导包：mybatis-plus的包内置了 jdbc 和 mysql 的包。

   ```xml
   <parent>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-parent</artifactId>
       <version>2.6.7</version>
       <relativePath/>
   </parent>
   
   <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter</artifactId>
           </dependency>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-test</artifactId>
               <scope>test</scope>
           </dependency>
           
           <!--lombok-->
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <version>1.18.22</version>
           </dependency>
   
           <!--mybatis-plus-->
           <dependency>
               <groupId>com.baomidou</groupId>
               <artifactId>mybatis-plus-boot-starter</artifactId>
               <version>3.5.1</version>
           </dependency>
           
           <!--mysql驱动-->
           <dependency>
               <groupId>mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <version>8.0.27</version>
           </dependency>
       </dependencies>
   ```

2. application.yml

   ```yml
   spring:
     datasource:
       url: jdbc:mysql://rm-2ze6vkod6j06d87406o.mysql.rds.aliyuncs.com:3306/userdomain_dev?characterEncoding=UTF-8&useSSL=false&useAffectedRows=true
       username: userdomain_dev
       password: learnable_AI
       driver-class-name: com.mysql.jdbc.Driver
   ```

3. 启动类：

   ```java
   package org.example;
   
   import org.mybatis.spring.annotation.MapperScan;
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   
   /**
    * @Author: ShenGuopin
    * @Date: 2022/4/26 17:08
    * @Version: 1.0
    */
   @SpringBootApplication
   @MapperScan("org.example.mapper") //扫描mapper所在的包
   public class ApplicationRunner {
       public static void main(String[] args) {
           SpringApplication.run(ApplicationRunner.class, args);
       }
   }
   ```

4. 实体类：

   ```java
   package org.example.po;
   
   import com.baomidou.mybatisplus.annotation.TableName;
   import lombok.AllArgsConstructor;
   import lombok.Builder;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   import java.util.Date;
   
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   @Builder
   @TableName("school") //实体类和表名不一致时指定表名
   public class SchoolPO {
       private Integer id;
   
       private Integer schoolNumber;
   
       private String schoolName;
   
       private Integer province;
   
       private Integer city;
   
       private Integer county;
   
       private Integer type;
   
       private String arrangement;
   
       private String period;
   
       private Integer status;
   
       private Date createTime;
   
       private Date updateTime;
   }
   
   ```

5. mapper接口：继承 BaseMapper

   ```java
   package org.example.mapper;
   
   import com.baomidou.mybatisplus.core.mapper.BaseMapper;
   import org.example.po.SchoolPO;
   
   public interface SchoolMapper extends BaseMapper<SchoolPO> {
   }
   
   ```

6. 测试：

   ```java
   package org.example;
   
   import org.example.mapper.SchoolMapper;
   import org.example.po.SchoolPO;
   import org.junit.jupiter.api.Test;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.boot.test.context.SpringBootTest;
   import java.util.List;
   
   @SpringBootTest
   public class SampleTest {
       @Autowired
       private SchoolMapper schoolMapper;
   
       @Test
       public void test() {
           List<SchoolPO> schoolPOS = schoolMapper.selectList(null);
           System.out.println(schoolPOS);
       }
   }
   
   ```

## 快速测试

导包：

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter-test</artifactId>
    <version>3.5.1</version>
</dependency>
```

用例：

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisPlusTest
class MybatisPlusSampleTest {

    @Autowired
    private SampleMapper sampleMapper;

    @Test
    void testInsert() {
        Sample sample = new Sample();
        sampleMapper.insert(sample);
        assertThat(sample.getId()).isNotNull();
    }
}

```



## 注解

https://baomidou.com/pages/223848/#tablename

1. @TableName
    1. 描述：表名注解，标识实体类对应的表
    2. 使用位置：实体类
2. @TableId
    1. 描述：主键注解
    2. 使用位置：实体类主键字段
3. @TableField
    1. 描述：字段注解（非主键）