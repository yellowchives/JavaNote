spring cloud config 是分布式配置中心
可以集中管理配置文件，还可以方便的切换环境

1. 创建远程仓库，上传了一个yml文件
2. 创建config-server，配置一下
3. 通过localhost:port/master/配置文件名 来访问
4. 客户端引入坐标，配置一下config-server的url（集成eureka就不用写死了）。使用bootstrap.yml，这个会在application.yml之前加载

远程配置修改后，config-server会自动更新，但是客户端不会自动更新，需要重启自动生效。
如果想要客户端不重启自动更新，需要在客户端引入actuator依赖，加上@RefreshScope，还需要添加一些配置，并发送刷新的请求。
客户端更新配置还是很麻烦，结合下一节的Bus就容易了