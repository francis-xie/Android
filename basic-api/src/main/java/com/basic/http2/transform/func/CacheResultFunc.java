package com.basic.http2.transform.func;

import com.basic.http2.cache.model.CacheResult;

import io.reactivex.functions.Function;

/**
 * 缓存结果转换
 */
public class CacheResultFunc<T> implements Function<CacheResult<T>, T> {

    @Override
    public T apply(CacheResult<T> tCacheResult) throws Exception {
        return tCacheResult.data;
    }
}
