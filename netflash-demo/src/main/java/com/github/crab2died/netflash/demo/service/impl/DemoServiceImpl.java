package com.github.crab2died.netflash.demo.service.impl;

import com.github.crab2died.netflash.anntotaion.NetflashService;
import com.github.crab2died.netflash.demo.service.DemoService;

@NetflashService
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
