package com.basic.http2.callback;

import com.google.gson.internal.$Gson$Types;
import com.basic.http2.callback.impl.IType;
import com.basic.http2.model.ApiResult;
import com.basic.http2.utils.TypeUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * <p>描述：提供Clazz回调代理</p>
 * 主要用于可以自定义ApiResult<br>
 */
public class CallClazzProxy<T extends ApiResult<R>, R> implements IType<T> {
    private Type type;

    public CallClazzProxy(Type type) {
        this.type = type;
    }

    public Type getCallType() {
        return type;
    }

    @Override
    public Type getType() {  //CallClazz代理方式，获取需要解析的Type
        Type typeArguments = ResponseBody.class;
        if (type != null) {
            typeArguments = type;
        }
        Type rawType = TypeUtils.findNeedType(getClass());
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) rawType).getRawType();
        }
        return $Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArguments);
    }

    @Override
    public Type getRawType() {
        return null;
    }
}
