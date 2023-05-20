package com.wjy.learn.rocketmq.producer.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderInfo {

    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("tradeSn")
    private String tradeSn;
}
