
package com.basic.code.fragment.components.statelayout.status;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.basic.face.widget.statelayout.StatusLoader;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.ToastUtils;

import static com.basic.face.widget.statelayout.StatusLoader.STATUS_LOADING;

/**
 * 基础StatusLoader页
 */
public abstract class BaseStatusLoaderFragment extends BaseFragment {

    private Handler mLoadingHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onDestroyView() {
        mLoadingHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    //=============StatusLoader================//

    protected StatusLoader.Holder mHolder;

    protected void initLoadingStatusViewIfNeed() {
        if (mHolder == null) {
            //此处设置需要包裹的布局
            mHolder = StatusLoader.from(getStatusLoaderAdapter())
                    .wrap(getWrapView())
                    .withRetry(this::onLoadRetry);
        }
    }


    /**
     * 获取被包裹的内容组件
     *
     * @return 被包裹的内容组件
     */
    protected abstract View getWrapView();

    /**
     * 获取状态加载适配器
     *
     * @return 状态加载适配器
     */
    protected abstract StatusLoader.Adapter getStatusLoaderAdapter();

    /**
     * 重试
     */
    protected void onLoadRetry(View view) {
        ToastUtils.toast("点击重试");
        showLoading();
    }

    /**
     * 显示加载页面
     */
    protected void showLoading() {
        initLoadingStatusViewIfNeed();
        // 模拟加载
        if (mHolder.getCurState() != STATUS_LOADING) {
            mLoadingHandler.postDelayed(() -> {
                if (mHolder.getCurState() == STATUS_LOADING) {
                    showContent();
                }
            }, 3000);
        }
        mHolder.showLoading();
    }

    /**
     * 显示内容
     */
    protected void showContent() {
        initLoadingStatusViewIfNeed();
        mHolder.showLoadSuccess();
    }

    /**
     * 显示出错页面
     */
    protected void showError() {
        initLoadingStatusViewIfNeed();
        mHolder.showLoadFailed();
    }

    /**
     * 显示空页面
     */
    protected void showEmpty() {
        initLoadingStatusViewIfNeed();
        mHolder.showEmpty();
    }

    /**
     * 显示自定义布局页面
     */
    protected void showCustom() {
        initLoadingStatusViewIfNeed();
        mHolder.showCustom();
    }

}
