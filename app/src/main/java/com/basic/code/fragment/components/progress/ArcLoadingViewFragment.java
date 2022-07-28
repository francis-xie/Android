package com.basic.code.fragment.components.progress;

import android.view.View;
import android.widget.Button;

import com.basic.util.rxjava.RxJavaUtils;
import com.basic.page.annotation.Page;
import com.basic.face.utils.WidgetUtils;
import com.basic.face.widget.dialog.LoadingDialog;
import com.basic.face.widget.dialog.MiniLoadingDialog;
import com.basic.face.widget.progress.loading.ARCLoadingView;
import com.basic.face.widget.progress.loading.LoadingViewLayout;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "ARCLoadingView\n圆弧加载控件")
public class ArcLoadingViewFragment extends BaseFragment {

    @BindView(R.id.auto_arc_loading)
    ARCLoadingView mAutoLoadingView;
    @BindView(R.id.arc_loading)
    ARCLoadingView mLoadingView;

    @BindView(R.id.btn_switch)
    Button mBtSwitch;

    LoadingDialog mLoadingDialog;

    MiniLoadingDialog mMiniLoadingDialog;

    @BindView(R.id.loading_view_layout)
    LoadingViewLayout mLoadingViewLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_arcloadingview;
    }

    @Override
    protected void initViews() {
        mLoadingDialog = WidgetUtils.getLoadingDialog(getContext())
                .setIconScale(0.4F)
                .setLoadingSpeed(8);

        mMiniLoadingDialog =  WidgetUtils.getMiniLoadingDialog(getContext());
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

    @OnClick(R.id.btn_loading_dialog)
    void showLoadingDialog(View v) {
        mLoadingDialog.show();
        RxJavaUtils.delay(4, aLong -> mLoadingDialog.dismiss());
    }

    @OnClick(R.id.btn_loading_layout)
    void showLoadingLayout(View v) {
        mLoadingViewLayout.show();
        RxJavaUtils.delay(4, aLong -> mLoadingViewLayout.dismiss());
    }


    @OnClick(R.id.btn_test_dialog)
    void showTestDialog(View v) {
        mMiniLoadingDialog.show();
        RxJavaUtils.delay(4, aLong -> mMiniLoadingDialog.dismiss());
    }

    @Override
    public void onDestroyView() {
        mAutoLoadingView.recycle();
        mLoadingView.recycle();
        mLoadingDialog.recycle();
        mLoadingViewLayout.recycle();
        super.onDestroyView();
    }
}
