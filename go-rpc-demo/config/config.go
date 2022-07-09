package config

import (
	"encoding/json"
	"os"
)

type Config struct {
	LogLevel            string `json:"log_level"`
	RedialInterval      int    `json:"redial_interval"`
	HeartPingRateSecond int    `json:"heart_ping_rate_second"`
	CliDialAddr         string `json:"cli_dial_addr"`
	BearerToken         string `json:"bearer_token"`
	Meta                string `json:"meta"`
	SvcListenPort       int    `json:"svc_listen_port"`
	BroadcastRateSecond int    `json:"broadcast_rate_second"`
}

var Conf *Config

func InitConfig(configFilePath string) {
	if configFilePath == "" {
		configFilePath = os.Getenv(configFilePath)
	}
	file, err := os.Open(configFilePath)
	if err != nil {
		panic(err)
	}
	defer file.Close()

	Conf = &Config{}
	err = json.NewDecoder(file).Decode(&Conf)
	if err != nil {
		panic(err)
	}
}
