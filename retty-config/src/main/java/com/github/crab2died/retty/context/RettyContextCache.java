package com.github.crab2died.retty.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务与API的缓存
 *
 * @author : Crab2Died
 * 2017/12/20  15:04:24
 */
public class RettyContextCache {

    public static final Map<String, Object> RETTY_SERVICE_CONTEXT = new ConcurrentHashMap<>();

    public static final Map<String, Object> RETTY_API_CONTEXT = new ConcurrentHashMap<>();

}
