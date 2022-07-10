package com.wjy.demo.protobuf.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wjy.demo.protobuf.dto.OrderProto;
import com.wjy.demo.protobuf.dto.OrderProto.Order;
import com.wjy.demo.protobuf.dto.OrderProto.OrderType;

@Controller
public class SimpleController {

    /**
     * 利用ProtobufHttpMessageConverter转换
     */
    @RequestMapping(value = "/order")
    @ResponseBody
    public Order getOrderProto(@RequestBody Order order) {

        Order od = OrderProto.Order.newBuilder()
                .setId(1L).setUserId(order.getUserId()).setOrderName("testOrder")
                .setOrderType(OrderType.TRADE)
                .putGoods(1, OrderProto.GoodDetail.newBuilder().setGoodName("rice").build())
                .putGoods(2, OrderProto.GoodDetail.newBuilder().setGoodName("potato").build())
                .addRemark("testRemark1").addRemark("testRemark2")
                .build();
        return od;
    }

    /**
     * 读取字节流利用protobuf-java转换
     */
    @RequestMapping(value = "/order2")
    @ResponseBody
    public Order getOrderProto2(HttpServletRequest req) {

        Order reqOd = null;
        try {
            StringBuilder bd = new StringBuilder();
            BufferedReader reader;
            reader = new BufferedReader(
                    new InputStreamReader(req.getInputStream(), Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                bd.append(line);
            }
            reqOd = Order.parseFrom(bd.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Order od = OrderProto.Order.newBuilder()
                .setId(1L).setUserId(reqOd.getUserId()).setOrderName("testOrder")
                .setOrderType(OrderType.TRADE)
                .putGoods(1, OrderProto.GoodDetail.newBuilder().setGoodName("rice").build())
                .putGoods(2, OrderProto.GoodDetail.newBuilder().setGoodName("potato").build())
                .addRemark("testRemark1").addRemark("testRemark2")
                .build();
        return od;
    }

}
