package com.basic.code.core.http.callback;

import androidx.annotation.NonNull;

import com.basic.code.core.BaseFragment;
import com.basic.code.utils.ToastUtils;
import com.basic.http2.callback.ProgressLoadingCallBack;
import com.basic.http2.exception.ApiException;
import com.basic.http2.model.HttpRequest;
import com.basic.http2.subsciber.impl.IProgressLoader;
import com.basic.tools.common.StringUtils;
import com.basic.tools.common.logger.Logger;

/**
 * 带错误toast提示和加载进度条的网络请求回调
 */
public abstract class TipProgressLoadingCallBack<T> extends ProgressLoadingCallBack<T> {
    /**
     * 记录一下请求的url,确定出错的请求是哪个请求
     */
    private String mUrl;

    public TipProgressLoadingCallBack(BaseFragment fragment) {
        super(fragment.getProgressLoader());
    }

    public TipProgressLoadingCallBack(IProgressLoader iProgressLoader) {
        super(iProgressLoader);
    }

    public TipProgressLoadingCallBack(@NonNull HttpRequest req, IProgressLoader iProgressLoader) {
        this(req.getUrl(), iProgressLoader);
    }

    public TipProgressLoadingCallBack(String url, IProgressLoader iProgressLoader) {
        super(iProgressLoader);
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
