# curl

## 基本使用

https://www.ruanyifeng.com/blog/2011/09/curl.html

可以在 http://www.httpbin.org 这个网站测试各种rest请求。

参数：

1. --help：查看帮助
2. -i：包含响应头
3. --head：只查看响应头，不返回响应体
4. -v：包含请求和响应的详细信息
5. -X：指定请求的http方法
   1. -X OPTIONS 可以查看网站支持的请求方法
6. -A "chrome"：修改user-agent，默认是 curl
7. -o filename：保存文件，并指定文件名
8. -O：按原名称保存文件
9. -L：跟随重定向请求跳转，默认是不跳转的
10. -H：设置头信息
11. -k：允许发起不安全的ssl请求
12. -b k=v：设置cookies

提交post表单

1. -X POST：指定使用post方法
2. -d：指定要提交的参数
   1. `-d "k1=v1&k2=v2"`
   2. `-d k1=v1 k2=v2`
   3. `-d k1=v1 -d k2=v2`
   4. `-d @filename.txt` 将参数保存在文件中上传
3. -i：登录时最好加上，因为页面如果跳转就不显示响应体了，可以用这个参数来查看有没有重定向

```shell
# 一次提交多个参数使用 & 连接。引号可以省略，http://也可以省略
curl -d "username=zs&password=123" -iX POST http://www.taobao.com/login
# 也可以使用多个 -d，一次提交一个参数
curl -iX POST -d "username=zs" -d "password=123" www.taobao.com/login
# 其实有了 -d，默认就是post请求，可以省略 -X POST
C:\Users\sheng>curl -d name=xiaoming http://www.httpbin.org/post
{
  "args": {},
  "data": "",
  "files": {},
  "form": {
    "name": "xiaoming"
  },
  "headers": {
    "Accept": "*/*",
    "Content-Length": "13",
    "Content-Type": "application/x-www-form-urlencoded",
    "Host": "www.httpbin.org",
    "User-Agent": "curl/7.79.1",
    "X-Amzn-Trace-Id": "Root=1-62359f7a-543ff8443cab74153200d3c2"
  },
  "json": null,
  "origin": "111.194.218.19",
  "url": "http://www.httpbin.org/post"
}
```



## 

