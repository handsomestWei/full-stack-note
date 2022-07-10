package com.wjy.demo.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

@Sharable
public class SimpleSharabledChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf in = (ByteBuf) msg;
            System.out.print(in.toString(CharsetUtil.UTF_8));
            // ctx.write(in);
        } finally {
            // 抛弃收到的数据
            ReferenceCountUtil.release(msg);
        }
    }

}
