package com.github.crab2died.retty.rpc.handler;

import com.github.crab2died.retty.future.RettyFuture;
import com.github.crab2died.retty.protocol.RettyRequest;
import com.github.crab2died.retty.protocol.RettyResponse;
import io.netty.channel.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ResponseHandler extends SimpleChannelInboundHandler<RettyResponse> {

    private Channel channel;

    private static final Map<String, RettyFuture> SYNC_RESPONSE = new HashMap<>();


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, RettyResponse resp)
            throws Exception {

        String reqId = resp.getRequestId();
        RettyFuture future = SYNC_RESPONSE.get(reqId);
        if (future != null) {
            SYNC_RESPONSE.remove(reqId);
            future.done(resp);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    public void set(RettyFuture future) {

        SYNC_RESPONSE.put(future.getRequestId(), future);
    }

    public RettyFuture request(RettyRequest req) {

        final CountDownLatch latch = new CountDownLatch(1);
        RettyFuture future = new RettyFuture(req.getRequestId());
        SYNC_RESPONSE.put(req.getRequestId(), future);
        channel.writeAndFlush(req).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;

    }

}
