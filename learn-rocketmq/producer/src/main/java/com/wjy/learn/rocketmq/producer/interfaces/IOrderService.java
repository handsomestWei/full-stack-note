package com.wjy.learn.rocketmq.producer.interfaces;

import com.wjy.learn.rocketmq.producer.vo.OrderInfo;

public interface IOrderService {

    void sendTransactionMsg(OrderInfo entity);

    void sendMsg(OrderInfo entity);
}
