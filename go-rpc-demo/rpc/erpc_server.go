package rpc

import (
	"fmt"
	"github.com/henrylee2cn/erpc/v6"
	"github.com/henrylee2cn/erpc/v6/plugin/auth"
	"github.com/henrylee2cn/erpc/v6/plugin/heartbeat"
	"time"
	"github.com/handsomestWei/go-rpc-demo/config"
	"github.com/handsomestWei/go-rpc-demo/dto"
)

var srv erpc.Peer

func InitErpcSvcByStructMod() {
	erpc.SetLoggerLevel("INFO")
	//defer erpc.FlushLogger()
	// graceful
	go erpc.GraceSignal()
	// server peer
	srv := erpc.NewPeer(erpc.PeerConfig{
		CountTime:   true,
		ListenPort:  uint16(config.Conf.SvcListenPort),
		PrintDetail: true,
	},
		authChecker,
		heartbeat.NewPong())
	// srv.SetTLSConfig(erpc.GenerateTLSConfigForServer())

	// 注册路由
	srv.RouteCall(new(Demos))

	// 广播，心跳保持
	go func() {
		for {
			time.Sleep(time.Second * time.Duration(config.Conf.BroadcastRateSecond))
			srv.RangeSession(func(sess erpc.Session) bool {
				sess.Push(
					"/demo/status",
					fmt.Sprintf("this is a broadcast, server time: %v", time.Now()),
				)
				return true
			})
		}
	}()

	// listen and serve
	srv.ListenAndServe()
}

// 客户端接入令牌校验
var authChecker = auth.NewCheckerPlugin(
	func(sess auth.Session, fn auth.RecvOnce) (ret interface{}, stat *erpc.Status) {
		var authInfo string
		stat = fn(&authInfo)
		if !stat.OK() {
			return
		}
		erpc.Infof("auth info: %v", authInfo)
		if config.Conf.BearerToken != authInfo {
			return nil, erpc.NewStatus(403, "auth fail", "auth fail detail")
		}
		return "pass", nil
	},
	erpc.WithBodyCodec('s'),
)

/**********************************************************************************************************************/
type Demos struct {
	erpc.CallCtx
}

func (c *Demos) Receive(pack *dto.DemoPack) (int, *erpc.Status) {
	var result int
	if pack == nil {
		return result, erpc.NewStatus(erpc.CodeOK, "数据包为空", nil)
	}
	erpc.Infof("客户端信息【%s】，数据包编号【%s】", c.PeekMeta("clientMeta"), pack.Id)
	// handle data
	rsp := true
	if rsp {
		return result, erpc.NewStatus(erpc.CodeOK, "", nil)
	} else {
		return result, erpc.NewStatus(erpc.CodeInternalServerError, "处理失败", nil)
	}
}
