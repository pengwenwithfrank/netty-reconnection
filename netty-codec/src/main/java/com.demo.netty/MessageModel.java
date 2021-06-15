package com.demo.netty;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Peng Wen
 * @Date: 2021/6/8 18:33
 */
@Data
@Accessors(chain = true)
public class MessageModel {
    private int length;
    private byte[] content;
}
