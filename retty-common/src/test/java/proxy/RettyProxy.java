package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@SuppressWarnings("all")
public class RettyProxy implements InvocationHandler {

    private Class clazz;

    public RettyProxy() {
    }

    public RettyProxy(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("sayHello".equals(method.getName())) {
            if (null != args && args.length == 1) {
                return "hello, " + args[0];
            } else {
                return "hello";
            }
        }
        if ("echo".equals(method.getName())) {
            System.out.println(args[0]);
            return null;
        }
        return null;
    }

    public static <T> T instance(Class<T> interfaceClass) {

        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new RettyProxy()
        );
    }
}
