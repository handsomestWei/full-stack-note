package com.wjy.learn.rocketmq.mqadmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 开启springboot admin服务端
@EnableAdminServer
@SpringBootApplication
public class MqAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqAdminApplication.class, args);
    }

}
