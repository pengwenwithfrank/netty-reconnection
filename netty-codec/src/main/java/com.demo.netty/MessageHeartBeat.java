package com.demo.netty;

import io.netty.util.CharsetUtil;

/**
 * @Author: Peng Wen
 * @Date: 2021/6/15 16:10
 */
public class MessageHeartBeat {
    public static final byte[] HEARTBEAT= "HEARTBEAT".getBytes(CharsetUtil.UTF_8);
}
