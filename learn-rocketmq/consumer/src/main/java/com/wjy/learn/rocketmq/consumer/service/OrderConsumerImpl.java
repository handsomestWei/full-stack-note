package com.wjy.learn.rocketmq.consumer.service;

import com.wjy.learn.rocketmq.consumer.interfaces.MySink;
import com.wjy.learn.rocketmq.consumer.vo.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderConsumerImpl implements CommandLineRunner {

    @Autowired
    private MySink mySink;

    @StreamListener("inputTx")
    public void receiveInputTx(@Payload OrderInfo od) {
        // 接收消息推送
        System.out.printf("inputTx receive 【%s】\n", od.toString());
    }

    // 容器启动完成时执行
    @Override
    public void run(String... args) throws Exception {
        while (true) {
            // 定时主动拉取消息
            mySink.inputPoll().poll(m -> {
                if (m != null) {
                    String payload = (String) m.getPayload();
                    System.out.printf("pull msg 【%s】\n", payload);
                }
            }, new ParameterizedTypeReference<String>() {
            });
            Thread.sleep(5 * 1000);
        }
    }
}
