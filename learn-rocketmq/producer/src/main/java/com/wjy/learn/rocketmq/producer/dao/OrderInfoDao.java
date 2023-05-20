package com.wjy.learn.rocketmq.producer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjy.learn.rocketmq.producer.bo.OrderInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInfoDao extends BaseMapper<OrderInfo> {
}