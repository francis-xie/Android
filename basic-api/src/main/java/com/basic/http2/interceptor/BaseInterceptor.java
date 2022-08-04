package com.basic.http2.interceptor;

import com.basic.http2.utils.HttpUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 基础拦截器
 */
public abstract class BaseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request;
        Request requestTmp = onBeforeRequest(chain.request(), chain);
        if (requestTmp != null) {
            request = requestTmp;
        } else {
            request = chain.request();
        }
        Response response = chain.proceed(request);
        boolean isText = HttpUtils.isText(getMediaType(response));
        if (!isText) {
            return response;
        }

        String bodyString = HttpUtils.getResponseBodyString(response);
        Response tmp = onAfterRequest(response, chain, bodyString);
        if (tmp != null) {
            return tmp;
        }
        return response;
    }

    private MediaType getMediaType(Response response) {
        if (response.body() != null) {
            return response.body().contentType();
        }
        return null;
    }

    /**
     * 请求拦截
     *
     * @param request 请求
     * @param chain   拦截链
     * @return {@code null} : 不进行拦截处理
     */
    protected abstract Request onBeforeRequest(Request request, Chain chain);

    /**
     * 响应拦截
     *
     * @param response   响应
     * @param chain      拦截链
     * @param bodyString 响应内容
     * @return {@code null} : 不进行拦截处理
     */
    protected abstract Response onAfterRequest(Response response, Chain chain, String bodyString);
}
