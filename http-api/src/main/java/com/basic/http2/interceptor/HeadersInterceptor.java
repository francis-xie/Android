package com.basic.http2.interceptor;

import com.basic.http2.logs.HttpLog;
import com.basic.http2.model.HttpHeaders;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 配置公共头部
 */
public class HeadersInterceptor implements Interceptor {

    private HttpHeaders headers;

    public HeadersInterceptor(HttpHeaders headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (headers.headersMap.isEmpty()) {
            return chain.proceed(builder.build());
        }
        try {
            for (Map.Entry<String, String> entry : headers.headersMap.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue()).build();
            }
        } catch (Exception e) {
            HttpLog.e(e);
        }
        return chain.proceed(builder.build());

    }
}
