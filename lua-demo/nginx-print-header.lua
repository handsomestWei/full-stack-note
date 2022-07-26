## 获得请求头信息，并在网页输出
local headers = ngx.req.get_headers()                         
ngx.say("Host : ", headers["Host"], "<br/>")  
ngx.say("user-agent : ", headers["user-agent"], "<br/>")  
ngx.say("user-agent : ", headers.user_agent, "<br/>")
for k,v in pairs(headers) do  
    if type(v) == "table" then  
        ngx.say(k, " : ", table.concat(v, ","), "<br/>")  
    else  
        ngx.say(k, " : ", v, "<br/>")  
    end  
end