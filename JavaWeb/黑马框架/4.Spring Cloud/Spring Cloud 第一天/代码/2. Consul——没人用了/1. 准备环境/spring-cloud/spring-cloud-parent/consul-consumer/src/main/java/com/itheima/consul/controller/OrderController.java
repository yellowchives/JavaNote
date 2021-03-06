package com.itheima.consul.controller;


import com.itheima.consul.domain.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 服务的调用方
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/goods/{id}")
    public Goods findGoodsById(@PathVariable("id") int id) {
        System.out.println("findGoodsById..." + id);

        /*
         * 远程调用Goods服务中的findOne接口
         * 使用RestTemplate
         * 1. 定义Bean restTemplate
         * 2. 注入Bean
         * 3. 调用方法
         */

        /*
         * 动态从Eureka Server 中获取 provider 的 ip 和端口
         * 1. 注入 DiscoveryClient 对象.需要激活，在启动类加个@EnableDiscoveryClient
         * 2. 调用方法，获取实例。
         */

        // 演示discoveryClient 使用
        List<ServiceInstance> instances = discoveryClient.getInstances("consul-provider");

        // 对应的应用名没有获取到实例
        if (instances == null || instances.size() == 0) {
            return null;
        }

        // 这里没有多服务器，只有一个实例，所以get(0)即可
        ServiceInstance instance = instances.get(0);
        String host = instance.getHost();// 获取ip
        int port = instance.getPort();// 获取端口

        // 拼接url
        String url = "http://" + host + ":" + port + "/goods/findOne/" + id;
        //// 发送restful请求
        Goods goods = restTemplate.getForObject(url, Goods.class);

        return goods;
    }
}
