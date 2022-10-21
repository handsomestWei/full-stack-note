# cross-domain
跨域解决方案

## 什么是跨域
跨域不是问题，是一种安全机制。
```
跨域并不会阻止请求的发出，也不会阻止请求的接受，跨域是浏览器为了保护当前页面，页面请求得到了响应，浏览器不会把响应的数据交给页面上的回调，会有报错提示是一个跨域数据。
```
```
跨域是浏览器加载了与当前域名、协议、端口不同另一站点下的资源，这与各大支持JavaScript的浏览器的同源策略是违背的。所谓同源策略，它是由Netscape提出的一个著名的安全策略。现在所有支持JavaScript 的浏览器都会使用这个策略。所谓同源是指，域名，协议，端口相同。
```

## 解决方案
### CORS
后端设置Access-Control-Allow-Origin响应头。告知浏览器不要拦截响应，允许数据通行。
```
请求响应例：
// 从http://example.com界面发出了一个请求到：http://example2.com，因为不同源，导致了跨域。
// 而http://example2.com返回了下面的响应头：
Content-Type: application/json;charset=utf-8
Content-Length: 3210
Server: apache
Access-Control-Allow-Origin: http://example.com

由于浏览器检测到http://example2.com的响应头中显示的写着：Access-Control-Allow-Origin: http://example.com，如果请求数据的源是http://example.com则可以允许访问返回的数据。这样浏览器就不会抛出错误提示，而是正确的将数据交给页面的ajax回调。
```

### JSONP
利用script标签的src没有跨域限制来完成。只能进行GET请求。优势在于支持老式浏览器，以及可以向不支持CORS的网站请求数据，有的浏览器不允许https跨站访问http网站。
```
前端js例：
<script type='text/javascript'>
    window.jsonpCallback = function (res) {
        console.log(res)
    }
</script>
<script src='http://localhost:8080/api/jsonp?id=1&cb=jsonpCallback' type='text/javascript'></script>
```