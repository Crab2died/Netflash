package com.github.crab2died.retty.rpc.handler;

import com.github.crab2died.retty.protocol.RettyRequest;
import com.github.crab2died.retty.protocol.RettyResponse;
import com.github.crab2died.retty.rpc.proxy.MethodInvoke;
import com.github.crab2died.retty.rpc.server.RettyServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RequestHandler extends SimpleChannelInboundHandler<RettyRequest> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, RettyRequest req) throws Exception {

        System.out.println(req);
        RettyServer.submit(new Runnable() {
            @Override
            public void run() {
                RettyResponse resp = MethodInvoke.invoke(req);
                ctx.writeAndFlush(resp).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        System.out.println("请求:" + req + " => " + resp);
                    }
                });
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
