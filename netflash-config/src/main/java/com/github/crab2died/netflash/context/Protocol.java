package com.github.crab2died.netflash.context;

/**
 * 服务协议配置
 *
 * @author : Crab2Died
 * 2017/12/20  14:46:12
 */
public class Protocol {

    /**
     * 协议名
     */
    private String name;

    /**
     * IP
     */
    private String host;

    /**
     * 服务端口
     */
    private int port;

    public Protocol() {
    }

    public Protocol(String name, int port) {
        this.name = name;
        this.port = port;
    }

    public Protocol(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
