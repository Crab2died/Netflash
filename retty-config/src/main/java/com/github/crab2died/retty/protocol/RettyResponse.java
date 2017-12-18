package com.github.crab2died.retty.protocol;

public class RettyResponse {

    private String requestId;

    private int err;

    private String desc;

    private Object response;

    public RettyResponse() {
    }

    public RettyResponse(String requestId, int err, String desc, Object response) {
        this.requestId = requestId;
        this.err = err;
        this.desc = desc;
        this.response = response;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "RettyResponse{" +
                "requestId=" + requestId +
                ", err=" + err +
                ", desc='" + desc + '\'' +
                ", response=" + response +
                '}';
    }
}
