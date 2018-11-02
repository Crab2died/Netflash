package com.github.crab2died.netflash.proxy;

import com.github.crab2died.netflash.context.NetflashContextCache;
import com.github.crab2died.netflash.protocol.NetflashRequest;
import com.github.crab2died.netflash.protocol.NetflashResponse;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;


public class MethodInvoke {

    public static NetflashResponse invoke(NetflashRequest req) {

        NetflashResponse resp = new NetflashResponse();
        resp.setRequestId(req.getRequestId());
        try {
            Object bean = NetflashContextCache.NETFLASH_SERVICE_CONTEXT.get(req.getInterfaceName());
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

            resp.setResponse(implMethod.invoke(bean, req.getParameters()));
        } catch (Throwable t) {
            resp.setErr(-1);
            resp.setDesc(t.getMessage());
        }
        return resp;
    }
}
