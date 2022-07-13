package routers

import (
	"github.com/handsomestWei/full-stack-note/beego-demo/controllers"
	"github.com/astaxie/beego"
)

// 路由注册
func init() {

    beego.Router("/", &controllers.MainController{})

    // 使用了//@router注解，自动生成路由
	beego.Include(&controllers.CmsController{})
	// 自动化文档只支持NS写法的解析，且只支持二层嵌套
	ns := beego.NewNamespace("/api",
		beego.NSNamespace("/v1",beego.NSInclude(&controllers.BizController{})))
	beego.AddNamespace(ns)
}
