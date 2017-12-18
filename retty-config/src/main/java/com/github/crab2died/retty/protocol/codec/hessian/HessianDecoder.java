package com.github.crab2died.retty.protocol.codec.hessian;

import com.github.crab2died.retty.protocol.RettyRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class HessianDecoder extends LengthFieldBasedFrameDecoder {

    public HessianDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                          int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (null == frame || frame.readableBytes() <= 0) return null;
        int len = in.readableBytes();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        return HessianCodec.decode(bytes, RettyRequest.class);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        ctx.close();
        cause.printStackTrace();
    }
}
