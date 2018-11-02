package com.github.crab2died.netflash.route;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URL implements Serializable {

    // 服务全限定名，如: com.github.crab2died.demo.DemoService
    private String service;

    // 权重,如：100
    private Integer weight;

    // 版本标识
    private String version;

    // 协议 如:netflash
    private String protocol;

    // 服务地址 如：netflash://127.0.0.1:8900
    private String address;

    // 服务实现bean，排除序列化
    private transient Object bean;

    public URL() {
    }

    public URL(String service, String protocol, String address, Object bean) {
        this.service = service;
        this.protocol = protocol;
        this.address = address;
        this.bean = bean;
    }

    public URL(String service, Integer weight, String version, String protocol, String address) {
        this.service = service;
        this.weight = weight < 0 ? 0 : weight;
        this.version = version;
        this.protocol = protocol;
        this.address = address;
    }

    public URL(String service, Integer weight, String version, String protocol, String address, Object bean) {
        this.service = service;
        this.weight = weight;
        this.version = version;
        this.protocol = protocol;
        this.address = address;
        this.bean = bean;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight < 0 ? 0 : weight;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    // 如 netflash:127.0.0.1:4321:com.github.crab2died.demo.DemoService?version=1.1&weight=100
    @Override
    public String toString() {
        return protocol + "://" + address + ":" + service + "?version=" + version + "&weight=" + weight;
    }


    // url的比较

    @Override
    public int hashCode() {

        String uri = protocol + "://" + service + "[" + version + "]";
        return uri.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj)
            throw new IllegalArgumentException("object must not be null");
        if (this.getClass() == obj.getClass()) {
            URL url = (URL) obj;
            return this.protocol.equals(url.getProtocol())
                    && this.service.equals(url.getService())
                    && this.version.equals(url.getVersion());
        } else {
            return false;
        }
    }

    public String encode() {
        try {
            return URLEncoder.encode(this.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static URL analysis(String url) throws UnsupportedEncodingException {

        url = URLDecoder.decode(url, "UTF-8");
        URL _url = new URL();
        String[] $_ = url.split("\\?");
        String[] params = $_[1].split("&");

        String[] $_h = $_[0].split(":");
        _url.setProtocol($_h[0]);
        _url.setAddress($_h[1].substring(2) + ":" + $_h[2]);
        _url.setService($_h[3]);

        for (String param : params) {
            if (param.startsWith("version=")) {
                _url.setVersion(param.substring(8));
            }
            if (param.startsWith("weight=")) {
                _url.setWeight(Integer.parseInt(param.substring(7)));
            }
        }
        return _url;
    }
}
