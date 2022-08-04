package com.basic.http2.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 不加载缓存(不使用OKHttp自带的缓存)
 */
public class NoCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().header("Cache-Control", "no-cache").build();
        Response response = chain.proceed(request);
        response = response.newBuilder().header("Cache-Control", "no-cache").build();
        return response;
    }
}



