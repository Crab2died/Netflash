package com.github.crab2died.retty.rpc.handler;

import com.github.crab2died.retty.protocol.RettyResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ResponseHandler extends SimpleChannelInboundHandler<RettyResponse> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, RettyResponse resp)
            throws Exception {
    }
}
