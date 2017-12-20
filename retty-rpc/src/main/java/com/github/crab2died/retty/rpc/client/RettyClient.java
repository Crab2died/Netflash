package com.github.crab2died.retty.rpc.client;

import com.github.crab2died.retty.client.ClientUtil;
import com.github.crab2died.retty.protocol.codec.hessian.HessianEncoder;
import com.github.crab2died.retty.protocol.codec.hessian.HessianResponseDecoder;
import com.github.crab2died.retty.rpc.handler.ResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class RettyClient implements Client {

    private String serverAddress = "127.0.0.1:8200";

    public RettyClient(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Override
    public void connect() throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap client = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("decoder", new HessianResponseDecoder(1024 * 1024, 0, 2, 0, 2))
                                    .addLast("encoder", new HessianEncoder())
                                    .addLast("response", new ResponseHandler());
                        }
                    });
            String[] str = this.serverAddress.split(":");
            ChannelFuture future = client.connect(str[0], Integer.parseInt(str[1])).sync();
            if (future.isSuccess()) {
                System.out.println("client 启动成功");
                ClientUtil.setSocketChannel((SocketChannel)future.channel());
            }
            future.channel().closeFuture().sync();
        } finally {
             group.shutdownGracefully();
        }
    }

    @Override
    public void disConnect() {
        //
    }

}
