package com.demo.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author: Peng Wen
 * @Date: 2021/6/7 18:53
 */
public class NettyServer {


    private static final int PORT=9999;

    public static void main(String[] args) {

        /**
         * 1 创建处理连接器线程池
         * 2 创建处理任务线程池
         */
        EventLoopGroup parentGroup =  new NioEventLoopGroup(1);
        EventLoopGroup childrenGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.group(parentGroup,childrenGroup);
            bootstrap.childHandler(new NettyServerChannelInitializer());
            ChannelFuture channelFuture = bootstrap.bind(PORT).sync();
            System.out.println("Netty server start success!");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childrenGroup.shutdownGracefully();
        }

    }
}
