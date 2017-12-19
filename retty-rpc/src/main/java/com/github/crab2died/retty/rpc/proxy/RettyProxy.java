package com.github.crab2died.retty.rpc.proxy;

/**
 * Retty底层代理接口
 *
 * @author : Crab2Died
 * 2017/12/18  13:18:01
 */
public interface RettyProxy {

    <T> T instance(Class<T> interfaceClass);

}
