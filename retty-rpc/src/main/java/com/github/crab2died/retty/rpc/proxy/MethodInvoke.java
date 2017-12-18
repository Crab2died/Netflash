package com.github.crab2died.retty.rpc.proxy;

import com.github.crab2died.retty.context.RettyContextCache;
import com.github.crab2died.retty.protocol.RettyRequest;
import com.github.crab2died.retty.protocol.RettyResponse;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;


public class MethodInvoke {

    public static RettyResponse invoke(RettyRequest req) {

        RettyResponse resp = new RettyResponse();
        resp.setRequestId(req.getRequestId());
        try {
            Object bean = RettyContextCache.RETTY_CONTEXT.get(req.getMethodName());
            String methodName = req.getMethodName();
            Class<?>[] parameterTypes = req.getMethodType();

            if (null == bean) {
                throw new ClassNotFoundException("The implementation class is not found");
            }

            FastClass implClass = FastClass.create(bean.getClass());
            FastMethod implMethod = implClass.getMethod(methodName, parameterTypes);

            if (null == implMethod) {
                throw new NoSuchMethodException("An implementation method is not found");
            }

            implMethod.invoke(bean, req.getParameters());
        } catch (Throwable t) {
            resp.setErr(-1);
            resp.setDesc(t.getMessage());
        }
        return resp;
    }
}
