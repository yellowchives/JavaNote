package com.itheima.springbootcondition.config;

import com.itheima.springbootcondition.condtion.ClassCondition;
import com.itheima.springbootcondition.condtion.ConditionOnClass;
import com.itheima.springbootcondition.domain.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Configuration用来定义配置类
 * @Conditional注意用来判断条件，返回true时才会在容器中创建bean
 * 需要一个Condition的实现类
 * @ConditionalOnProperty只有在配置文件中有对应的属性时才会创建bena
 */
@Configuration
public class UserConfig {

    @Bean
    //@Conditional(ClassCondition.class)
    //@ConditionOnClass("com.alibaba.fastjson.JSON")
    public User user(){
        return new User();
    }

    @Bean
    @ConditionalOnProperty(name = "itcast",havingValue = "itheima")
    public User user2(){
        return new User();
    }

}
