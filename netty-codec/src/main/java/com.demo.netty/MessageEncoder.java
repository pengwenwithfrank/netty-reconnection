package com.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: Peng Wen
 * @Date: 2021/6/8 18:35
 */

public class MessageEncoder extends MessageToByteEncoder<MessageModel> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageModel msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getContent());
    }
}
