package com.wjy.demo.disruptor.multi;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.lmax.disruptor.WorkHandler;

/**
 * @author weijiayu
 * @date 2022/8/24 15:02
 */
public class Consumer implements WorkHandler<Order> {

    private String consumerId;

    private static AtomicInteger count = new AtomicInteger(0);

    private Random random = new Random();

    public Consumer(String consumerId) {
        this.consumerId = consumerId;
    }

    @Override
    public void onEvent(Order event) throws Exception {
        Thread.sleep(1 * random.nextInt(5));
        // if ("C1".equals(consumerId)) {
        // Thread.sleep(100 * 1000);
        // }
        System.err.println("当前消费者: " + this.consumerId + ", 消费信息ID: " + event.getId());
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}
