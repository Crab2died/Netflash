package com.github.crab2died.netflash.future;

import com.github.crab2died.netflash.protocol.NetflashResponse;

import java.util.concurrent.*;

public class NetflashFuture implements Future<Object> {

    private CountDownLatch latch = new CountDownLatch(1);

    private String requestId;

    private NetflashResponse resp;

    public NetflashFuture(String requestId) {
        this.requestId = requestId;
    }

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
        return null != resp;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        latch.await();
        return this.resp.getResponse();
    }

    @Override
    public Object get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return this.resp;
        }
        return null;
    }

    public void done(NetflashResponse resp) {
        this.resp = resp;
        latch.countDown();
    }

    public String getRequestId() {
        return requestId;
    }

}
