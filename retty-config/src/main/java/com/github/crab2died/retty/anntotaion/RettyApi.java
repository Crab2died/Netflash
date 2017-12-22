package com.github.crab2died.retty.anntotaion;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RettyApi {

    //
    String value() default "";

    // 调用服务版本号
    String version() default "";

}
