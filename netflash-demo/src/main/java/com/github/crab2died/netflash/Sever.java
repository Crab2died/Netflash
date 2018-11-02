package com.github.crab2died.netflash;

import com.github.crab2died.netflash.rpc.server.NetflashServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Sever {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("server.xml");
        NetflashServer server = (NetflashServer) context.getBean("server");
        server.open();
    }
}
