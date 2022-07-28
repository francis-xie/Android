package com.basic.http2.cache.core;

import com.basic.http2.logs.HttpLog;
import com.basic.http2.utils.Utils;

import java.lang.reflect.Type;

import okio.ByteString;

/**
 * 缓存核心管理类
 * <p>1.采用LruDiskCache</p>
 * <p>2.对Key进行MD5加密</p>
 */
public class CacheCore implements ICache {

    private ICache mCache;

    public CacheCore(ICache cache) {
        mCache = Utils.checkNotNull(cache, "ICache == null");
    }

    /**
     * 设置缓存实现接口
     * @param icache
     * @return
     */
    public CacheCore setICache(ICache icache) {
        mCache = icache;
        return this;
    }

    /**
     * 读取
     */
    @Override
    public <T> T load(Type type, String key, long time) {
        if (mCache != null) {
            String cacheKey = ByteString.of(key.getBytes()).md5().hex();
            HttpLog.d("loadCache  key=" + cacheKey);
            return mCache.load(type, cacheKey, time);
        }
        return null;
    }

    /**
     * 保存
     */
    @Override
    public <T> boolean save(String key, T value) {
        if (mCache != null) {
            String cacheKey = ByteString.of(key.getBytes()).md5().hex();
            HttpLog.d("saveCache  key=" + cacheKey);
            return mCache.save(cacheKey, value);
        }
        return false;
    }

    /**
     * 是否包含
     *
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(String key) {
        if (mCache != null) {
            String cacheKey = ByteString.of(key.getBytes()).md5().hex();
            HttpLog.d("containsCache  key=" + cacheKey);
            return mCache.containsKey(cacheKey);
        }
        return false;
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    @Override
    public boolean remove(String key) {
        String cacheKey = ByteString.of(key.getBytes()).md5().hex();
        HttpLog.d("removeCache  key=" + cacheKey);
        return mCache == null || mCache.remove(cacheKey);
    }

    /**
     * 清空缓存
     */
    @Override
    public boolean clear() {
        return mCache != null && mCache.clear();
    }

}
