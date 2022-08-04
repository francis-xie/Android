package com.basic.http2.cache.stategy;

import com.basic.http2.cache.RxCache;
import com.basic.http2.cache.model.CacheResult;
import com.basic.http2.logs.HttpLog;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 实现缓存策略的基类
 */
public abstract class BaseStrategy implements IStrategy {
    /**
     * 加载本地缓存
     *
     * @param rxCache
     * @param type
     * @param key
     * @param time      缓存的有效时间
     * @param needEmpty 出错是否发射空，保证链路正常执行
     * @param <T>
     * @return
     */
    <T> Observable<CacheResult<T>> loadCache(final RxCache rxCache, Type type, final String key, final long time, final boolean needEmpty) {
        Observable<CacheResult<T>> observable = rxCache.<T>load(type, key, time)
                .flatMap(new Function<T, ObservableSource<CacheResult<T>>>() {
                    @Override
                    public ObservableSource<CacheResult<T>> apply(@NonNull T t) throws Exception {
                        if (t == null) {
                            return Observable.error(new NullPointerException("Not find the cache!"));
                        }
                        return Observable.just(new CacheResult<T>(true, t));
                    }
                });
        if (needEmpty) {
            observable = observable
                    .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends CacheResult<T>>>() {
                        @Override
                        public ObservableSource<? extends CacheResult<T>> apply(@NonNull Throwable throwable) throws Exception {
                            return Observable.empty(); //直接调用onComplete。
                        }
                    });
        }
        return observable;
    }

    /**
     * 保存并读取远程请求的数据
     *
     * @param rxCache
     * @param key
     * @param source
     * @param needEmpty 出错是否发射空，保证链路正常执行
     * @param <T>
     * @return
     */
    <T> Observable<CacheResult<T>> loadRemote(final RxCache rxCache, final String key, Observable<T> source, final boolean needEmpty) {
        Observable<CacheResult<T>> observable = source
                .flatMap(new Function<T, ObservableSource<CacheResult<T>>>() {
                    @Override
                    public ObservableSource<CacheResult<T>> apply(final @NonNull T t) throws Exception {
                        return rxCache.save(key, t).map(new Function<Boolean, CacheResult<T>>() {
                            @Override
                            public CacheResult<T> apply(@NonNull Boolean aBoolean) throws Exception {
                                HttpLog.i("save status => " + aBoolean);
                                return new CacheResult<T>(false, t);
                            }
                        }).onErrorReturn(new Function<Throwable, CacheResult<T>>() {
                            @Override
                            public CacheResult<T> apply(@NonNull Throwable throwable) throws Exception {
                                HttpLog.i("save status => " + throwable);
                                return new CacheResult<T>(false, t);
                            }
                        });
                    }
                });
        if (needEmpty) {
            observable = observable
                    .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends CacheResult<T>>>() {
                        @Override
                        public ObservableSource<? extends CacheResult<T>> apply(@NonNull Throwable throwable) throws Exception {
                            return Observable.empty();
                        }
                    });
        }
        return observable;
    }
}
