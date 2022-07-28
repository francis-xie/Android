package com.basic.http2.callback;

import com.basic.http2.callback.impl.IType;
import com.basic.http2.exception.ApiException;
import com.basic.http2.utils.TypeUtils;

import java.lang.reflect.Type;

/**
 * 网络请求回调
 */
public abstract class CallBack<T> implements IType<T> {

    public abstract void onStart();

    public abstract void onSuccess(T response) throws Throwable;

    public abstract void onError(ApiException e);

    public abstract void onCompleted();

    @Override
    public Type getType() {
        return TypeUtils.findNeedClass(getClass());
    }

    @Override
    public Type getRawType() {
        return TypeUtils.findRawType(getClass());
    }
}
