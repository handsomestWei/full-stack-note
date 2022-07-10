package com.wjy.demo.netty.server;

import com.wjy.demo.netty.handler.SimpleSharabledChannelHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SimpleEchoServer {

    private String ipAddr;
    private int port;

    public SimpleEchoServer(String ipAddr, int port) {
        this.ipAddr = ipAddr;
        this.port = port;
    }

    public void run() {
        EventLoopGroup parentGroup = null;
        EventLoopGroup childGroup = null;
        try {
            parentGroup = new NioEventLoopGroup(1);
            childGroup = new NioEventLoopGroup(2);

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 自定义处理类
                            ch.pipeline().addLast(new SimpleSharabledChannelHandler());
                        }
                    });

            ChannelFuture cf = bootstrap.bind(ipAddr, port).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (parentGroup != null) {
                parentGroup.shutdownGracefully();
            }
            if (childGroup != null) {
                childGroup.shutdownGracefully();
            }
        }
    }

}
