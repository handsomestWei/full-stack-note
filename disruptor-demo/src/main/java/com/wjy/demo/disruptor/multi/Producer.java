package com.wjy.demo.disruptor.multi;

import com.lmax.disruptor.RingBuffer;

/**
 * @author weijiayu
 * @date 2022/8/24 15:01
 */
public class Producer {

    private RingBuffer<Order> ringBuffer;

    public Producer(RingBuffer<Order> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(String uuid) {
        long sequence = ringBuffer.next();
        try {
            Order order = ringBuffer.get(sequence);
            order.setId(uuid);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

}
