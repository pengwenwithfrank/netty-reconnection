package com.demo.netty.server;

import com.demo.netty.MessageModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.util.Arrays;

import static com.demo.netty.MessageHeartBeat.HEARTBEAT;

/**
 * @Author: Peng Wen
 * @Date: 2021/6/15 16:08
 */
public class NettyServerHandler  extends SimpleChannelInboundHandler<MessageModel> {
    /**
     * 超时次数
     */
    private int timeOutCount;
    /**
     * 最大连续超时次数
     */
    private static final int MAX_TIME_OUT_COUNT=3;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        /**
         * 当前事件类型，只针对读空闲事件做处理
         */
        IdleStateEvent event  = (IdleStateEvent)evt;
        if(event.state() == IdleState.READER_IDLE){
            timeOutCount++;
        }
        /**
         * 连接超过3次超时没有发送心跳包,关闭当前连接，释放内存资源
         */
        if(timeOutCount>MAX_TIME_OUT_COUNT){
            System.out.println(" [server]读空闲超过3次，关闭连接，释放更多资源");
            ctx.channel().close();
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageModel msg) throws Exception {
        String m = new String(msg.getContent(), CharsetUtil.UTF_8);

        /**
         * 超时计数器置为0
         */
        timeOutCount=0;

        if(Arrays.equals(msg.getContent(),HEARTBEAT)){
            System.out.println("收到客户端心跳"+ctx.channel().remoteAddress());
            MessageModel model = new MessageModel()
                    .setLength(HEARTBEAT.length)
                    .setContent(HEARTBEAT);
            ctx.writeAndFlush(model);
        }else{
            System.out.println("收到客户端业务消息："+m);
        }
    }
}
