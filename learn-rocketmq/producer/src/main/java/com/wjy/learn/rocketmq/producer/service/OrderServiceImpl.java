package com.wjy.learn.rocketmq.producer.service;

import com.wjy.learn.rocketmq.producer.interfaces.IOrderService;
import com.wjy.learn.rocketmq.producer.interfaces.MySource;
import com.wjy.learn.rocketmq.producer.vo.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private MySource mySource;

    @Override
    public void sendTransactionMsg(OrderInfo entity) {
        Message message = MessageBuilder.withPayload(entity)
                .setHeader(MessageConst.PROPERTY_TAGS, "tag")
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();
        mySource.outputTx().send(message);
    }

    @Override
    public void sendMsg(OrderInfo entity) {
        Message message = MessageBuilder.withPayload(entity)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.TEXT_PLAIN)
                .build();
        mySource.outputPoll().send(message);
    }
}
