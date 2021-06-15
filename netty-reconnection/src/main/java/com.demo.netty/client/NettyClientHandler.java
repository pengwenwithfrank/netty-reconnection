package com.demo.netty.client;

import com.demo.netty.MessageModel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static com.demo.netty.MessageHeartBeat.HEARTBEAT;

/**
 * @Author: Peng Wen
 * @Date: 2021/6/10 15:53
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<MessageModel> {
    /**
     *  创建连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("create connect"+new Timestamp(System.currentTimeMillis()).toString());
        ctx.fireChannelActive();
    }

    /**
     * 关闭连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务器关闭了连接:"+new Timestamp(System.currentTimeMillis()).toString());
        System.err.println("客户端尝试重连");
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(() -> new NettyClient().connect(new Bootstrap(), eventLoop), 5, TimeUnit.SECONDS);

    }

    /**
     * 客户端心跳事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        /**
         * 如果是空闲事件
         */
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            /**
             * 如果write通道没有有效的数据包发送，发送心跳包，
             */
            if(IdleState.WRITER_IDLE.equals(idleStateEvent.state())){
                MessageModel sendMsg = new MessageModel();
                sendMsg.setLength(HEARTBEAT.length);
                sendMsg.setContent(HEARTBEAT);
                ctx.channel().writeAndFlush(sendMsg);
            }
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageModel messageModel) throws Exception {
        String msg = new String(messageModel.getContent());
        System.out.println("服务端响应数据："+msg);

    }
}
