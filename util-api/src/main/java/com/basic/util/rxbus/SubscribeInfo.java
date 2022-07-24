
package com.basic.util.rxbus;


import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

/**
 * 订阅信息
 *

 * @since 2018/3/3 下午11:43
 */
public final class SubscribeInfo<T> {
    /**
     * 订阅者
     */
    private Flowable<T> mFlowable;
    /**
     * 订阅信息
     */
    private Disposable mDisposable;

    public SubscribeInfo(Flowable<T> flowable) {
        mFlowable = flowable;
    }

    public Flowable<T> getFlowable() {
        return mFlowable;
    }

    public SubscribeInfo setFlowable(Flowable<T> flowable) {
        mFlowable = flowable;
        return this;
    }

    public Disposable getDisposable() {
        return mDisposable;
    }

    public SubscribeInfo setDisposable(Disposable disposable) {
        mDisposable = disposable;
        return this;
    }
}
