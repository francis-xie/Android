package com.basic.code.core.http.callback;

import com.basic.http2.callback.SimpleCallBack;
import com.basic.http2.exception.ApiException;
import com.basic.http2.model.HttpRequest;
import com.basic.tools.common.StringUtils;
import com.basic.tools.common.logger.Logger;

/**
 * 不带错误提示的网络请求回调
 */
public abstract class NoTipCallBack<T> extends SimpleCallBack<T> {

    /**
     * 记录一下请求的url,确定出错的请求是哪个请求
     */
    private String mUrl;

    public NoTipCallBack() {

    }

    public NoTipCallBack(HttpRequest req) {
        this(req.getUrl());
    }

    public NoTipCallBack(String url) {
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
