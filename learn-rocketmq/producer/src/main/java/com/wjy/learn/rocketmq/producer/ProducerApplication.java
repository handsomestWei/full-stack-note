package com.wjy.learn.rocketmq.producer;

import com.wjy.learn.rocketmq.producer.interfaces.MySource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication(scanBasePackages = { "com.wjy.learn.rocketmq.producer" })
@MapperScan("com.wjy.learn.rocketmq.producer.dao")
// 绑定频道
@EnableBinding({ MySource.class })
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

}
