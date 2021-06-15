package com.demo.netty.server;

import com.demo.netty.MessageDecoder;
import com.demo.netty.MessageEncoder;
import com.demo.netty.client.NettyClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Peng Wen
 * @Date: 2021/6/15 17:21
 */
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        /**
         * 注册读空闲事件，5秒触发一次
         */
        pipeline.addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
        /**
         * 1.添加解码处理器
         * 2.添加编码处理器
         * 3.添加服务端端业务处理器
         */
        pipeline.addLast(new MessageEncoder());
        pipeline.addLast(new MessageDecoder());
        pipeline.addLast(new NettyServerHandler());
    }
}
