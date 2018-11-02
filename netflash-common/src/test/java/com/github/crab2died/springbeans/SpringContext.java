package com.github.crab2died.springbeans;

import com.github.crab2died.annotation.NetflashService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public class SpringContext implements ApplicationContextAware, InitializingBean {


    public static final Map<String, Object> netflash_SERVICES = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("init bean completed");
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {

        Map<String, Object> annotations = ctx.getBeansWithAnnotation(NetflashService.class);
        for (Map.Entry entry : annotations.entrySet()) {
            if (void.class == entry.getValue().getClass().getAnnotation(NetflashService.class).interfaceClass()) {
                Class<?>[] classes = entry.getValue().getClass().getInterfaces();
                if (null == classes || classes.length > 1) {
                    throw new UnsupportedOperationException("无法确认唯一的接口");
                }
                netflash_SERVICES.put(classes[0].getName(), entry.getValue());
            } else {
                Class<?> clazz = entry.getValue().getClass().getAnnotation(NetflashService.class).interfaceClass();
                if (clazz.isInterface()) {
                    netflash_SERVICES.put(clazz.getName(), entry.getValue());
                }else {
                    throw new UnsupportedOperationException("netflashService注解value必须为接口的class");
                }
            }
        }

        System.out.println("注册的services:" + netflash_SERVICES);
    }
}
