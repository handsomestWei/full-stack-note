package com.wjy.demo.disruptor.single;

import com.lmax.disruptor.EventHandler;

/**
 * @author weijiayu
 * @date 2022/8/24 15:26
 */
public class LongEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println("Event: " + event);
    }
}