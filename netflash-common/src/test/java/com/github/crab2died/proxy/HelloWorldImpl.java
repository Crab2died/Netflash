package com.github.crab2died.proxy;

import com.github.crab2died.annotation.NetflashService;

@NetflashService(interfaceClass = HelloWorld.class)
public class HelloWorldImpl implements HelloWorld {

    @Override
    public String sayHello() {
        return "hello, netflash";
    }

    @Override
    public String sayHello(String name) {
        return "hello, " + name;
    }

    @Override
    public void echo(String cmd) {
        System.out.println(">>" + cmd);
    }
}
