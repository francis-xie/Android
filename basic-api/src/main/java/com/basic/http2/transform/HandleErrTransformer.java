package com.basic.http2.transform;

import com.basic.http2.transform.func.HttpResponseThrowableFunc;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * 将错误抛出
 */
public class HandleErrTransformer<T> implements ObservableTransformer<T, T> {
    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.onErrorResumeNext(new HttpResponseThrowableFunc<T>());
    }
}
