package com.github.crab2died.retty.context;

import com.github.crab2died.retty.anntotaion.RettyApi;
import com.github.crab2died.retty.anntotaion.RettyService;
import com.github.crab2died.retty.proxy.ProxyUtils;
import com.github.crab2died.retty.scan.RettyApiAnnotationScanner;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;

import java.io.IOException;
import java.util.Map;

/**
 * 客户端Context配置
 *
 * @author : wbhe2
 * 2017/12/20  14:46:58
 */
public class RettyClientContext implements ApplicationContextAware, BeanFactoryPostProcessor {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(RettyClientContext.class);

    // 客户端名称
    private String name;

    // 注册中心
    private String registry;

    // 配置api的class
    private Class<?>[] apiInterfaces;

    // 扫描路径
    private String scanPackage;

    public RettyClientContext() {
    }

    public RettyClientContext(String name, String registry, Class<?>[] apiInterfaces) {
        this.name = name;
        this.registry = registry;
        this.apiInterfaces = apiInterfaces;
    }

    public RettyClientContext(String name, String registry, Class<?>[] apiInterfaces, String scanPackage) {
        this.name = name;
        this.registry = registry;
        this.apiInterfaces = apiInterfaces;
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

    public Class<?>[] getApiInterfaces() {
        return apiInterfaces;
    }

    public void setApiInterfaces(Class<?>[] apiInterfaces) {
        this.apiInterfaces = apiInterfaces;
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

        DefaultListableBeanFactory fcy = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();

        if (null != apiInterfaces) {
            if (apiInterfaces.length > 0) {
                for (Class<?> clazz : apiInterfaces) {
                    if (clazz.isInterface() && !clazz.isAnnotation()) {
                        String simpleName = clazz.getSimpleName();
                        String beanName = simpleName.replaceFirst(
                                simpleName.substring(0, 1),
                                simpleName.substring(0, 1).toLowerCase()
                        );
                        Object proxyObj = ProxyUtils.instance(clazz);
                        fcy.registerSingleton(beanName, proxyObj);
                        RettyContextCache.RETTY_API_CONTEXT.put(beanName, proxyObj);
                    } else {
                        throw new IllegalStateException(clazz.getName() + "must interface");
                    }

                }
            }
        }
        if (logger.isEnabled(InternalLogLevel.INFO)) {
            logger.info("Initialized apis:");
            for (Map.Entry entry : RettyContextCache.RETTY_API_CONTEXT.entrySet()) {
                logger.info(entry.getKey() + " => " + entry.getValue());
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        DefaultListableBeanFactory fcy = (DefaultListableBeanFactory) applicationContext
                .getAutowireCapableBeanFactory();

        RettyApiAnnotationScanner scanner1 = RettyApiAnnotationScanner.getScanner(
                (BeanDefinitionRegistry) beanFactory,
                RettyService.class
        );
        // 设置ApplicationContext
        scanner1.setResourceLoader(this.applicationContext);
        // 执行扫描
        scanner1.scan(this.scanPackage);
        try {
            Resource[] resources = scanner1.getResources(this.scanPackage);
            CachingMetadataReaderFactory metadataReaderFactory = (CachingMetadataReaderFactory)
                    scanner1.getMetadataReaderFactory();

            for (Resource resource : resources) {
                ClassMetadata obj = metadataReaderFactory.getMetadataReader(resource).getClassMetadata();
                try {
                    Class<?> clazz = Class.forName(obj.getClassName());
                    if (clazz.isInterface() && !clazz.isAnnotation() && clazz.getAnnotation(RettyApi.class) != null) {

                        String simpleName = clazz.getSimpleName();
                        String beanName = simpleName.replaceFirst(
                                simpleName.substring(0, 1),
                                simpleName.substring(0, 1).toLowerCase()
                        );
                        Object proxyObj = ProxyUtils.instance(clazz);
                        Object bean = null;
                        try {
                            bean = applicationContext.getBean(beanName);
                        } catch (Exception e) {
                            // do nothing
                        }
                        if (bean == null) {
                            fcy.registerSingleton(beanName, proxyObj);
                            RettyContextCache.RETTY_API_CONTEXT.put(beanName, proxyObj);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
