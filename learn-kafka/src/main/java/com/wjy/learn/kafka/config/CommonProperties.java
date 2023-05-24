package com.wjy.learn.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "wjy.learn.kafka")
public class CommonProperties {

    private String topic;
    private String delayTopic;

    private int numPartitions;
    private short replicationFactor;
    private String partitionKey;

}
