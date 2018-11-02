package com.github.crab2died.proxy;

import com.github.crab2died.springbeans.SpringContext;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@SuppressWarnings("all")
public class NetflashProxy implements InvocationHandler {

    private Class clazz;

    public NetflashProxy() {
    }

    public NetflashProxy(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class clazz = method.getDeclaringClass();

        Object bean = SpringContext.netflash_SERVICES.get(clazz.getName());

        if (null == bean)
            throw new ClassNotFoundException("未找到实现类");

        FastClass implClass = FastClass.create(bean.getClass());
        FastMethod implMethod = implClass.getMethod(method.getName(), method.getParameterTypes());

        if (null == implMethod){
            throw new NoSuchMethodException("未找到相关实现方法");
        }

        return implMethod.invoke(bean, args);
    }

    public static <T> T instance(Class<T> interfaceClass) {

        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new NetflashProxy()
        );
    }
}
