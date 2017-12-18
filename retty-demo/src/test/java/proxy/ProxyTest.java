package proxy;

import com.github.crab2died.retty.context.RettyContext;
import com.github.crab2died.retty.demo.service.DemoService;
import com.github.crab2died.retty.rpc.proxy.DefaultRettyProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("classpath:spring-demo-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProxyTest {

    @Autowired
    private RettyContext springRettyContextInit;

    @Test
    public void test(){

        DemoService demoService = new DefaultRettyProxy().instance(DemoService.class);
        demoService.test();

    }

    @Test
    public void server(){

    }

}
