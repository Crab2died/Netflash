package com.github.crab2died.netflash.context;

import com.github.crab2died.netflash.anntotaion.NetflashService;
import com.github.crab2died.netflash.common.ZkClientUtils;
import com.github.crab2died.netflash.common.support.scanner.ClassAnnotationScanner;
import com.github.crab2died.netflash.route.URL;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

import static com.github.crab2died.netflash.Constant.REGISTRY_SERVICE_NODE;
import static com.github.crab2died.netflash.context.NetflashContextCache.LOCAL_ADDRESS;
import static com.github.crab2died.netflash.context.NetflashContextCache.NETFLASH_SERVICE_CONTEXT;


/**
 * 服务端Context配置
 *
 * @author : Crab2Died
 * 2017/12/18  14:21:51
 */
public class NetflashServerContext implements ApplicationContextAware, BeanFactoryPostProcessor, InitializingBean {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(NetflashServerContext.class);

    // 服务名
    private String name;

    // 注册中心
    private String registry;

    // 协议
    private Protocol protocol;

    // service服务实现
    private Object[] services;

    private String scanPackage;

    public NetflashServerContext() {
    }

    public NetflashServerContext(String registry, Protocol protocol, Object[] services) {
        this.registry = registry;
        this.protocol = protocol;
        this.services = services;
    }

    public NetflashServerContext(String name, String registry, Protocol protocol, Object[] services) {
        this.name = name;
        this.registry = registry;
        this.protocol = protocol;
        this.services = services;
    }

    public NetflashServerContext(String name, String registry, Protocol protocol, Object[] services, String scanPackage) {
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
        NetflashServerContext serverContext = ctx.getBean(NetflashServerContext.class);
        if (null != serverContext) {
            Object[] services = serverContext.getServices();
            if (null != services && services.length > 0) {
                for (Object service : services) {
                    Class[] faced = service.getClass().getInterfaces();
                    if (service.getClass().isInterface() || faced == null || faced.length != 1) {
                        throw new IllegalStateException(service.getClass().getName());
                    }
                    URL url = new URL(faced[0].getName(), protocol.getName(), LOCAL_ADDRESS, service);
                    NETFLASH_SERVICE_CONTEXT.put(faced[0].getName(), url);
                }
            }

        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        // 使用自定义扫描类，针对@TestModel进行扫描
        ClassAnnotationScanner scanner = ClassAnnotationScanner.getScanner(
                (BeanDefinitionRegistry) beanFactory,
                NetflashService.class
        );
        // 设置ApplicationContext
        scanner.setResourceLoader(this.applicationContext);
        // 执行扫描
        scanner.scan(this.scanPackage);

        // 取得对应Annotation映射，BeanName -- 实例
        Map<String, Object> annotations = beanFactory.getBeansWithAnnotation(NetflashService.class);

        for (Map.Entry entry : annotations.entrySet()) {
            if (Void.TYPE == entry.getValue().getClass().getAnnotation(NetflashService.class).interfaceClass()) {
                Class<?>[] classes = entry.getValue().getClass().getInterfaces();
                if (null == classes || classes.length > 1) {
                    throw new IllegalStateException("Unable to confirm the unique interface");
                }
                URL url = new URL(classes[0].getName(), protocol.getName(), LOCAL_ADDRESS, entry.getValue());
                NETFLASH_SERVICE_CONTEXT.put(classes[0].getName(), url);
            } else {
                NetflashService annotation = entry.getValue().getClass().getAnnotation(NetflashService.class);
                Class<?>[] classes = entry.getValue().getClass().getInterfaces();
                if (null == classes || classes.length > 1) {
                    throw new IllegalStateException("Unable to confirm the unique interface");
                }
                Class<?> clazz = classes[0];
                if (clazz.isInterface() && !clazz.isAnnotation()) {
                    URL url = new URL(
                            clazz.getName(), annotation.weight(),
                            annotation.version(), protocol.getName(),
                            LOCAL_ADDRESS, entry.getValue()
                    );
                    NETFLASH_SERVICE_CONTEXT.put(clazz.getName(), url);
                } else {
                    throw new IllegalStateException("NetflashService注解value必须为接口的class");
                }
            }
        }
        if (logger.isEnabled(InternalLogLevel.INFO)) {
            logger.info("Initialized services:");
            for (Map.Entry entry : NETFLASH_SERVICE_CONTEXT.entrySet()) {
                logger.info(entry.getValue().toString());
            }
        }

        // 2) 发布服务至注册中心

        ZkClient zkClient = new ZkClient(registry, 5000, 5000, new ZkClientUtils.MyZkSerializer());
        zkClient.createPersistent(REGISTRY_SERVICE_NODE, true);

        for (Map.Entry<String, URL> entry : NETFLASH_SERVICE_CONTEXT.entrySet()) {

            String servicesNode = REGISTRY_SERVICE_NODE + "/" + entry.getKey();
            zkClient.createEphemeralSequential(servicesNode, entry.getValue().encode());
        }
//
//
//        Set<String> set = new HashSet<>();
//        for (Map.Entry<String, URL> entry : Netflash_SERVICE_CONTEXT.entrySet()) {
//            set.add(entry.getValue().encode());
//        }
//        if (zkClient.exists(REGISTRY_SERVICE_NODE)) {
//            String store = zkClient.readData(REGISTRY_SERVICE_NODE);
//            if (null != store && !"[]".equals(store)) {
//                set.addAll(JSONArray.parseArray(store, String.class));
//            }
//        }
//        if (!set.isEmpty()) {
//            ZkClientUtils.initNode(zkClient, REGISTRY_SERVICE_NODE, set);
//        }

        String str = zkClient.readData(REGISTRY_SERVICE_NODE);
        System.out.println(str);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // spring 加载完成后处理
        // 1) TCP server启动


    }
}
