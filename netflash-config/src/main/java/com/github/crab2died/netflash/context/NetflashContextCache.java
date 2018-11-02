package com.github.crab2died.netflash.context;

import com.github.crab2died.netflash.route.URL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务与API的缓存
 *
 * @author : Crab2Died
 * 2017/12/20  15:04:24
 */
public class NetflashContextCache {

    public static final Map<String, URL> NETFLASH_SERVICE_CONTEXT = new ConcurrentHashMap<>();

    public static final Map<String, Object> NETFLASH_API_CONTEXT = new ConcurrentHashMap<>();

    private static String LOCAL_PROTOCOL = "netflash";

    public static String LOCAL_ADDRESS = "127.0.0.1:4321";
}
