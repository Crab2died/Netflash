package com.github.crab2died.retty.proxy;

import com.github.crab2died.retty.context.RettyContext;
import com.github.crab2died.retty.protocol.RettyRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

@SuppressWarnings("all")
public class DefaultRettyProxy implements InvocationHandler, RettyProxy {

    private RettyContext rettyContext;

    private DefaultRettyProxy() {
    }

    public DefaultRettyProxy(RettyContext rettyContext) {
        this.rettyContext = rettyContext;
    }

    @Override
    public <T> T instance(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new DefaultRettyProxy(this.rettyContext)
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class clazz = method.getDeclaringClass();

        RettyRequest req = new RettyRequest();
        req.setRequestId(UUID.randomUUID().toString());
        req.setInterfaceName(clazz.getName());
        req.setMethodName(method.getName());
        req.setMethodType(method.getParameterTypes());
        req.setParameters(args);

        return MethodInvoke.invoke(req);
    }
}
