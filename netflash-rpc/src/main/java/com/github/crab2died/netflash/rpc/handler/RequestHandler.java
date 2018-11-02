package com.github.crab2died.netflash.rpc.handler;

import com.github.crab2died.netflash.protocol.NetflashRequest;
import com.github.crab2died.netflash.protocol.NetflashResponse;
import com.github.crab2died.netflash.proxy.MethodInvoke;
import com.github.crab2died.netflash.rpc.server.NetflashServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RequestHandler extends SimpleChannelInboundHandler<NetflashRequest> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NetflashRequest req) throws
            Exception {

        System.out.println(req);
//        netflashResponse resp = new netflashResponse();
//        resp.setRequestId(req.getRequestId());
//        ctx.channel().writeAndFlush(resp);
        NetflashServer.submit(new Runnable() {
            @Override
            public void run() {
                NetflashResponse resp = MethodInvoke.invoke(req);
                resp.setRequestId(req.getRequestId());
                ctx.channel().writeAndFlush(resp).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        System.out.println("请求:" + req + " => " + resp);
                    }
                });
            }
        });
    }
}
