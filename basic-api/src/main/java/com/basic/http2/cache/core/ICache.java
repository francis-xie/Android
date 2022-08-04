package com.basic.http2.cache.core;

import java.lang.reflect.Type;

/**
 * 缓存需要实现的接口
 */
public interface ICache {

    /**
     * 读取缓存
     * @param type 对象的类型
     * @param key
     * @param time 有效期
     * @return
     */
    <T> T load(Type type, String key, long time);

    /**
     * 保存缓存
     * @param key
     * @param value
     * @return
     */
    <T> boolean save(String key, T value);

    /**
     * 是否包含
     *
     * @param key
     * @return
     */
    boolean containsKey(String key);

    /**
     * 删除缓存
     *
     * @param key
     */
    boolean remove(String key);

    /**
     * 清空缓存
     */
    boolean clear();
}
