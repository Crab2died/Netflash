package proxy;

import org.junit.Assert;
import org.junit.Test;

public class RettyProxyTest {

    @Test
    public void helloTest() {
        HelloWorld hw = RettyProxy.instance(HelloWorld.class);
        hw.echo("Test");
        Assert.assertEquals(hw.sayHello("Crab2Died"), "hello, Crab2Died");
        Assert.assertEquals(hw.sayHello(), "hello");
    }
}
