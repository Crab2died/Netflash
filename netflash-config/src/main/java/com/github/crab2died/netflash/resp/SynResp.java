package com.github.crab2died.netflash.resp;

import com.github.crab2died.netflash.future.NetflashFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SynResp {

    public static Map<String, NetflashFuture> SYNC_RESPONSE = new ConcurrentHashMap<>();
}
