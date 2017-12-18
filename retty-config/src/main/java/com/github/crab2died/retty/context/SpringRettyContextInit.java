package com.github.crab2died.retty.context;

import com.github.crab2died.retty.anntotaion.RettyService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class SpringRettyContextInit implements ApplicationContextAware, RettyContext {

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {

        Map<String, Object> annotations = ctx.getBeansWithAnnotation(RettyService.class);
        for (Map.Entry entry : annotations.entrySet()) {
            if (void.class == entry.getValue().getClass().getAnnotation(RettyService.class).interfaceClass()) {
                Class<?>[] classes = entry.getValue().getClass().getInterfaces();
                if (null == classes || classes.length > 1) {
                    throw new UnsupportedOperationException("Unable to confirm the unique interface");
                }
                RettyContextCache.RETTY_CONTEXT.put(classes[0].getName(), entry.getValue());
            } else {
                Class<?> clazz = entry.getValue().getClass().getAnnotation(RettyService.class).interfaceClass();
                if (clazz.isInterface()) {
                    RettyContextCache.RETTY_CONTEXT.put(clazz.getName(), entry.getValue());
                } else {
                    throw new UnsupportedOperationException("RettyService注解value必须为接口的class");
                }
            }
        }

        System.out.println("注册的services:" + RettyContextCache.RETTY_CONTEXT);
    }

    /**
     * 初始化Retty Context
     * 此处被{@link ApplicationContextAware#setApplicationContext(ApplicationContext ctx)}替代了
     *
     * @author : Crab2Died
     * 2017/12/18  14:16:12
     */
    @Override
    public void init() {
        //nothing
    }
}
