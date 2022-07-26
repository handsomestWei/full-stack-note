## 获得post请求参数，并在网页输出
ngx.req.read_body()  
ngx.say("post args begin", "<br/>")  
local post_args = ngx.req.get_post_args()  
for k, v in pairs(post_args) do  
    if type(v) == "table" then  
        ngx.say(k, " : ", table.concat(v, ", "), "<br/>")  
    else  
        ngx.say(k, ": ", v, "<br/>")  
    end  
end