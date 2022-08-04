
package com.basic.aop.cache;

import java.lang.reflect.Type;

/**
 * <pre>
 *     desc   : 磁盘缓存管理器

 *     time   : 2018/4/24 上午9:26
 * </pre>
 */
public final class XDiskCache {

    private static XDiskCache sInstance;

    private XCache mXCache;

    private XDiskCache() {
        mXCache = XCache.newBuilder().isDiskCache(true).build();
    }

    /**
     * 获取磁盘缓存管理实例
     *
     * @return
     */
    public static XDiskCache getInstance() {
        if (sInstance == null) {
            synchronized (XDiskCache.class) {
                if (sInstance == null) {
                    sInstance = new XDiskCache();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化磁盘缓存
     * @param builder
     * @return
     */
    public XDiskCache init(XCache.Builder builder) {
        mXCache.init(builder);
        return this;
    }

    /**
     * 读取缓存
     *
     * @param key 缓存key
     */
    public <T> T load(final String key) {
        return mXCache.load(key);
    }

    /**
     * 读取缓存
     *
     * @param key  缓存key
     * @param time 保存时间
     */
    public <T> T load(final String key, final long time) {
        return mXCache.load(key, time);
    }

    /**
     * 读取缓存
     *
     * @param type 保存的类型
     * @param key  缓存key
     * @param time 保存时间
     */
    public <T> T load(final Type type, final String key, final long time) {
        return mXCache.load(type, key, time);
    }

    /**
     * 保存缓存
     *
     * @param key   缓存key
     * @param value 缓存Value
     */
    public <T> boolean save(final String key, final T value) {
        return mXCache.save(key, value);
    }

    /**
     * 是否包含
     */
    public boolean containsKey(final String key) {
        return mXCache.containsKey(key);
    }

    /**
     * 删除缓存
     */
    public boolean remove(final String key) {
        return mXCache.remove(key);
    }

    /**
     * 清空缓存
     */
    public boolean clear() {
        return mXCache.clear();
    }

}
