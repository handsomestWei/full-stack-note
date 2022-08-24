package com.wjy.demo.disruptor.single;

import com.lmax.disruptor.EventFactory;

/**
 * @author weijiayu
 * @date 2022/8/24 15:26
 */
public class LongEventFactory implements EventFactory<LongEvent> {

    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}