package com.wjy.learn.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义消费者</br>
 * 配置@KafkaListener(containerFactory = xxx)参考org.springframework.kafka.config.KafkaListenerContainerFactory</br>
 * 配置spring.kafka.listener.xxx</br>
 * 参考org.springframework.kafka.listener.AckMode
 */
@Slf4j
@Component
public class CustomConsumer {

    /**
     * 简单的延迟队列实现：满足条件，消息转移到另一个主题</br>
     */
    @KafkaListener(topics = { "${wjy.learn.kafka.topic}" }, errorHandler = "customErrHandle")
    @SendTo(value = { "${wjy.learn.kafka.delay-topic}" })
    public String consumeMsg(String msg, Acknowledgment ack) {
        if ("delay".equals(msg)) {
            ack.acknowledge();
            // 消息转移到延迟主题
            return msg;
        } else if ("dead".equals(msg)) {
            // 处理失败，没有提交确认，可以sendto到死信队列
            return null;
        } else {
            log.info("consume msg: {}", msg);
            ack.acknowledge();
            return null;
        }
    }

    /**
     * 延迟消费消息
     */
    @SuppressWarnings("static-access")
    @KafkaListener(topics = { "${wjy.learn.kafka.delay-topic}" })
    public String delayConsumeMsg(String msg, Acknowledgment ack) {
        try {
            // 延迟
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.info("consume delay msg: {}", msg);
        ack.acknowledge();
        return msg;
    }

    /**
     * 自动提交</br>
     * 配置spring.kafka.consumer.enable-auto-commit=true</br>
     * 配置ack方式spring.kafka.listener.ack-mode
     */
    public String consumeMsg(String msg) {
        return null;
    }

}
