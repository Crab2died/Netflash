package com.github.crab2died.springbeans;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class SpringContextTest {

    @Autowired
    private SpringContext springContext;

    @Test
    public void test() {
        springContext.netflash_SERVICES.isEmpty();
    }
}
