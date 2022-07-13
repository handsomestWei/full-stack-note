package models

import (
	"github.com/astaxie/beego/orm"
	"context"
	"database/sql"
)

type Biz struct {
	Id   int64
	Name string `orm:"size(128)"`
}

func init() {
	// 这一步必须，需要注册
	orm.RegisterModel(new(Biz))
}

func AddBiz(m *Biz) (id int64, err error) {
	o := orm.NewOrm()
	// 默认使用default数据源，可以切换
	o.Using("slave")
	id, err = o.Insert(m)
	return
}

func AddBizByTx(m *Biz) (id int64, err error) {
	o := orm.NewOrm()
	// 开启事务
	o.BeginTx(context.Background(), &sql.TxOptions{Isolation: sql.LevelRepeatableRead})
	id, err = o.Insert(m)
	if err != nil {
		// 事务回滚
		o.Rollback()
	} else {
		// 事务提交
		o.Commit()
	}
	return
}