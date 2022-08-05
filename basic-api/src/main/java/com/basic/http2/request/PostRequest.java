package com.basic.http2.request;


import android.text.TextUtils;

import com.basic.http2.Http;
import com.basic.http2.cache.model.CacheMode;
import com.basic.http2.model.HttpRequest;

/**
 * post请求
 */
public class PostRequest extends BaseBodyRequest<PostRequest> {

    public PostRequest(String url) {
        super(url);
    }

    /**
     * 使用httpRequest来构建post请求
     * @param httpRequest 统一封装的请求实体对象
     */
    public PostRequest(HttpRequest httpRequest) {
        super(httpRequest.getUrl());
        initRequest(httpRequest);
    }

    /**
     * 初始化请求
     *
     * @param httpRequest
     */
    private void initRequest(HttpRequest httpRequest) {
        String baseUrl = httpRequest.getBaseUrl();
        String url = httpRequest.getUrl();
        long timeout = httpRequest.getTimeout();
        boolean accessToken = httpRequest.isAccessToken();
        CacheMode cacheMode = httpRequest.getCacheMode();

        if (!TextUtils.isEmpty(baseUrl)) {
            baseUrl(baseUrl);
        }
        if (!CacheMode.NO_CACHE.equals(cacheMode)) {
            cacheMode(cacheMode).cacheKey(url);
        }
        //如果超时时间小于等于0，使用默认的超时时间
        if (timeout <= 0) {
            timeout = Http.DEFAULT_TIMEOUT_MILLISECONDS;
        }
        accessToken(accessToken).timeOut(timeout).upJson(httpRequest.toString());
    }

}
