package com.basic.code.fragment.components.statelayout;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.basic.page.annotation.Page;
import com.basic.face.widget.statelayout.MultipleStatusView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 自定义状态布局
 */
@Page(name = "MultipleStatusView\n自定义布局")
public class MultipleStatusViewFragment extends BaseFragment {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.fab_menu)
    FloatingActionMenu mFloatingActionMenu;

    private Handler mLoadingHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mMultipleStatusView.getViewStatus() == MultipleStatusView.STATUS_LOADING) {
                mMultipleStatusView.showContent();
            }
            return true;
        }
    });

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multiplestatusview;
    }

    @Override
    protected void initViews() {
        loading();
    }

    @Override
    protected void initListeners() {
        mMultipleStatusView.setOnRetryClickListener(mRetryClickListener);
    }

    final View.OnClickListener mRetryClickListener = (v -> {
        ToastUtils.toast("点击重试");
        loading();
    });

    void loading() {
        mMultipleStatusView.showLoading();
        mLoadingHandler.sendEmptyMessageDelayed(0, 5000);
    }

    @OnClick({R.id.fab_loading, R.id.fab_empty, R.id.fab_error, R.id.fab_no_network, R.id.fab_content})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_loading:
                loading();
                break;
            case R.id.fab_empty:
                mMultipleStatusView.showEmpty();
                break;
            case R.id.fab_error:
                mMultipleStatusView.showError();
                break;
            case R.id.fab_no_network:
                mMultipleStatusView.showNoNetwork();
                break;
            case R.id.fab_content:
                mMultipleStatusView.showContent();
                break;
            default:
                break;
        }
        mFloatingActionMenu.toggle(false);
    }

    @Override
    public void onDestroyView() {
        mLoadingHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }
}
