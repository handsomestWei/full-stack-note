package log

import (
	"encoding/json"
	"fmt"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
)

var Logger *zap.Logger

func InitLog(level string) {
	if level == "" {
		level = "debug"
	}
	var js string
	js = fmt.Sprintf(`{
      "level": "%s",
      "encoding": "console",
      "outputPaths": ["stdout"],
      "errorOutputPaths": ["stdout"]
      }`, level)

	var cfg zap.Config
	if err := json.Unmarshal([]byte(js), &cfg); err != nil {
		panic(err)
	}
	cfg.EncoderConfig = zap.NewProductionEncoderConfig()
	cfg.EncoderConfig.EncodeTime = zapcore.ISO8601TimeEncoder
	var err error
	Logger, err = cfg.Build()
	if err != nil {
		panic(err)
	}
}
