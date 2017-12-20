package com.github.crab2died.retty.anntotaion;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RettyService {

    /**
     * 指定api接口
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 权重
     */
    int weight() default 100;

    /**
     * 版本号
     */
    String version() default "";
}
