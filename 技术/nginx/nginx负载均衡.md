```
	#配置上游服务器，给集群起个名字
    #默认负载均衡策略是轮询
    #下面使用了权重策略
    upstream tomcats {
        server 192.168.1.173:8080 weight=1;
        server 192.168.1.174:8080 weight=1;
        server 192.168.1.175:8080 weight=5;
    }
    #符合下面条件的转发给集群处理
    server {
        listen         80;
        server_name    www.tomcats.com;

        localhost / {
            proxy_pass http://tomcats;
        }
    }
```

> [Module ngx_http_upstream_module (nginx.org)](https://nginx.org/en/docs/http/ngx_http_upstream_module.html)

负载均衡：ip_hash、一致性哈希算法、url_hash
