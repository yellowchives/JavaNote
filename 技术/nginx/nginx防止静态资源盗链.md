location 里添加下面的代码：

```
#对源站点进行验证
        valid_referers *.imooc.com;
        #非法引入会进行判断
        if ($invalid_referer) {
            return 404;
        }
```

