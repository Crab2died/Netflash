package com.github.crab2died.retty.rpc.handler;

import com.github.crab2died.retty.protocol.RettyRequest;
import com.github.crab2died.retty.protocol.RettyResponse;
import com.github.crab2died.retty.proxy.MethodInvoke;
import com.github.crab2died.retty.rpc.server.RettyServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RequestHandler extends SimpleChannelInboundHandler<RettyRequest> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RettyRequest req) throws
            Exception {

        System.out.println(req);
//        RettyResponse resp = new RettyResponse();
//        resp.setRequestId(req.getRequestId());
//        ctx.channel().writeAndFlush(resp);
        RettyServer.submit(new Runnable() {
            @Override
            public void run() {
                RettyResponse resp = MethodInvoke.invoke(req);
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
