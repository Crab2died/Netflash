package com.github.crab2died.proxy;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-test.xml")
public class NetflashProxyTest {


    @Test
    public void helloTest() {
        HelloWorld hw = NetflashProxy.instance(HelloWorld.class);
        hw.echo("Test");
        Assert.assertEquals(hw.sayHello("Crab2Died"), "hello, Crab2Died");
        Assert.assertEquals(hw.sayHello(), "hello, netflash");
    }

}
