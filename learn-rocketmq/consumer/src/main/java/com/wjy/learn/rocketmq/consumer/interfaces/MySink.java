package com.wjy.learn.rocketmq.consumer.interfaces;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.messaging.SubscribableChannel;

// @Input名与配置文件里的bind名绑定对应。定义消息频道bind和消息主题topic的映射关系
public interface MySink {

    @Input("inputTx")
    SubscribableChannel inputTx();

    @Input("inputPoll")
    PollableMessageSource inputPoll();
}
