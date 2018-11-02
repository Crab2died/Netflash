package com.github.crab2died.netflash.rpc.client;

import com.github.crab2died.netflash.rpc.handler.ResponseHandler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class ClientFuture {

    private static final Queue<ResponseHandler> CLIENT_HANDLER = new ConcurrentLinkedQueue<>();

    public synchronized static void set(ResponseHandler responseHandler) {
        CLIENT_HANDLER.add(responseHandler);
    }

    public synchronized static ResponseHandler get() {
        ResponseHandler handler = CLIENT_HANDLER.poll();
        while (null == handler){
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return handler;
    }

    public synchronized static void release(ResponseHandler handler){
        CLIENT_HANDLER.add(handler);
    }
}
