package com.github.crab2died.retty.protocol.codec.hessian;

import com.github.crab2died.retty.protocol.RettyRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class HessianEncoder extends MessageToMessageEncoder<Object> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Object req, List<Object> out)
            throws Exception {

        ByteBuf buf = Unpooled.buffer();
        byte[] reqBytes = HessianCodec.encode(req);
        buf.writeInt(reqBytes.length);
        buf.writeBytes(reqBytes);
        out.add(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        ctx.close();
        cause.printStackTrace();
    }
}
