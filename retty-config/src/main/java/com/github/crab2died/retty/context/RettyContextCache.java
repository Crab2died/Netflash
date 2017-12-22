package com.github.crab2died.retty.context;

import com.github.crab2died.retty.route.URL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务与API的缓存
 *
 * @author : Crab2Died
 * 2017/12/20  15:04:24
 */
public class RettyContextCache {

    public static final Map<String, URL> RETTY_SERVICE_CONTEXT = new ConcurrentHashMap<>();

    public static final Map<String, Object> RETTY_API_CONTEXT = new ConcurrentHashMap<>();

    private static String LOCAL_PROTOCOL = "retty";

    public static String LOCAL_ADDRESS = "127.0.0.1:4321";
}
