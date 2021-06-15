package com.demo.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Peng Wen
 * @Date: 2021/6/7 18:52
 */
public class NettyClient {

    private static final String HOST="127.0.0.1";
    private static final int PORT=9999;
    private static final EventLoopGroup EVENT_LOOP_GROUP = new NioEventLoopGroup();
    public static void main(String[] args) {
        new NettyClient().start();
    }

    /**
     * 启动服务，创建连接
     */
    void start(){
        connect(new Bootstrap(),EVENT_LOOP_GROUP);
    }

    void connect(Bootstrap bootstrap, EventLoopGroup eventExecutors){
        try {
            /**
             * 绑定线程池
             */
            bootstrap.group(eventExecutors);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new NettyClientChannelInitializer());
            bootstrap.remoteAddress(HOST,PORT);
            ChannelFuture cf = bootstrap.connect().addListener((ChannelFuture channelFuture) -> {
                final EventLoop eventLoop = channelFuture.channel().eventLoop();
                if (!channelFuture.isSuccess()) {
                    System.err.println("通讯连接已断开！5秒后尝试重新连接");
                    eventLoop.schedule(() -> connect(new Bootstrap(), eventLoop), 5, TimeUnit.SECONDS);
                }else {
                    System.out.println("start success!");
                }
            });

            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //EVENT_LOOP_GROUP.shutdownGracefully();
        }
    }

}
