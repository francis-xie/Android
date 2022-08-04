package com.basic.http2.interceptor;

import okhttp3.Request;

/**
 * 基础响应拦截器
 */
public abstract class BaseResponseInterceptor extends BaseInterceptor {
    @Override
    protected Request onBeforeRequest(Request request, Chain chain) {
        return null;
    }

}
