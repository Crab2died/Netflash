package com.github.crab2died.retty.scan;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;

public class RettyServiceAnnotationScanner extends ClassPathBeanDefinitionScanner {

    /**
     * 实体类对应的AnnotationClazz
     */
    private Class<? extends Annotation> selfAnnotationClazz;

    /**
     * 传值使用的临时静态变量
     */
    private static Class<? extends Annotation> staticTempAnnotationClazz = null;

    /**
     * 因构造函数无法传入指定的Annotation类，需使用静态方法来调用
     */
    public static synchronized RettyServiceAnnotationScanner getScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> clazz){
        staticTempAnnotationClazz = clazz;
        RettyServiceAnnotationScanner scanner = new RettyServiceAnnotationScanner(registry);
        scanner.setSelfAnnotationClazz(clazz);
        return scanner;
    }

    private RettyServiceAnnotationScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    // 构造函数需调用函数，使用静态变量annotationClazz传值
    @Override
    public void registerDefaultFilters() {
        // 添加需扫描的Annotation Class
        this.addIncludeFilter(new AnnotationTypeFilter(staticTempAnnotationClazz));
    }

    // 以下为初始化后调用的方法
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    @Override
    public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return super.isCandidateComponent(beanDefinition)
                && beanDefinition.getMetadata().hasAnnotation(this.selfAnnotationClazz.getName());
    }

    public Class<? extends Annotation> getSelfAnnotationClazz() {
        return selfAnnotationClazz;
    }

    public void setSelfAnnotationClazz(Class<? extends Annotation> selfAnnotationClazz) {
        this.selfAnnotationClazz = selfAnnotationClazz;
    }

    public static Class<? extends Annotation> getStaticTempAnnotationClazz() {
        return staticTempAnnotationClazz;
    }

    public static void setStaticTempAnnotationClazz(Class<? extends Annotation> staticTempAnnotationClazz) {
        RettyServiceAnnotationScanner.staticTempAnnotationClazz = staticTempAnnotationClazz;
    }
}
