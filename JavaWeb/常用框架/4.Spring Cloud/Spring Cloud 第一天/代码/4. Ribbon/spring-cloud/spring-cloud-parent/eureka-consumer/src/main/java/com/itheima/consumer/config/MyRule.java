package com.itheima.consumer.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这个类配置ribbon的负载均衡策略
 */
@Configuration
public class MyRule {


    /**
     * IRule就是ribbon的负载均衡配置类
     * @return
     */
    @Bean
    public IRule rule(){
        return new RandomRule();
    }
}
