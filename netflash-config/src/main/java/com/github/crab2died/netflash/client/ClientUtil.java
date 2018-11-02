package com.github.crab2died.netflash.client;

import io.netty.channel.socket.SocketChannel;

public class ClientUtil {

    private volatile static SocketChannel socketChannel;

    public static SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public static void setSocketChannel(SocketChannel socketChannel) {
        ClientUtil.socketChannel = socketChannel;
    }
}
