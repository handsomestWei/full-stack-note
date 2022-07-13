package models

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
)

// beego orm 初始化
func init() {
	orm.RegisterDriver("mysql", orm.DRMySQL)
	// 配置数据源连接，使用时指定别名
	orm.RegisterDataBase("default", "mysql", beego.AppConfig.String("mysql_url_master"), beego.AppConfig.DefaultInt("mysql_max_idle", 5), beego.AppConfig.DefaultInt("mysql_max_conns", 10))
	orm.RegisterDataBase("slave", "mysql", beego.AppConfig.String("mysql_url_slave"), beego.AppConfig.DefaultInt("mysql_max_idle", 5), beego.AppConfig.DefaultInt("mysql_max_conns", 10))
	// orm日志
	orm.Debug = true
}