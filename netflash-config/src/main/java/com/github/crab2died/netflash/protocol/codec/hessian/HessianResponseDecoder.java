package com.github.crab2died.netflash.protocol.codec.hessian;

import com.github.crab2died.netflash.protocol.NetflashResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class HessianResponseDecoder extends LengthFieldBasedFrameDecoder {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(HessianResponseDecoder.class);

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
        return HessianCodec.decode(bytes, NetflashResponse.class);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        if (logger.isErrorEnabled()){
            logger.error("Hessian decoder handler process error, " + cause);
        }
    }
}
