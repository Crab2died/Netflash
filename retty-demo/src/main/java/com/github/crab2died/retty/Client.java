package com.github.crab2died.retty;

import com.github.crab2died.retty.demo.service.DemoService;
import com.github.crab2died.retty.protocol.RettyRequest;
import com.github.crab2died.retty.rpc.client.RettyClient;
import com.github.crab2died.retty.rpc.proxy.ProxyUtils;
import io.netty.channel.socket.SocketChannel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        ApplicationContext context = new ClassPathXmlApplicationContext("client.xml");
        RettyClient client = (RettyClient) context.getBean("client");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.connect();
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        latch.await(5, TimeUnit.SECONDS);

        for (int i = 0; i < 100; i++){
            final int fi = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DemoService demoService = ProxyUtils.instance(DemoService.class);
                    String s = demoService.test("请返回：" + fi);
                    System.out.println(fi + " > 结果：" + s);
                }
            }).start();
        }

        System.in.read();
    }
}
