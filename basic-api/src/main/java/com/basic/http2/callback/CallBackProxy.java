package com.basic.http2.callback;

import androidx.annotation.NonNull;

import com.google.gson.internal.$Gson$Types;
import com.basic.http2.cache.model.CacheResult;
import com.basic.http2.callback.impl.IType;
import com.basic.http2.model.ApiResult;
import com.basic.http2.utils.TypeUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * <p>描述：提供回调代理</p>
 * 主要用于可以自定义ApiResult<br>
 */
public class CallBackProxy<T extends ApiResult<R>, R> implements IType<T> {
    CallBack<R> mCallBack;

    public CallBackProxy(@NonNull CallBack<R> callBack) {
        mCallBack = callBack;
    }

    public CallBack getCallBack() {
        return mCallBack;
    }

    @Override
    public Type getType() {
        // CallBack代理方式，获取需要解析的Type
        Type typeArguments = ResponseBody.class;
        if (mCallBack != null) {
            Type rawType = mCallBack.getRawType();
            // 如果用户的信息是返回List需单独处理
            if (List.class.isAssignableFrom(TypeUtils.getClass(rawType, 0)) || Map.class.isAssignableFrom(TypeUtils.getClass(rawType, 0))) {
                typeArguments = mCallBack.getType();
            } else if (CacheResult.class.isAssignableFrom(TypeUtils.getClass(rawType, 0))) {
                Type type = mCallBack.getType();
                typeArguments = TypeUtils.getParameterizedType(type, 0);
            } else {
                Type type = mCallBack.getType();
                typeArguments = TypeUtils.getClass(type, 0);
            }
        }
        Type rawType = TypeUtils.findNeedType(getClass());
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) rawType).getRawType();
        }
        return $Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArguments);
    }

    @Override
    public Type getRawType() {
        return mCallBack.getRawType();
    }
}
