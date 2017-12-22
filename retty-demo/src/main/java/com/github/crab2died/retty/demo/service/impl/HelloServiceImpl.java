package com.github.crab2died.retty.demo.service.impl;

import com.github.crab2died.retty.anntotaion.RettyService;
import com.github.crab2died.retty.demo.service.HelloService;

@RettyService
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String str) {
        return str;
    }
}
