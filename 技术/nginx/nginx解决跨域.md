跨域的请求其实都发出去了，也收到响应了，只是被浏览器拦截了。所以只要在 nginx 里配置一下跨域，告诉浏览器允许跨域，就不用在后端的项目里配置了。

下面的配置添加到 server 中，可以放在 location 里更精确的控制

```
#允许跨域请求
        add_header 'Access-Control-Allow-Origin' *;
        #允许带上cookie的请求
        add_header 'Access-Control-Allow-Credentials' 'true';
        #允许请求的方法
        add_header 'Access-Control-Allow-Methods' *;
        #允许请求的header
        add_header 'Access-Control-Allow-Headers' *;
```

