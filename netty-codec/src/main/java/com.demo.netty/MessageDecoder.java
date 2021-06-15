package com.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author: Peng Wen
 * @Date: 2021/6/8 18:37
 */

public class MessageDecoder extends ByteToMessageDecoder {

    /**
     * len 消息的长度
     * FIXED_INTEGER_LENGTH int类型 4字节长度值
     * FIXED_START_IDX 开始位置
     */
    private int len;
    private static final Integer FIXED_INTEGER_LENGTH=4;
    private static final Integer FIXED_START_IDX=0;
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if(in.readableBytes()>=FIXED_INTEGER_LENGTH){
            if(len==FIXED_START_IDX){
                len=in.readInt();
            }
            /**
             * 判断可以读取的长度是否符合消息头的长度值
             * 如果小于len，证明当前的消息被拆包了，直接返回，等待下个buf在读取
             */
            if(in.readableBytes()<len){
                return;
            }
            /**
             * 判断当前可以读的buf是否>=len，如果是则可以读到当前的消息
             * 这个可读的buf可能包含了不止当前的消息，所以一定要>=来判断
             */
            byte[] content = new byte[len];
            if(in.readableBytes()>=len){
                /**
                 * 读取当前消息，并且封装成Model实体类
                 * 将节码的消息添加到list，继续执行pipeline的处理器
                 * 并且将len置为0
                 */
                in.readBytes(content);
                MessageModel messageModel = new MessageModel()
                        .setContent(content).setLength(len);
                out.add(messageModel);
            }
            len=0;
        }
    }
}
