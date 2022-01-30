feign是一个声明式的rest客户端，作用在consumer中
比ribbon和restTemplate都要简单

使用步骤：
1. 在消费端引入open-feign依赖
2. 编写feign调用接口
3. 在启动类添加@EnableFeignClients注解，开启feign功能