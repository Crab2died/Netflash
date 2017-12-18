package com.github.crab2died.retty.future;

import com.github.crab2died.retty.protocol.RettyResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RettyFuture implements Future<RettyResponse> {

    private String requestId;

    public RettyFuture(String requestId) {
        this.requestId = requestId;
    }

    private static final Map<String, RettyResponse> SYNC_RESPONSE = new HashMap<>();

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException("Unsupported Operation");
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException("Unsupported Operation");
    }

    @Override
    public boolean isDone() {
        return  null != SYNC_RESPONSE.get(this.requestId);
    }

    @Override
    public RettyResponse get() throws InterruptedException, ExecutionException {
        return SYNC_RESPONSE.get(this.requestId);
    }

    @Override
    public RettyResponse get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    public void setResp(RettyResponse resp){
        if (null == resp)
            return;
        SYNC_RESPONSE.put(resp.getRequestId(), resp);
    }

}
