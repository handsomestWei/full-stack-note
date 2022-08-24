package com.wjy.demo.disruptor.single;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;

/**
 * @author weijiayu
 * @date 2022/8/24 15:33
 */
public class LongEventProducer {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb) {
        // Grab the next sequence
        long sequence = ringBuffer.next();
        try {
            // Get the entry in the Disruptor for that sequence
            LongEvent event = ringBuffer.get(sequence);
            // Fill the entry with data
            event.set(bb.getLong(0));
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
