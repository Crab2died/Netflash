package com.github.crab2died.retty.context;

import com.github.crab2died.retty.common.support.scanner.ClassAnnotationScanner;
import com.github.crab2died.retty.anntotaion.RettyService;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * 服务端Context配置
 *
 * @author : Crab2Died
 * 2017/12/18  14:21:51
 */
public class RettyServerContext implements ApplicationContextAware, BeanFactoryPostProcessor {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(RettyServerContext.class);

    // 服务名
    private String name;

    // 注册中心
    private String registry;

    // 协议
    private Protocol protocol;

    // service服务实现
    private Object[] services;

    private String scanPackage;

    public RettyServerContext() {
    }

    public RettyServerContext(String registry, Protocol protocol, Object[] services) {
        this.registry = registry;
        this.protocol = protocol;
        this.services = services;
    }

    public RettyServerContext(String name, String registry, Protocol protocol, Object[] services) {
        this.name = name;
        this.registry = registry;
        this.protocol = protocol;
        this.services = services;
    }

    public RettyServerContext(String name, String registry, Protocol protocol, Object[] services, String scanPackage) {
        this.name = name;
        this.registry = registry;
        this.protocol = protocol;
        this.services = services;
        this.scanPackage = scanPackage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public Object[] getServices() {
        return services;
    }

    public void setServices(Object[] services) {
        this.services = services;
    }

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {

        this.applicationContext = ctx;
        // 处理非注解的
        RettyServerContext serverContext = ctx.getBean(RettyServerContext.class);
        if (null != serverContext) {
            Object[] services = serverContext.getServices();
            if (null != services && services.length > 0) {
                for (Object service : services) {
                    Class[] faced = service.getClass().getInterfaces();
                    if (service.getClass().isInterface() || faced == null || faced.length != 1) {
                        throw new IllegalStateException(service.getClass().getName());
                    }
                    RettyContextCache.RETTY_SERVICE_CONTEXT.put(faced[0].getName(), service);
                }
            }

        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        // 使用自定义扫描类，针对@TestModel进行扫描
        ClassAnnotationScanner scanner = ClassAnnotationScanner.getScanner(
                (BeanDefinitionRegistry) beanFactory,
                RettyService.class
        );
        // 设置ApplicationContext
        scanner.setResourceLoader(this.applicationContext);
        // 执行扫描
        scanner.scan(this.scanPackage);

        // 取得对应Annotation映射，BeanName -- 实例
        Map<String, Object> annotations = beanFactory.getBeansWithAnnotation(RettyService.class);

        for (Map.Entry entry : annotations.entrySet()) {
            if (void.class == entry.getValue().getClass().getAnnotation(RettyService.class).interfaceClass()) {
                Class<?>[] classes = entry.getValue().getClass().getInterfaces();
                if (null == classes || classes.length > 1) {
                    throw new IllegalStateException("Unable to confirm the unique interface");
                }
                RettyContextCache.RETTY_SERVICE_CONTEXT.put(classes[0].getName(), entry.getValue());
            } else {
                Class<?> clazz = entry.getValue().getClass().getAnnotation(RettyService.class).interfaceClass();
                if (clazz.isInterface()) {
                    RettyContextCache.RETTY_SERVICE_CONTEXT.put(clazz.getName(), entry.getValue());
                } else {
                    throw new IllegalStateException("RettyService注解value必须为接口的class");
                }
            }
        }
        if (logger.isEnabled(InternalLogLevel.INFO)) {
            logger.info("Initialized services:");
            for (Map.Entry entry : RettyContextCache.RETTY_SERVICE_CONTEXT.entrySet()) {
                logger.info(entry.getKey() + " => " + entry.getValue());
            }
        }
    }
}
