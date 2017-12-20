package com.github.crab2died.retty.resp;

import com.github.crab2died.retty.future.RettyFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SynResp {

    public static Map<String, RettyFuture> SYNC_RESPONSE = new ConcurrentHashMap<>();
}
