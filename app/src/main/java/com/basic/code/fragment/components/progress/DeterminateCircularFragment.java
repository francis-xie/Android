package com.basic.code.fragment.components.progress;

import android.animation.ValueAnimator;
import android.widget.ProgressBar;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Animators;

import butterknife.BindViews;

/**
 
 * @date 2017/12/8 上午12:14
 */
@Page(name = "环形进度条样式")
public class DeterminateCircularFragment extends BaseFragment {
    @BindViews({
            R.id.normal_progress,
            R.id.tinted_normal_progress,
            R.id.dynamic_progress,
            R.id.tinted_dynamic_progress
    })
    ProgressBar[] mPrimaryProgressBars;
    @BindViews({
            R.id.normal_secondary_progress,
            R.id.normal_background_progress,
            R.id.tinted_normal_secondary_progress,
            R.id.tinted_normal_background_progress,
            R.id.dynamic_secondary_progress,
            R.id.dynamic_background_progress,
            R.id.tinted_dynamic_secondary_progress,
            R.id.tinted_dynamic_background_progress
    })
    ProgressBar[] mPrimaryAndSecondaryProgressBars;

    private ValueAnimator mPrimaryProgressAnimator;
    private ValueAnimator mPrimaryAndSecondaryProgressAnimator;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_determinatecircular;
    }

    @Override
    protected void initViews() {
        mPrimaryProgressAnimator =
                Animators.makeDeterminateCircularPrimaryProgressAnimator(mPrimaryProgressBars);
        mPrimaryAndSecondaryProgressAnimator =
                Animators.makeDeterminateCircularPrimaryAndSecondaryProgressAnimator(
                        mPrimaryAndSecondaryProgressBars);

        mPrimaryProgressAnimator.start();
        mPrimaryAndSecondaryProgressAnimator.start();
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void onDestroyView() {
        mPrimaryProgressAnimator.end();
        mPrimaryAndSecondaryProgressAnimator.end();
        super.onDestroyView();
    }
}
