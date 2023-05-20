package com.wjy.learn.rocketmq.producer.controller;

import com.wjy.learn.rocketmq.producer.interfaces.IOrderService;
import com.wjy.learn.rocketmq.producer.vo.OrderInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/v1/order", produces = { "application/json;charset=UTF-8" })
public class OrderController {

    @Resource
    private IOrderService od;

    @PostMapping(value = "/tx")
    public void handleTxOrder(@RequestBody OrderInfo odInfo) {
        // 发送事务消息
        od.sendTransactionMsg(odInfo);
    }

    @PostMapping(value = "/default")
    public void handleOrder(@RequestBody OrderInfo odInfo) {
        // 发送普通消息
        od.sendMsg(odInfo);
    }
}
