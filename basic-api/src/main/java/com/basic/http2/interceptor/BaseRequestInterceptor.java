package com.basic.http2.interceptor;

import okhttp3.Response;

/**
 * 基础请求拦截器
 */
public abstract class BaseRequestInterceptor extends BaseInterceptor {

    @Override
    protected Response onAfterRequest(Response response, Chain chain, String bodyString) {
        return null;
    }
}
