
package com.basic.code.fragment.utils.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 
 * @since 2020/4/2 12:43 AM
 */
@Page(name = "控件自定义动画")
public class ViewCustomAnimationFragment extends BaseFragment {


    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.iv_add)
    AppCompatImageView ivAdd;

    private boolean isExpand;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_view_custom_animation;
    }

    @Override
    protected void initViews() {

    }

    @SingleClick
    @OnClick({R.id.iv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                switchAnimation();
                break;
            default:
                break;
        }
    }

    /**
     * 切换旋转动画
     */
    private void switchAnimation() {
        ObjectAnimator rotation;
        ObjectAnimator alpha;

        if (isExpand) {
            rotation = ObjectAnimator.ofFloat(ivAdd, "rotation", -45, 0);
            alpha = ObjectAnimator.ofFloat(llContainer, "alpha", 1, 0);
        } else {
            rotation = ObjectAnimator.ofFloat(ivAdd, "rotation", 0, -45);
            alpha = ObjectAnimator.ofFloat(llContainer, "alpha", 0, 1);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alpha).with(rotation);
        animatorSet.setDuration(500).start();
        isExpand = !isExpand;
    }
}
