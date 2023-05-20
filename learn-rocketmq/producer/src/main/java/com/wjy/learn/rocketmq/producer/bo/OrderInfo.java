package com.wjy.learn.rocketmq.producer.bo;

import lombok.Data;

import java.util.Date;

@Data
public class OrderInfo {

    private Long orderId;
    private Integer userId;
    private String tradeSn;
    private String tradeType = "01";
    private String tradeStatus = "00";
    private Date createTime = new Date();
}
