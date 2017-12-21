package com.github.crab2died.retty.route;

public class URL {

    // 服务全限定名，如: com.github.crab2died.demo.DemoService
    private String service;

    // 权重,如：100
    private Integer weight;

    // 服务地址 如：retty://127.0.0.1:8900
    private String address;

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
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // 如 URL:retty:127.0.0.1:4321:com.github.crab2died.demo.DemoService[10]
    @Override
    public String toString() {
        return "URL:" + address + ":" + service + "[" + weight + "]";
    }
}
