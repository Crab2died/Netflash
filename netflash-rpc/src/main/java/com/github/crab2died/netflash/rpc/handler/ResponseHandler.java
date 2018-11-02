package com.github.crab2died.netflash.rpc.handler;

import com.github.crab2died.netflash.future.NetflashFuture;
import com.github.crab2died.netflash.protocol.NetflashResponse;
import com.github.crab2died.netflash.resp.SynResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ResponseHandler extends SimpleChannelInboundHandler<NetflashResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NetflashResponse resp) throws
            Exception {

        System.out.println(resp);
        String reqId = resp.getRequestId();
        NetflashFuture future = SynResp.SYNC_RESPONSE.get(reqId);
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

    public void set(NetflashFuture future) {
        SynResp.SYNC_RESPONSE.put(future.getRequestId(), future);
    }

}
