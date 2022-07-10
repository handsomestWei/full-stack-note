package com.wjy.demo.protobuf;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wjy.demo.protobuf.dto.OrderProto;
import com.wjy.demo.protobuf.dto.OrderProto.Order;
import com.wjy.demo.protobuf.util.HttpHelper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = ProtobufDemoApplication.class) // 启动tomcat容器
class LearnProtobufApplicationTests {

    @Test
    public void test1() {
        try {
            String uri = "http://127.0.0.1:8086/demo/protobuf/order";
            byte[] content = OrderProto.Order.newBuilder().setUserId(2L).build().toByteArray();
            String contentType = "application/x-protobuf";

            Order rspOrder = Order.parseFrom(HttpHelper.doPost(uri, content, contentType).getEntity().getContent());

            System.out.println(rspOrder.toString());
            Assert.assertEquals(2L, rspOrder.getUserId());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void test2() {
        try {
            String uri = "http://127.0.0.1:8086/demo/protobuf/order2";
            byte[] content = OrderProto.Order.newBuilder().setUserId(3L).build().toByteArray();
            String contentType = "application/x-protobuf";

            Order rspOrder = Order.parseFrom(HttpHelper.doPost(uri, content, contentType).getEntity().getContent());

            System.out.println(rspOrder.toString());
            Assert.assertEquals(3L, rspOrder.getUserId());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
