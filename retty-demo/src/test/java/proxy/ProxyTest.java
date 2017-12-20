package proxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("classpath:spring-demo-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProxyTest {

    @Autowired

    @Test
    public void test(){

     //   DemoService demoService = new DefaultRettyProxy().instance(DemoService.class);
     //   demoService.test();

    }

    @Test
    public void server(){

    }

}
