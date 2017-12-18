package com.github.crab2died.retty.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RettyContextCache {

    public static final Map<String, Object> RETTY_CONTEXT = new ConcurrentHashMap<>();
}
