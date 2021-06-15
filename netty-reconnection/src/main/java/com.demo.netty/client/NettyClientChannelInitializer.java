package com.demo.netty.client;

import com.demo.netty.MessageDecoder;
import com.demo.netty.MessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.awt.print.PrinterJob;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Peng Wen
 * @Date: 2021/6/10 17:31
 */
public class NettyClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        /**
         * 注册5秒没有消息包则触发事件
         */
        pipeline.addLast(new IdleStateHandler(0,5,0, TimeUnit.SECONDS));
        /**
         * 1.添加解码处理器
         * 2.添加编码处理器
         * 3.添加客户端业务处理器
         */
        pipeline.addLast(new MessageEncoder());
        pipeline.addLast(new MessageDecoder());
        pipeline.addLast(new NettyClientHandler());


    }
}
