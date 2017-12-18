package com.github.crab2died.springbeans;

import com.github.crab2died.annotation.RettyService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public class SpringContext implements ApplicationContextAware, InitializingBean {


    public static final Map<String, Object> RETTY_SERVICES = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("init bean completed");
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {

        Map<String, Object> annotations = ctx.getBeansWithAnnotation(RettyService.class);
        for (Map.Entry entry : annotations.entrySet()) {
            if (void.class == entry.getValue().getClass().getAnnotation(RettyService.class).interfaceClass()) {
                Class<?>[] classes = entry.getValue().getClass().getInterfaces();
                if (null == classes || classes.length > 1) {
                    throw new UnsupportedOperationException("无法确认唯一的接口");
                }
                RETTY_SERVICES.put(classes[0].getName(), entry.getValue());
            } else {
                Class<?> clazz = entry.getValue().getClass().getAnnotation(RettyService.class).interfaceClass();
                if (clazz.isInterface()) {
                    RETTY_SERVICES.put(clazz.getName(), entry.getValue());
                }else {
                    throw new UnsupportedOperationException("RettyService注解value必须为接口的class");
                }
            }
        }

        System.out.println("注册的services:" + RETTY_SERVICES);
    }
}
