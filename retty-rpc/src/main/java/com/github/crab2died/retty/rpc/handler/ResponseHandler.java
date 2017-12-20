package com.github.crab2died.retty.rpc.handler;

import com.github.crab2died.retty.future.RettyFuture;
import com.github.crab2died.retty.protocol.RettyRequest;
import com.github.crab2died.retty.protocol.RettyResponse;
import com.github.crab2died.retty.resp.SynResp;
import io.netty.channel.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class ResponseHandler extends SimpleChannelInboundHandler<RettyResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RettyResponse resp) throws
            Exception {

        System.out.println(resp);
        String reqId = resp.getRequestId();
        RettyFuture future = SynResp.SYNC_RESPONSE.get(reqId);
        if (future != null) {
            SynResp.SYNC_RESPONSE.remove(reqId);
            future.done(resp);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    public void set(RettyFuture future) {
        SynResp.SYNC_RESPONSE.put(future.getRequestId(), future);
    }

}
