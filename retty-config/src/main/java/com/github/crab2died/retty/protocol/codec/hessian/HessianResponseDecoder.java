package com.github.crab2died.retty.protocol.codec.hessian;

import com.github.crab2died.retty.protocol.RettyResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class HessianResponseDecoder extends LengthFieldBasedFrameDecoder {

    public HessianResponseDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                                  int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (null == frame || frame.readableBytes() <= 0) return null;
        int len = frame.readableBytes();
        byte[] bytes = new byte[len];
        frame.readBytes(bytes);
        return HessianCodec.decode(bytes, RettyResponse.class);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
