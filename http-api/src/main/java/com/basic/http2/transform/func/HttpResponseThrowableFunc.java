package com.basic.http2.transform.func;

import com.basic.http2.exception.ApiExceptionHandler;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 网络响应异常转换处理
 */
public class HttpResponseThrowableFunc<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
        return Observable.error(ApiExceptionHandler.handleException(throwable));
    }
}
