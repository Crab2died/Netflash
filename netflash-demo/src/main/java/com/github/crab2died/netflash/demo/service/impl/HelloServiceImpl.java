package com.github.crab2died.netflash.demo.service.impl;

import com.github.crab2died.netflash.anntotaion.NetflashService;
import com.github.crab2died.netflash.demo.service.HelloService;

@NetflashService
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String str) {
        return str;
    }
}
