package com.basic.http2.subsciber;

import com.basic.http2.callback.CallBack;
import com.basic.http2.callback.ProgressLoadingCallBack;
import com.basic.http2.exception.ApiException;

/**
 * 带有callBack的回调【如果作用是不需要用户订阅，只要实现callback回调】
 */
public class CallBackSubscriber<T> extends BaseSubscriber<T> {

    private CallBack<T> mCallBack;

    public CallBackSubscriber(CallBack<T> callBack) {
        super();
        mCallBack = callBack;
        if (callBack instanceof ProgressLoadingCallBack) {
            ((ProgressLoadingCallBack) callBack).subscription(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mCallBack != null) {
            mCallBack.onStart();
        }
    }

    @Override
    public void onError(ApiException e) {
        if (mCallBack != null) {
            mCallBack.onError(e);
        }
    }

    @Override
    public void onSuccess(T t) {
        try {
            if (mCallBack != null) {
                mCallBack.onSuccess(t);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            onError(e);
        }

    }

    @Override
    public void onComplete() {
        super.onComplete();
        if (mCallBack != null) {
            mCallBack.onCompleted();
        }
    }

}
