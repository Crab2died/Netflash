package com.github.crab2died.netflash.proxy;

import java.lang.reflect.Proxy;

@SuppressWarnings("all")
public class ProxyUtils {

    public static <T> T instance(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{interfaceClass},
                new DefaultNetflashProxy()
        );
    }
}
