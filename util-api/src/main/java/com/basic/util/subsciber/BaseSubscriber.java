
package com.basic.util.subsciber;

import com.basic.util.exception.RxException;
import com.basic.util.exception.RxExceptionHandler;
import com.basic.util.logs.RxLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.observers.DisposableObserver;


/**
 * 基础订阅者
 *

 * @since 2018/6/10 下午9:27
 */
public abstract class BaseSubscriber<T> extends DisposableObserver<T> {

    public BaseSubscriber() {

    }

    @Override
    protected void onStart() {
        RxLog.e("-->Subscriber is onStart");
    }

    @Override
    public void onComplete() {
        RxLog.e("-->Subscriber is Complete");
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
        RxLog.e("-->Subscriber is onError");
        try {
            if (e instanceof RxException) {
                RxLog.e("--> e instanceof RxException, message:" + e.getMessage());
                onError((RxException) e);
            } else {
                RxLog.e("e !instanceof RxException, message:" + e.getMessage());
                onError(RxExceptionHandler.handleException(e));
            }
        } catch (Throwable throwable) {  //防止onError中执行又报错导致rx.exceptions.OnErrorFailedException: Error occurred when trying to propagate error to Observer.onError问题
            e.printStackTrace();
        }
    }

    /**
     * 出错
     *
     * @param e
     */
    public abstract void onError(RxException e);

    /**
     * 安全版的{@link #onNext},自动做了try-catch
     *
     * @param t
     */
    public abstract void onSuccess(T t);

}
