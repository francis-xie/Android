
package com.basic.code.fragment.components.statelayout;

import android.view.View;

import com.basic.page.annotation.Page;
import com.basic.face.widget.popupwindow.status.Status;
import com.basic.face.widget.popupwindow.status.StatusView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "StatusView\n状态提示")
public class StatusViewFragment extends BaseFragment {

    @BindView(R.id.status)
    StatusView mStatusView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_status_view;
    }

    @Override
    protected void initViews() {
        mStatusView.setOnErrorClickListener(v -> {
            XToastUtils.toast("点击错误状态视图");
            mStatusView.dismiss();
        });

        mStatusView.setOnLoadingClickListener(v -> {
            XToastUtils.toast("点击正在加载状态视图");
            mStatusView.dismiss();
        });

        mStatusView.setOnCustomClickListener(v -> {
            XToastUtils.toast("点击自定义状态视图");
            mStatusView.dismiss();
        });
    }


    @OnClick({R.id.complete, R.id.error, R.id.loading, R.id.none, R.id.custom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.complete:
                mStatusView.setStatus(Status.COMPLETE);
                break;
            case R.id.error:
                mStatusView.setStatus(Status.ERROR);
                break;
            case R.id.loading:
                mStatusView.setStatus(Status.LOADING);
                break;
            case R.id.none:
                mStatusView.setStatus(Status.NONE);
                break;
            case R.id.custom:
                mStatusView.setStatus(Status.CUSTOM);
                break;
            default:
                break;
        }
    }

}
