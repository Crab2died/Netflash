package com.github.crab2died.retty.rpc.proxy;

import com.github.crab2died.retty.future.RettyFuture;
import com.github.crab2died.retty.protocol.RettyRequest;
import com.github.crab2died.retty.rpc.client.ClientFuture;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

@SuppressWarnings("all")
public class DefaultRettyProxy implements InvocationHandler, RettyProxy {


    public DefaultRettyProxy() {
    }

    @Override
    public <T> T instance(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new DefaultRettyProxy()
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

        RettyFuture future = ClientFuture.get().request(req);

        return future.get();
    }
}
