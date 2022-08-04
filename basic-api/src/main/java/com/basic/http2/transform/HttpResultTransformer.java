package com.basic.http2.transform;

import com.basic.http2.model.ApiResult;
import com.basic.http2.transform.func.HttpResponseThrowableFunc;
import com.basic.http2.transform.func.HttpResultFuc;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * 将解析到的ApiResult转化为T, 并将错误抛出
 */
public class HttpResultTransformer<T> implements ObservableTransformer<ApiResult<T>, T> {

    @Override
    public ObservableSource<T> apply(Observable<ApiResult<T>> upstream) {
        return upstream.map(new HttpResultFuc<T>())
                .onErrorResumeNext(new HttpResponseThrowableFunc<T>());
    }
}
