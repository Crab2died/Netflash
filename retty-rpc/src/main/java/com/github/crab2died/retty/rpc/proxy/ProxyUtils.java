package com.github.crab2died.retty.rpc.proxy;

import java.lang.reflect.Proxy;

@SuppressWarnings("all")
public class ProxyUtils {

    public static <T> T instance(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new DefaultRettyProxy()
        );
    }
}
