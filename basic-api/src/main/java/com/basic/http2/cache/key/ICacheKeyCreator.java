package com.basic.http2.cache.key;

import com.basic.http2.annotation.NetMethod;

import java.lang.reflect.Method;

/**
 * 缓存Key的生成器
 */
public interface ICacheKeyCreator {

    /**
     * 根据网络请求的请求接口方法，自动生成缓存的Key
     *
     * @param method    请求方法
     * @param args      请求参数
     * @param apiMethod 请求信息
     * @return 缓存的key
     */
    String getCacheKey(Method method, Object[] args, NetMethod apiMethod);

}
