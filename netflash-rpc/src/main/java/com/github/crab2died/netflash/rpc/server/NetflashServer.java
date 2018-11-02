package com.github.crab2died.netflash.rpc.server;

import com.github.crab2died.netflash.protocol.codec.hessian.HessianRequestDecoder;
import com.github.crab2died.netflash.protocol.codec.hessian.HessianEncoder;
import com.github.crab2died.netflash.rpc.handler.RequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NetflashServer implements Server {

    private String serverAddress = "127.0.0.1:8200";

    private ChannelFuture future;

    private static ThreadPoolExecutor threadPoolExecutor;

    public NetflashServer() {
    }

    public NetflashServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Override
    public void open() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("decoder", new HessianRequestDecoder(1024 * 1024, 0, 2, 0, 2))
                                    .addLast("encoder", new HessianEncoder())
                                    .addLast("request", new RequestHandler());
                        }
                    });

            String[] str = serverAddress.split(":");

            this.future = server.bind(str[0], Integer.parseInt(str[1])).sync();
            this.future.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void close() {
        // future.channel().close();
    }


    public static void submit(Runnable task) {
        if (threadPoolExecutor == null) {
            synchronized (NetflashServer.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L,
                            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1024 * 1024));
                }
            }
        }
        threadPoolExecutor.submit(task);
    }
}
