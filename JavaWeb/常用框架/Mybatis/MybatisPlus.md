## ORM

实体类的字段名是驼峰形式的，表的列名是蛇形的，MP会自动映射。

但是二者不相同时，可以使用注解来修改。

```java
public class User {
    
    //mail字段对应的表的列名是email
    @TableField(value = "email")
    private String mail;
    
    //表中不存在此列
    @TableField(exist = false)
    private String address;
    
    //查询时不返回此列
    @TableField(select = false)
    private String birthDay;
    
    //乐观锁标记
    @Version
    //标记填充字段
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
```

### 自动填充

自动添加功能的实现：

1. 使用 `@TableField(fill = FieldFill.INSERT)` 标记
2. 实现 MetaObjectHandler 中的方法

### ActiveRecord

这是 Rails 提出的 ORM 层的模型，可以直接把业务逻辑封装到实体类中，广受动态语言的喜爱。

MP 使用 AR，需要实体类继承 Model：
```java
public class User extends Model<User> {
    private Long id;
    private String username;
    private String mail;
}
```

同时要有继承了 BaseMapper 的 Mapper 存在：

```java
public interface UserMapper extends BaseMapper<User> {
    
}
```

使用起来非常简单：

```java
public class Test {
    
    public void test() {
        User user = new User();
        user.setId(1);
        
        //查询
        User user1 = user.selectById();
    }
}
```



## 主键id

### 自动回填

使用MP 的 `insert(T entity) `方法插入数据，返回的 `int result` 是影响的行数。同时 id 会被自动回填到 entity 中。

## update

### 自动忽略null

`updateById(T entity)` 根据 id 更新数据，默认 entity 中非 null 的数据才会被更新。

## delete

### 逻辑删除

1. 标记逻辑删除字段：

```java
@TableLogic
private Integer deleted;
```

2. 配置

```properties
# 逻辑已删除的值
mybatis-plus.global-config.db-config.logic-delete-value=1
# 逻辑未删除的值
mybatis-plus.global-config.db-config.logic-not-delete-value=0
```

配置了逻辑删除的话，所有的 select 语句都会在 where 条件中增加 deleted = 0 这个过滤条件，包括自己在 xml 中写的 sql 。

## select

### selectOne()

根据 Wrapper 查询一条记录，超过一条会报错。可以在最后添加 `.last(limit 1)`，确保只会查出一条数据。

### selectPage() 分页查询

添加分页插件：

```java
@Configuration
@MapperScan("org.example.dao")
public class MybatisPlusConfig {
    
    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
```

## 配置

```properties
# 单独设置mybatis的核心配置文件，指定文件的位置
mybatis-plus.config-location = classpath:sqlMapConfig.xml

# 单独加载映射配置文件
mybatis-plus.mapper-location = classpath*:mybatis/*.xml

# 配置缓存，默认为true
mybatis-plus.configuration.cache-enabled = false

# 全局配置主键策略
mybatis-plus.global-config.db-config.id-type=auto

# 全局配置表名的前缀
mybatis-plus.global-config.db-config.talbe-prefix=tb_
```

`classpath` 只加载当前模块的classpath，`classpath*` 加载当前模块和所有依赖的classpath

## 插件

### 乐观锁

原理：

1. 查询，得到 version
2. 更新时，set version = newVersion where version = oldVersion
3. 如果 version 不对，更新失败。

使用方式：

1. 创建 OptimisticLockerInterceptor 插件
2. 添加字段：

```java
public class User {
    
    @Version
    private Integer version;
}
```

接下来更新时，会自动更新 version 字段。

