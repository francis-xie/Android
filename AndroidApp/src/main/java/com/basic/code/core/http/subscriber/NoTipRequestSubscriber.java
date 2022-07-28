package com.basic.code.core.http.subscriber;

import com.basic.http2.exception.ApiException;
import com.basic.http2.model.HttpRequest;
import com.basic.http2.subsciber.BaseSubscriber;
import com.basic.tools.common.StringUtils;
import com.basic.tools.common.logger.Logger;

/**
 * 不带错误toast提示的网络请求订阅，只存储错误的日志
 */
public abstract class NoTipRequestSubscriber<T> extends BaseSubscriber<T> {

    /**
     * 记录一下请求的url,确定出错的请求是哪个请求
     */
    private String mUrl;

    public NoTipRequestSubscriber() {

    }

    public NoTipRequestSubscriber(HttpRequest req) {
        this(req.getUrl());
    }

    public NoTipRequestSubscriber(String url) {
        mUrl = url;
    }

    @Override
    public void onError(ApiException e) {
        if (!StringUtils.isEmpty(mUrl)) {
            Logger.e("网络请求的url:" + mUrl, e);
        } else {
            Logger.e(e);
        }
    }
}
