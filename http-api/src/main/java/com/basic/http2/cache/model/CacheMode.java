package com.basic.http2.cache.model;

/**
 * 网络请求策略
 */
public enum CacheMode {
    /**
     * 不使用缓存,该模式下,mCacheKey,cacheMaxAge 参数均无效
     **/
    NO_CACHE("NoStrategy"),
    /**
     * 完全按照HTTP协议的默认缓存规则，走OKhttp的Cache缓存
     **/
    DEFAULT("NoStrategy"),
    /**
     * 先请求网络，请求网络失败后再加载缓存
     */
    FIRST_REMOTE("FirstRemoteStrategy"),

    /**
     * 先加载缓存，缓存没有再去请求网络
     */
    FIRST_CACHE("FirstCacheStategy"),

    /**
     * 仅加载网络，但数据依然会被缓存
     */
    ONLY_REMOTE("OnlyRemoteStrategy"),

    /**
     * 只读取缓存
     */
    ONLY_CACHE("OnlyCacheStrategy"),

    /**
     * 先使用缓存，不管是否存在，仍然请求网络，会回调两次
     */
    CACHE_REMOTE("CacheAndRemoteStrategy"),
    /**
     * 先使用缓存，不管是否存在，仍然请求网络，会先把缓存回调给你，
     * 等网络请求回来发现数据是一样的就不会再返回，否则再返回
     * （这样做的目的是防止数据是一样的你也需要刷新界面）
     */
    CACHE_REMOTE_DISTINCT("CacheAndRemoteDistinctStrategy");

    private final String className;

    CacheMode(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
