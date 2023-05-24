package com.wjy.learn.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaOperations.OperationsCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wjy.learn.kafka.config.CommonProperties;

/**
 * 自定义生产者</br>
 * 配置KafkaTemplate
 */
@Component
public class CustomProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTpl;
    @Autowired
    private CommonProperties pro;

    /**
     * 发送消息</br>
     * 注释配置spring.kafka.producer.transaction-id-prefix关闭事务
     */
    public boolean send(String msg) throws Exception {
        // 同步阻塞
        SendResult<String, Object> result = kafkaTpl.send(pro.getTopic(), pro.getPartitionKey(), msg).get();
        if (result.getRecordMetadata() != null) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * 发送消息：发生异常，未开启事务，消息被消费</br>
     * 注释配置spring.kafka.producer.transaction-id-prefix关闭事务
     */
    public boolean executeNonTransactional(String msg) throws Exception {
        kafkaTpl.send(pro.getTopic(), pro.getPartitionKey(), msg).get();
        throw new RuntimeException("non Transactional");
    }

    /***
     * 发送消息：发生异常，消息未提交</br>
     * 配置spring.kafka.producer.transaction-id-prefix开启事务</br>
     * 使用@Transactional注解</br>
     * 配置事务隔离级别spring.kafka.consumer.isolation-level=read-committed
     */
    @Transactional
    public boolean executeAnotationTransactional(String msg) throws Exception {
        kafkaTpl.send(pro.getTopic(), pro.getPartitionKey(), msg).get();
        throw new RuntimeException("@Transactional");
    }

    /***
     * 发送消息：发生异常，消息未提交</br>
     * 配置spring.kafka.producer.transaction-id-prefix开启事务</br>
     * 使用executeInTransaction方法</br>
     * 配置事务隔离级别spring.kafka.consumer.isolation-level=read-committed
     */
    public boolean executeInTransactional(String msg) throws Exception {
        return kafkaTpl.executeInTransaction(new OperationsCallback<String, Object, Boolean>() {

            @Override
            public Boolean doInOperations(KafkaOperations<String, Object> operations) {
                try {
                    kafkaTpl.send(pro.getTopic(), pro.getPartitionKey(), msg).get();
                    kafkaTpl.send(pro.getTopic(), pro.getPartitionKey(), msg).get();
                    // 发生异常，消息回滚
                    @SuppressWarnings("unused")
                    int i = 1 / 0;
                } catch (Exception e) {
                    throw new RuntimeException("executeInTransaction");
                }
                return false;
            }
        });

    }

}
