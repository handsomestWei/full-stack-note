package rpc

import (
	"github.com/handsomestWei/go-rpc-demo/config"
	"github.com/handsomestWei/go-rpc-demo/dto"
	"github.com/handsomestWei/go-rpc-demo/log"
	"github.com/henrylee2cn/erpc/v6"
	"github.com/henrylee2cn/erpc/v6/plugin/auth"
	"github.com/henrylee2cn/erpc/v6/plugin/heartbeat"
	"go.uber.org/zap"
	"time"
)

var erpcSession erpc.Session

func InitERpcClientByStructMod() {
	erpc.SetLoggerLevel("TRACE")()

	cli := erpc.NewPeer(
		erpc.PeerConfig{
			PrintDetail:    true,
			RedialTimes:    1,
			RedialInterval: time.Duration(config.Conf.RedialInterval) * time.Second,
			DialTimeout:    30 * time.Second,
		},
		authBearer,
		heartbeat.NewPing(config.Conf.HeartPingRateSecond, true),
	)
	// cli.SetTLSConfig(&tls.Config{InsecureSkipVerify: true})

	// 注册路由
	cli.RoutePush(new(Demo))

	ses, stat := cli.Dial(config.Conf.CliDialAddr)
	if !stat.OK() {
		erpc.Infof("%v", stat)
		erpcSession = nil
		return
	}

	erpc.Infof("init new session complete")
	erpcSession = ses
}

// 客户端接入令牌校验
var authBearer = auth.NewBearerPlugin(
	func(sess auth.Session, fn auth.SendOnce) (stat *erpc.Status) {
		var ret string
		stat = fn(config.Conf.BearerToken, &ret)
		if !stat.OK() {
			return
		}
		erpc.Infof("auth info: %s, result: %s", config.Conf.BearerToken, ret)
		return
	},
	erpc.WithBodyCodec('s'),
)

/**********************************************************************************************************************/
type Demo struct {
	erpc.PushCtx
}

// Push handles '/demo/status' message
func (c *Demo) Status(arg *string) *erpc.Status {
	erpc.Printf("%s", *arg)
	return nil
}

func (c *Demo) PushDemoData(pack *dto.DemoPack) *erpc.Status {
	var result int
	session := c.getSession()
	if session == nil {
		log.Logger.Warn("数据包推送失败：连接失败", zap.Any("pack id", pack.Id))
		return erpc.NewStatus(erpc.CodeInternalServerError, "处理失败", nil)
	}
	stat := session.Call("/demos/receive",
		pack,
		&result,
		erpc.WithAddMeta("clientMeta", config.Conf.Meta),
	).Status()
	if !stat.OK() {
		log.Logger.Info("数据包推送异常", zap.Any("pack id", pack.Id))
		// TODO retry
		return stat
	}
	log.Logger.Info("数据包推送完成", zap.Any("pack id", pack.Id))
	return stat
}

func (c *Demo) getSession() erpc.Session {
	if c.checkSessionStatus() {
		return erpcSession
	} else {
		// 重连
		InitERpcClientByStructMod()
		return erpcSession
	}
}

// 连接状态检测
func (c *Demo) checkSessionStatus() bool {
	if erpcSession == nil {
		erpc.Warnf("old session is null. to renew session")
		return false
	}
	if erpcSession.Health() {
		return true
	} else {
		erpc.Warnf("old session is unHealth. to renew session")
		erpcSession.Close()
		return false
	}
}
