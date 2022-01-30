package com.itheima.springbootcondition.condtion;


import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 自定义的注解，代替@Conditional
 * 为了动态的指定判断条件。
 * 这种类在springboot-autoConfigure中大量存在，都是各种的条件注解，用来判断是否创建bean
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ClassCondition.class)
public @interface ConditionOnClass {
    String[] value();
}
