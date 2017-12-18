package com.github.crab2died.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RettyService {

    Class<?> interfaceClass() default void.class;

    String version() default "";
}
