## Docker 快速入门

> [🎉 Docker 简介和安装 - Docker 快速入门 - 易文档 (easydoc.net)](https://docker.easydoc.net/doc/81170005/cCewZWoN/lTKfePfP)

windows 推荐安装 wsl 和 docker-desktop

## 概念：

- 镜像：项目的完整运行环境打包在一起
- 容器：镜像运行起来的实例
- 仓库：存放镜像的仓库

## 镜像命令

查看 docker images

查看运行中的容器 docker ps

搜索 docker search mysql

下载 docker pull mysql[:TAG] 默认latest

查看占用空间 docker system df

删除 docker rmi [-f] mysql 、docker rmi image-id

仓库名、标签都是 <none> 的镜像，叫做dangling image

## 容器命令

启动容器：

```shell
docker run -d -p 3306:3306 \
--privileged=true \
-v /data/mysql/log:/var/log/mysql \
-v /data/mysql/data:/var/lib/mysql \
-v /data/mysql/conf:/etc/mysql/conf.d \
-e MYSQL_ROOT_PASSWORD=123456  \
--name mysql mysql:latest
```

```shell
docker run -d \
-p 6379:6379 \
--name redis \
--privileged=true \
-v /app/redis/redis.conf:/etc/redis/redis.conf \
-v /app/redis/data:/data \
-d redis:latest redis-server /etc/redis/redis.conf
```

`docker run -d --name nginx `

options:

- 为容器指定名字 --name

- 后台运行容器 -d

- -i 以交互模式运行，通常与 -t 一起使用

- -t 为容器重新分配一个伪终端 `docker run ubuntu:latest /bin/bash`

- 指定端口映射 -p
- `--privileged=true -v 宿主机文件:容器文件` 挂载容器的目录或者文件到宿主机的目录或文件

查看容器

- 查看运行的容器 docker ps

- 查看所有容器 docker ps -a
- 查看最新创建的 -l

启动容器 docker start id/name

停止容器 docker stop id/name

删除容器 docker rm [-f] id/name

进入后台容器 docker exec -it id /bin/bash，进入后再输入 `mysql -uroot -proot` 就可以使用了。使用 exit 退出容器。

提交容器成为镜像 docker commit

容器日志：`docker logs id`

> 安装mongo https://zhuanlan.zhihu.com/p/610560696