package com.wjy.learn.kafka.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wjy.learn.kafka.service.CustomConsumer;
import com.wjy.learn.kafka.service.CustomProducer;

/**
 * 通过http请求发送消息到kafka
 */
@RestController
public class KafkaController {

    @SuppressWarnings("unused")
    private final CustomConsumer c;
    private final CustomProducer p;

    KafkaController(CustomConsumer c, CustomProducer p) {
        // 放在控制器构造函数内初始化，程序未启动完成时会提示连接kafka超时
        this.c = c;
        this.p = p;
    }

    @RequestMapping("/produce")
    public boolean produceMsg(@RequestParam String msg) {
        try {
            return p.send(msg);
            // return p.executeNonTransactional(msg);
            // return p.executeAnotationTransactional(msg);
            // return p.executeInTransactional(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
