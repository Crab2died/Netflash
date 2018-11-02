package com.github.crab2died.netflash.proxy;

import com.github.crab2died.netflash.client.ClientUtil;
import com.github.crab2died.netflash.future.NetflashFuture;
import com.github.crab2died.netflash.protocol.NetflashRequest;
import com.github.crab2died.netflash.resp.SynResp;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@SuppressWarnings("all")
public class DefaultNetflashProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class clazz = method.getDeclaringClass();
        if (Object.class == clazz) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                return proxy.getClass().getName() + "@" +
                        Integer.toHexString(System.identityHashCode(proxy)) +
                        ", with InvocationHandler " + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }
        NetflashRequest req = new NetflashRequest();
        req.setRequestId(UUID.randomUUID().toString());
        req.setInterfaceName(clazz.getName());
        req.setMethodName(method.getName());
        req.setMethodType(method.getParameterTypes());
        req.setParameters(args);
        NetflashFuture future = request(req, ClientUtil.getSocketChannel());
        return future.get();
    }

    public NetflashFuture request(NetflashRequest req, SocketChannel socketChannel) throws Throwable {

        final CountDownLatch latch = new CountDownLatch(1);
        NetflashFuture future = new NetflashFuture(req.getRequestId());
        SynResp.SYNC_RESPONSE.put(req.getRequestId(), future);
        socketChannel.writeAndFlush(req).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                latch.countDown();
            }
        });
        latch.await();
        return future;
    }
}
