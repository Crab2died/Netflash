package com.github.crab2died.retty.rpc.client;

import com.github.crab2died.retty.rpc.handler.ResponseHandler;

import java.util.concurrent.CopyOnWriteArrayList;

public class ClientFuture {

    private static final CopyOnWriteArrayList<ResponseHandler> CLIENT_HANDLER = new CopyOnWriteArrayList<>();

    public static void set(ResponseHandler responseHandler) {
        CLIENT_HANDLER.add(responseHandler);
    }

    public static ResponseHandler get() {
        return CLIENT_HANDLER.get(0);
    }
}
