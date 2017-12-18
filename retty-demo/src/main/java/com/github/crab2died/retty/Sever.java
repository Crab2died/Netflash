package com.github.crab2died.retty;

import com.github.crab2died.retty.rpc.server.RettyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Sever {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("server.xml");
        RettyServer server = (RettyServer) context.getBean("server");
        server.open();
    }
}
