package com.basic.http2.cache.stategy;


import com.basic.http2.cache.RxCache;
import com.basic.http2.cache.model.CacheResult;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * <p>描述：先显示缓存，再请求网络</p>
 * <-------此类加载用的是反射 所以类名是灰色的 没有直接引用  不要误删----------------><br>
 */
public final class CacheAndRemoteStrategy extends BaseStrategy {
    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, long time, Observable<T> source, Type type) {
        Observable<CacheResult<T>> cache = loadCache(rxCache, type, key, time, true);
        Observable<CacheResult<T>> remote = loadRemote(rxCache, key, source, false);
        return Observable.concat(cache, remote)
                .filter(new Predicate<CacheResult<T>>() {
                    @Override
                    public boolean test(@NonNull CacheResult<T> tCacheResult) throws Exception {
                        return tCacheResult != null && tCacheResult.data != null;
                    }
                });
    }

}
