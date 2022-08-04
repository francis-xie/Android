package com.basic.http2.subsciber;

import com.basic.http2.exception.ApiException;
import com.basic.http2.exception.ApiExceptionHandler;
import com.basic.http2.logs.HttpLog;

import io.reactivex.observers.DisposableObserver;

/**
 * 基础的订阅者
 */
public abstract class BaseSubscriber<T> extends DisposableObserver<T> {

    public BaseSubscriber() {

    }

    @Override
    protected void onStart() {
        HttpLog.d("--> Subscriber is onStart");
    }

    @Override
    public void onComplete() {
        HttpLog.d("--> Subscriber is Complete");
    }

    @Override
    public void onNext(T t) {
        try {
            onSuccess(t);
        } catch (Throwable e) {
            e.printStackTrace();
            onError(e);
        }
    }

    @Override
    public final void onError(Throwable e) {
        HttpLog.e("--> Subscriber is onError");
        try {
            if (e instanceof ApiException) {
                HttpLog.e("--> e instanceof ApiException, message:" + e.getMessage());
                onError((ApiException) e);
            } else {
                HttpLog.e("--> e !instanceof ApiException, message:" + e.getMessage());
                onError(ApiExceptionHandler.handleException(e));
            }
        } catch (Throwable throwable) {  //防止onError中执行又报错导致rx.exceptions.OnErrorFailedException: Error occurred when trying to propagate error to Observer.onError问题
            e.printStackTrace();
        }
    }

    /**
     * 出错
     *
     * @param e 出错信息
     */
    protected abstract void onError(ApiException e);

    /**
     * 安全版的{@link #onNext},自动做了try-catch
     *
     * @param t
     */
    protected abstract void onSuccess(T t);

}
