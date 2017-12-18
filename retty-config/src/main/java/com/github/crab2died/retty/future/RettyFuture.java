package com.github.crab2died.retty.future;

import com.github.crab2died.retty.protocol.RettyResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class RettyFuture implements Future<Object> {

    private String requestId;

    private RettyResponse resp;

    private Sync sync;

    public RettyFuture(String requestId) {
        this.requestId = requestId;
        this.sync = new Sync();
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
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(-1);
        if (null != this.resp)
            return this.resp.getResponse();
        else return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (this.resp != null) {
                return this.resp.getResponse();
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Timeout exception. Request id: " + this.resp.getRequestId()
                    + ". Request ID: " + this.requestId);
        }
    }

    public void done(RettyResponse resp) {
        this.resp = resp;
        this.sync.release(1);
    }

    public String getRequestId() {
        return requestId;
    }

    private static class Sync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = 1L;

        //future status
        private final int done = 1;
        private final int pending = 0;

        protected boolean tryAcquire(int acquires) {
            return getState() == done;
        }

        protected boolean tryRelease(int releases) {
            if (getState() == pending) {
                if (compareAndSetState(pending, done)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isDone() {
            // getState();
            return getState() == done;
        }
    }
}
