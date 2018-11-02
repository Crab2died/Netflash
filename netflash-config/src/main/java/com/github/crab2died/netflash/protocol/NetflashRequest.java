package com.github.crab2died.netflash.protocol;

import java.io.Serializable;
import java.util.Arrays;

public class NetflashRequest implements Serializable{

    private String requestId;

    private long sessionId;

    private String version;

    private String interfaceName;

    private String methodName;

    private Class<?>[] methodType;

    private Object[] parameters;

    public NetflashRequest() {
    }

    public NetflashRequest(String requestId, long sessionId, String version, String interfaceName, String methodName,
                        Class<?>[] methodType, Object[] parameters) {
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.version = version;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.methodType = methodType;
        this.parameters = parameters;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getMethodType() {
        return methodType;
    }

    public void setMethodType(Class<?>[] methodType) {
        this.methodType = methodType;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "netflashRequest{" +
                "requestId=" + requestId +
                ", sessionId=" + sessionId +
                ", interfaceName='" + interfaceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodType=" + Arrays.toString(methodType) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
