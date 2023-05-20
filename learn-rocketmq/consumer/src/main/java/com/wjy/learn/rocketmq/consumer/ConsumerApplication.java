package com.wjy.learn.rocketmq.consumer;

import com.wjy.learn.rocketmq.consumer.interfaces.MySink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
// 绑定频道
@EnableBinding({ MySink.class })
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
