package com.github.crab2died.netflash.anntotaion;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NetflashApi {

    //
    String value() default "";

    // 调用服务版本号
    String version() default "";

}
