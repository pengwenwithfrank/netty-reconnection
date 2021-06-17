package com.demo.netty.nettywebsocket;

import com.demo.netty.nettywebsocket.websocket.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Pengwen
 */
@SpringBootApplication
public class NettyWebsocketApplication {

    public static void main(String[] args) {

        SpringApplication.run(NettyWebsocketApplication.class, args);
        new NettyServer().start();;
    }

}
