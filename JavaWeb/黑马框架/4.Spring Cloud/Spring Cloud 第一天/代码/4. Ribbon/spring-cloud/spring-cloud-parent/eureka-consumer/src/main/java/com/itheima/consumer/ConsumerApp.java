package com.itheima.consumer;

import com.itheima.consumer.config.MyRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@EnableDiscoveryClient // 激活DiscoveryClient
@EnableEurekaClient
@SpringBootApplication
/*
    配置Ribbon的负载均衡策略 name
    * name：设置服务提供方的应用名称。告诉框架当前策略适用于哪个服务提供方。
    * configuration:设置负载均衡Bean
 */
//@RibbonClient(name="EUREKA-PROVIDER",configuration = MyRule.class)
public class ConsumerApp {


    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class,args);
    }
}
