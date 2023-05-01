springboot推荐打包方式是jar。然后在java -jar .\包名 就能运行

也可以打包成war，然后在自己的tomcat中运行。需要把打包方式改成war，同时让启动了继承SpringBootServletInitializer并重写configure方法。 