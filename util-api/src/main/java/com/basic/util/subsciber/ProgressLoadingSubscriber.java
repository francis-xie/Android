
package com.basic.util.subsciber;

import com.basic.util.exception.RxException;
import com.basic.util.logs.RxLog;
import com.basic.util.subsciber.impl.IProgressLoader;
import com.basic.util.subsciber.impl.OnProgressCancelListener;

import org.reactivestreams.Subscription;

/**
 * 实现带有进度加载的订阅【进度条加载者实现接口】
 *

 * @since 2018/6/10 下午9:27
 */
public abstract class ProgressLoadingSubscriber<T> extends BaseSubscriber<T> implements OnProgressCancelListener {
    /**
     * 进度加载提示
     */
    private IProgressLoader mIProgressLoader;

    private boolean mIsShowProgress = true;

    /**
     * 默认不显示弹出框，不可以取消
     */
    public ProgressLoadingSubscriber() {
        super();
        init(false);
    }

    /**
     * 自定义加载进度框
     *
     * @param iProgressLoader 自定义加载
     */
    public ProgressLoadingSubscriber(IProgressLoader iProgressLoader) {
        super();
        mIProgressLoader = iProgressLoader;
        init(false);
    }

    /**
     * 自定义加载进度框,可以设置是否显示弹出框，是否可以取消
     *
     * @param iProgressLoader
     * @param isShowProgress
     * @param isCancel
     */
    public ProgressLoadingSubscriber(IProgressLoader iProgressLoader, boolean isShowProgress, boolean isCancel) {
        super();
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
    public void onStart()  {
        super.onStart();
        showProgress();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        dismissProgress();
    }

    @Override
    public void onError(RxException e) {
        RxLog.e(e);
        dismissProgress();
    }

    @Override
    public void onCancelProgress() { //取消loading弹窗时进行订阅取消
        if (!isDisposed()) {
            dispose();
        }
    }
}
