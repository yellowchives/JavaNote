package com.itheima.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 读取配置文件中的属性生成一个属性类
 */
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    private String host = "localhost";
    private int port = 6379;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
