package com.basic.http2.cache.core;

import android.util.LruCache;

import java.lang.reflect.Type;

/**
 * 内存缓存实现类
 */
public class LruMemoryCache implements ICache {

    /**
     * 内存缓存
     */
    private LruCache<String, Object> mMemoryCache;

    /**
     * 初始化内存缓存
     *
     * @param cacheSize 最大内存大小
     */
    public LruMemoryCache(int cacheSize) {
        mMemoryCache = new LruCache<>(cacheSize);
    }

    /**
     * 读取缓存
     *
     * @param type 对象的类型
     * @param key
     * @param time 有效期
     * @return
     */
    @Override
    public <T> T load(Type type, String key, long time) {
        if (mMemoryCache == null) {
            return null;
        }
        return (T) mMemoryCache.get(key);
    }

    /**
     * 保存缓存
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public <T> boolean save(String key, T value) {
        return mMemoryCache != null && mMemoryCache.put(key, value) != null;
    }

    /**
     * 是否包含
     *
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(String key) {
        return mMemoryCache != null && mMemoryCache.get(key) != null;
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    @Override
    public boolean remove(String key) {
        return mMemoryCache != null && mMemoryCache.remove(key) != null;
    }

    /**
     * 清空缓存
     */
    @Override
    public boolean clear() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
            return true;
        }
        return false;
    }
}
