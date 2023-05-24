package com.wjy.learn.kafka;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = LearnKafkaApplication.class)
@EmbeddedKafka(count = 1, ports = { 9092 }) // 模拟kafka环境
class LearnKafkaApplicationTests {

    @Test
    public void test1() {

    }

}
