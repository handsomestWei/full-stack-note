package demo

import (
	"github.com/handsomestWei/go-rpc-demo/config"
	"github.com/handsomestWei/go-rpc-demo/dto"
	"github.com/handsomestWei/go-rpc-demo/log"
	"github.com/handsomestWei/go-rpc-demo/rpc"
	"github.com/satori/go.uuid"
	"time"
)

func RunDemo() {

	config.InitConfig("config.properties")
	log.InitLog(config.Conf.LogLevel)

	go runServerDemo()
	go runClientDemo()
	for {
		select {}
	}
}

func runServerDemo() {
	rpc.InitErpcSvcByStructMod()
	for {
		select {}
	}
}

func runClientDemo() {
	rpc.InitERpcClientByStructMod()
	demo := &rpc.Demo{}
	for {
		t := time.NewTimer(30 * time.Second)
		<-t.C
		pack := &dto.DemoPack{
			Id: uuid.NewV4().String(),
			Data: []dto.DemoData{
				dto.DemoData{
					DataTime: time.Now().Format(time.RFC3339),
				}},
		}
		stat := demo.PushDemoData(pack)
		if stat.OK() {
		}
	}
}
