package com.github.crab2died.retty.demo.service.impl;

import com.github.crab2died.retty.anntotaion.RettyService;
import com.github.crab2died.retty.demo.service.DemoService;

@RettyService
public class DemoServiceImpl implements DemoService {

    @Override
    public void echo(String cmd) {
        System.out.println(">>" + cmd);
    }

    @Override
    public String test() {
        return "this is proxy test";
    }

    @Override
    public String test(String something) {
        return "this is proxy test, " + something;
    }
}
