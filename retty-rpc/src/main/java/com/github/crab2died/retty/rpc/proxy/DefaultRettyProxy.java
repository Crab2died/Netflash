package com.github.crab2died.retty.rpc.proxy;

import com.github.crab2died.retty.future.RettyFuture;
import com.github.crab2died.retty.protocol.RettyRequest;
import com.github.crab2died.retty.rpc.client.RettyClient;
import com.github.crab2died.retty.rpc.handler.ResponseHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@SuppressWarnings("all")
public class DefaultRettyProxy implements InvocationHandler {

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
        RettyRequest req = new RettyRequest();
        req.setRequestId(UUID.randomUUID().toString());
        req.setInterfaceName(clazz.getName());
        req.setMethodName(method.getName());
        req.setMethodType(method.getParameterTypes());
        req.setParameters(args);
        RettyFuture future = request(req, RettyClient.getSocketChannel());
        return future.get();
    }

    public RettyFuture request(RettyRequest req, SocketChannel socketChannel) {

        final CountDownLatch latch = new CountDownLatch(1);
        RettyFuture future = new RettyFuture(req.getRequestId());
        ResponseHandler.SYNC_RESPONSE.put(req.getRequestId(), future);
        socketChannel.writeAndFlush(req).addListener(new ChannelFutureListener() {
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
