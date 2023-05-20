package com.wjy.learn.rocketmq.producer.interfaces;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

// @Output名与配置文件里的bind名绑定对应。定义消息频道bind和消息主题topic的映射关系
public interface MySource {

    @Output("outputTx")
    MessageChannel outputTx();

    @Output("outputPoll")
    MessageChannel outputPoll();
}
