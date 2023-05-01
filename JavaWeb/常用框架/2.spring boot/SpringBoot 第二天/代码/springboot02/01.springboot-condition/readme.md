Condition是Spring4.0后引入的条件化配置接口，通过实现Condition接口可以完成有条件的加载相应的Bean

SpringApplication.run会返回ioc容器

springboot-web启动器会引入web服务器，没有这个依赖只能当做普通的ioc容器使用

web依赖默认提供了tomcat的服务器，我们可以将其排除，引入其他的服务器依赖。具体的配置看：org\springframework\boot\spring-boot-autoconfigure\2.1.8.RELEASE\spring-boot-autoconfigure-2.1.8.RELEASE.jar!\org\springframework\boot\autoconfigure\web\embedded\EmbeddedWebServerFactoryCustomizerAutoConfiguration.class
