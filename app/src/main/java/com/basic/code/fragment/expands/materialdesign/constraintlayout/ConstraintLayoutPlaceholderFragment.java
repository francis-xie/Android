
package com.basic.code.fragment.expands.materialdesign.constraintlayout;

import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Placeholder;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "占位符Placeholder使用")
public class ConstraintLayoutPlaceholderFragment extends BaseFragment {
    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.place_holder)
    Placeholder placeHolder;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_constraint_placeholder;
    }

    @Override
    protected void initViews() {

    }

    @SingleClick
    @OnClick({R.id.tv_1, R.id.tv_2, R.id.tv_3})
    public void onViewClicked(View view) {
        //切换占位控件
        placeHolder.setContentId(view.getId());
        //切换动画
        TransitionManager.beginDelayedTransition(constraintLayout, new ChangeBounds()
                .setInterpolator(new OvershootInterpolator()).setDuration(1000));
    }
}
