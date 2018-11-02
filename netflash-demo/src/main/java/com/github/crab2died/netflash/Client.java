package com.github.crab2died.netflash;

import com.github.crab2died.netflash.demo.service.DemoService;
import com.github.crab2died.netflash.rpc.client.NetflashClient;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("client.xml");
        context.start();
        NetflashClient client = (NetflashClient) context.getBean("client");
        new Thread(() -> {
            try {
                client.connect();
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        latch.await(4, TimeUnit.SECONDS);

        for (int i = 0; i < 100; i++){
            final int fi = i;
            new Thread(() -> {
                DemoService demoService = (DemoService) context.getBean("demoService");
                String s = demoService.test("请返回：" + fi);
                System.out.println(fi + " > 结果：" + s);
            }).start();
        }

        System.in.read();
    }
}
