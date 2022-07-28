package com.basic.code.core.http.subscriber;

import androidx.annotation.NonNull;

import com.basic.code.core.BaseFragment;
import com.basic.code.utils.ToastUtils;
import com.basic.http2.exception.ApiException;
import com.basic.http2.model.HttpRequest;
import com.basic.http2.subsciber.ProgressLoadingSubscriber;
import com.basic.http2.subsciber.impl.IProgressLoader;
import com.basic.tools.common.StringUtils;
import com.basic.tools.common.logger.Logger;

/**
 * 带错误toast提示和加载进度条的网络请求订阅
 */
public abstract class TipProgressLoadingSubscriber<T> extends ProgressLoadingSubscriber<T> {

    /**
     * 记录一下请求的url,确定出错的请求是哪个请求
     */
    private String mUrl;

    public TipProgressLoadingSubscriber() {
        super();
    }

    public TipProgressLoadingSubscriber(BaseFragment fragment) {
        super(fragment.getProgressLoader());
    }

    public TipProgressLoadingSubscriber(IProgressLoader iProgressLoader) {
        super(iProgressLoader);
    }

    public TipProgressLoadingSubscriber(@NonNull HttpRequest req) {
        this(req.getUrl());
    }

    public TipProgressLoadingSubscriber(String url) {
        super();
        mUrl = url;
    }

    @Override
    public void onError(ApiException e) {
        super.onError(e);
        ToastUtils.error(e);
        if (!StringUtils.isEmpty(mUrl)) {
            Logger.e("网络请求的url:" + mUrl, e);
        } else {
            Logger.e(e);
        }
    }
}
