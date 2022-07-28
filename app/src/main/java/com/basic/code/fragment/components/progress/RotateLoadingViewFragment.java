package com.basic.code.fragment.components.progress;

import android.widget.Button;

import com.basic.page.annotation.Page;
import com.basic.face.widget.progress.loading.RotateLoadingView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;

@Page(name = "RotateLoadingView\n旋转加载控件")
public class RotateLoadingViewFragment extends BaseFragment {
    @BindView(R.id.auto_arc_loading)
    RotateLoadingView mAutoLoadingView;
    @BindView(R.id.arc_loading)
    RotateLoadingView mLoadingView;

    @BindView(R.id.btn_switch)
    Button mBtSwitch;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rotate_loadingview;
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initListeners() {
        mBtSwitch.setOnClickListener(v -> {
            if (mLoadingView.isStart()) {
                mBtSwitch.setText(R.string.tip_start);
                mLoadingView.stop();
                mAutoLoadingView.stop();
            } else {
                mBtSwitch.setText(R.string.tip_stop);
                mLoadingView.start();
                mAutoLoadingView.start();
            }
        });
    }


    @Override
    public void onDestroyView() {
        mAutoLoadingView.recycle();
        mLoadingView.recycle();
        super.onDestroyView();
    }
}
