package com.basic.http2.transform;

import com.basic.http2.model.SchedulerType;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 网络请求线程切换调度
 */
public class HttpSchedulersTransformer<T> implements ObservableTransformer<T, T> {
    /**
     * 线程类型
     */
    private SchedulerType mSchedulerType;

    /**
     * 构造方法
     *
     * @param isSyncRequest  是否是同步请求
     * @param isOnMainThread 是否回到主线程
     */
    public HttpSchedulersTransformer(boolean isSyncRequest, boolean isOnMainThread) {
        mSchedulerType = getSchedulerType(isSyncRequest, isOnMainThread);
    }

    public HttpSchedulersTransformer(SchedulerType schedulerType) {
        mSchedulerType = schedulerType;
    }

    /**
     * 获取线程的类型
     *
     * @param isSyncRequest  是否是同步请求
     * @param isOnMainThread 是否回到主线程
     * @return
     */
    private SchedulerType getSchedulerType(boolean isSyncRequest, boolean isOnMainThread) {
        if (isSyncRequest) {
            // 同步请求
            if (isOnMainThread) {
                return SchedulerType._main;
            } else {
                return SchedulerType._io;
            }
        } else {
            // 异步请求,开启io线程
            if (isOnMainThread) {
                return SchedulerType._io_main;
            } else {
                return SchedulerType._io_io;
            }
        }
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        switch (mSchedulerType) {
            case _main:
                return upstream.observeOn(AndroidSchedulers.mainThread());
            case _io:
                return upstream;
            case _io_main:
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            case _io_io:
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io());
            default:
                break;
        }
        return upstream;
    }
}
