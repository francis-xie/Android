package com.basic.code.core.http.subscriber;


import androidx.annotation.NonNull;

import com.basic.code.utils.ToastUtils;
import com.basic.http2.exception.ApiException;
import com.basic.http2.model.HttpRequest;
import com.basic.http2.subsciber.BaseSubscriber;
import com.basic.tools.common.StringUtils;
import com.basic.tools.common.logger.Logger;

/**
 * 带错误toast提示的网络请求订阅
 */
public abstract class TipRequestSubscriber<T> extends BaseSubscriber<T> {
    /**
     * 记录一下请求的url,确定出错的请求是哪个请求
     */
    private String mUrl;

    public TipRequestSubscriber() {

    }

    public TipRequestSubscriber(@NonNull HttpRequest req) {
        this(req.getUrl());
    }

    public TipRequestSubscriber(String url) {
        mUrl = url;
    }


    @Override
    public void onError(ApiException e) {
        ToastUtils.error(e);
        if (!StringUtils.isEmpty(mUrl)) {
            Logger.e("网络请求的url:" + mUrl, e);
        } else {
            Logger.e(e);
        }
    }
}
