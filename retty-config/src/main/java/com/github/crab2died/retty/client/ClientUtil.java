package com.github.crab2died.retty.client;

import io.netty.channel.socket.SocketChannel;

public class ClientUtil {

    private static SocketChannel socketChannel;

    public static SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public static void setSocketChannel(SocketChannel socketChannel) {
        ClientUtil.socketChannel = socketChannel;
    }
}
