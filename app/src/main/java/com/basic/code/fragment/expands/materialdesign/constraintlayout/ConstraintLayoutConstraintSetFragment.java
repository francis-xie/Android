
package com.basic.code.fragment.expands.materialdesign.constraintlayout;

import android.os.Build;
import android.transition.TransitionManager;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 
 * @since 2020-01-09 13:38
 */
@Page(name = "ConstraintSet实现切换动画")
public class ConstraintLayoutConstraintSetFragment extends BaseFragment {
    private boolean mShowBigImage = false;

    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;

    private ConstraintSet mConstraintSetNormal = new ConstraintSet();
    private ConstraintSet mConstraintSetBig = new ConstraintSet();

    @Override
    protected int getLayoutId() {
        return R.layout.layout_constraintset_normal;
    }

    @Override
    protected void initViews() {
        mConstraintSetNormal.clone(constraintLayout);
        mConstraintSetBig.load(getContext(), R.layout.layout_constraintset_big);
    }

    @SingleClick
    @OnClick(R.id.iv_content)
    public void onViewClicked(View view) {
        toggleMode();
    }

    public void toggleMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(constraintLayout);
        }
        mShowBigImage = !mShowBigImage;
        applyConfig();
    }

    private void applyConfig() {
        if (mShowBigImage) {
            mConstraintSetBig.applyTo(constraintLayout);
        } else {
            mConstraintSetNormal.applyTo(constraintLayout);
        }
    }
}
