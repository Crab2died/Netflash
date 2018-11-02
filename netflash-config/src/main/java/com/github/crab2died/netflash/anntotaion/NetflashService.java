package com.github.crab2died.netflash.anntotaion;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NetflashService {

    /**
     * 指定api接口
     */
    Class<?> interfaceClass() default Void.class;

    /**
     * 权重
     */
    int weight() default 100;

    /**
     * 版本号
     */
    String version() default "";
}
