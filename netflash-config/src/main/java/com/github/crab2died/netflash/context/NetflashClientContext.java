package com.github.crab2died.netflash.context;

import com.github.crab2died.netflash.anntotaion.NetflashApi;
import com.github.crab2died.netflash.common.support.scanner.InterfaceAnnotationScanner;
import com.github.crab2died.netflash.proxy.ProxyUtils;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.Set;

/**
 * 客户端Context配置
 *
 * @author : Crab2Died
 * 2017/12/20  14:46:58
 */
public class NetflashClientContext implements ApplicationContextAware, BeanFactoryPostProcessor, InitializingBean {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(NetflashClientContext.class);

    // 客户端名称
    private String name;

    // 注册中心
    private String registry;

    // 配置api的class
    private Class<?>[] apiInterfaces;

    // 扫描路径
    private String scanPackage;

    public NetflashClientContext() {
    }

    public NetflashClientContext(String name, String registry, Class<?>[] apiInterfaces) {
        this.name = name;
        this.registry = registry;
        this.apiInterfaces = apiInterfaces;
    }

    public NetflashClientContext(String name, String registry, Class<?>[] apiInterfaces, String scanPackage) {
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
                        NetflashContextCache.NETFLASH_API_CONTEXT.put(beanName, proxyObj);
                    } else {
                        throw new IllegalStateException(clazz.getName() + "must interface");
                    }

                }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        DefaultListableBeanFactory fcy = (DefaultListableBeanFactory) applicationContext
                .getAutowireCapableBeanFactory();

        Set<Class> set = InterfaceAnnotationScanner.findPackageAnnotationClass(this.scanPackage, NetflashApi.class);

        if (null != set && !set.isEmpty()) {
            for (Class<?> clazz : set) {

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
                    NetflashContextCache.NETFLASH_API_CONTEXT.put(beanName, proxyObj);
                }
            }
        }
        if (logger.isEnabled(InternalLogLevel.INFO)) {
            logger.info("Initialized apis:");
            for (Map.Entry entry : NetflashContextCache.NETFLASH_API_CONTEXT.entrySet()) {
                logger.info(entry.getKey() + " => " + entry.getValue());
            }
        }


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // spring 加载完成后处理
        // 1) 同步注册中心注册服务

        // 2) 连接TCP


        // 3) 监听服务变化

    }
}
