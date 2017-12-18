package com.github.crab2died.retty;

import com.github.crab2died.retty.context.RettyContext;
import com.github.crab2died.retty.demo.service.DemoService;
import com.github.crab2died.retty.rpc.client.RettyClient;
import com.github.crab2died.retty.rpc.proxy.DefaultRettyProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        ApplicationContext context = new ClassPathXmlApplicationContext("client.xml");
        new Thread(new Runnable() {
            @Override
            public void run() {
                RettyClient client = (RettyClient) context.getBean("client");
                try {
                    client.connect();
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        latch.await(5, TimeUnit.SECONDS);
        DefaultRettyProxy proxy = new DefaultRettyProxy();
        DemoService demoService = proxy.instance(DemoService.class);
        String s = demoService.test("2345");
        System.out.println(s);
    }
}
