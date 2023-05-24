package com.wjy.learn.kafka.err;

import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomKafkaListenerErrorHandler implements KafkaListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        log.info("handleError msg: {}", message.getPayload());
        log.error(exception.getMessage());
        return null;
    }

}
