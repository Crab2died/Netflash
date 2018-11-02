package com.github.crab2died;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClassScan {


    public static void main(String... args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("client.xml");
        context.start();
    }
}
