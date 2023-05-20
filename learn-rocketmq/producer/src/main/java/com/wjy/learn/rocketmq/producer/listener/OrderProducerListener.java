package com.wjy.learn.rocketmq.producer.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wjy.learn.rocketmq.producer.bo.OrderInfo;
import com.wjy.learn.rocketmq.producer.dao.OrderInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

import javax.annotation.Resource;

// 监听指定组，配置在spring.cloud.stream.rocketmq.bindings.outputTx.producer.group=transaction-group
@RocketMQTransactionListener(txProducerGroup = "transaction-group", corePoolSize = 5, maximumPoolSize = 10)
@Slf4j
public class OrderProducerListener implements RocketMQLocalTransactionListener {

    @Resource
    private OrderInfoDao od;

    // 事务消息执行
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            com.wjy.learn.rocketmq.producer.vo.OrderInfo obj = JSON.parseObject(new String((byte[]) message.getPayload()),
                    com.wjy.learn.rocketmq.producer.vo.OrderInfo.class);
            OrderInfo entity = new OrderInfo();
            entity.setUserId(obj.getUserId());
            entity.setTradeSn(obj.getTradeSn());
            if (od.insert(entity) > 0) {
//                return RocketMQLocalTransactionState.COMMIT;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
//            return RocketMQLocalTransactionState.ROLLBACK;
        }
        // 事务消息的执行结果未明确，消息的消费者会回查结果
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    // 事务消息回查。消费者端调用
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        log.info("check transaction");
        com.wjy.learn.rocketmq.producer.vo.OrderInfo oi = JSON.parseObject(new String((byte[]) message.getPayload()),
                com.wjy.learn.rocketmq.producer.vo.OrderInfo.class);
        OrderInfo entity = od.selectOne(new QueryWrapper<OrderInfo>()
                .eq("trade_sn", oi.getTradeSn()));
        if (entity != null) {
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }
}
