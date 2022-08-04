package com.basic.http2.callback;

import com.basic.http2.exception.ApiException;
import com.basic.http2.subsciber.impl.IProgressLoader;
import com.basic.http2.subsciber.impl.OnProgressCancelListener;

import io.reactivex.disposables.Disposable;

/**
 * 描述：可以自定义进度加载的回调
 */
public abstract class ProgressLoadingCallBack<T> extends CallBack<T> implements OnProgressCancelListener {
    private IProgressLoader mIProgressLoader;
    private boolean mIsShowProgress = true;
    private Disposable mDisposable;

    public ProgressLoadingCallBack(IProgressLoader iProgressLoader) {
        mIProgressLoader = iProgressLoader;
        init(false);
    }

    /**
     * 自定义进度加载
     *
     * @param iProgressLoader
     * @param isShowProgress
     * @param isCancel
     */
    public ProgressLoadingCallBack(IProgressLoader iProgressLoader, boolean isShowProgress, boolean isCancel) {
        mIProgressLoader = iProgressLoader;
        mIsShowProgress = isShowProgress;
        init(isCancel);
    }

    /**
     * 初始化
     *
     * @param isCancel
     */
    private void init(boolean isCancel) {
        if (mIProgressLoader == null) {
            return;
        }
        mIProgressLoader.setCancelable(isCancel);
        if (isCancel) {
            mIProgressLoader.setOnProgressCancelListener(this);
        }
    }

    /**
     * 展示进度框
     */
    private void showProgress() {
        if (!mIsShowProgress) {
            return;
        }
        if (mIProgressLoader != null) {
            if (!mIProgressLoader.isLoading()) {
                mIProgressLoader.showLoading();
            }
        }
    }

    /**
     * 取消进度框
     */
    private void dismissProgress() {
        if (!mIsShowProgress) {
            return;
        }
        if (mIProgressLoader != null) {
            if (mIProgressLoader.isLoading()) {
                mIProgressLoader.dismissLoading();
            }
        }
    }

    @Override
    public void onStart() {
        showProgress();
    }

    @Override
    public void onCompleted() {
        dismissProgress();
    }

    @Override
    public void onError(ApiException e) {
        dismissProgress();
    }

    @Override
    public void onCancelProgress() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public void subscription(Disposable disposable) {
        mDisposable = disposable;
    }
}
