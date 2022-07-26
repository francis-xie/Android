package com.basic.http2.cache.stategy;

import com.basic.http2.cache.RxCache;
import com.basic.http2.cache.model.CacheResult;
import com.basic.http2.utils.HttpUtils;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import okio.ByteString;

/**
 * <p>描述:
 * 先使用缓存，不管是否存在，仍然请求网络，会先把缓存回调给你，
 * 等网络请求回来发现数据是一样的就不会再返回，否则再返回
 * （这样做的目的是防止数据是一样的你也需要刷新界面）
 * </p>
 * <-------此类加载用的是反射 所以类名是灰色的 没有直接引用  不要误删----------------><br>
 */
public final class CacheAndRemoteDistinctStrategy extends BaseStrategy {

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
                }).distinct(new Function<CacheResult<T>, String>() {
                    @Override
                    public String apply(@NonNull CacheResult<T> tCacheResult) throws Exception {
                        return ByteString.encodeUtf8(HttpUtils.toJson(tCacheResult.data)).md5().hex();
                    }
                });
    }

}
