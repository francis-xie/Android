package com.basic.code.fragment.components.progress;

import android.animation.ValueAnimator;
import android.widget.ProgressBar;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Animators;

import butterknife.BindViews;

/**

 * @date 2017/12/7 下午6:05
 */
@Page(name = "MaterialProgressBar\n进度条")
public class MaterialProgressBarFragment extends BaseFragment {
    @BindViews({
            R.id.determinate_circular_large_progress,
            R.id.determinate_circular_progress,
            R.id.determinate_circular_small_progress
    })
    ProgressBar[] mDeterminateCircularProgressBars;

    private ValueAnimator mDeterminateCircularProgressAnimator;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_material_progress_bar;
    }

    @Override
    protected void initViews() {
        mDeterminateCircularProgressAnimator =
                Animators.makeDeterminateCircularPrimaryProgressAnimator(
                        mDeterminateCircularProgressBars);

        mDeterminateCircularProgressAnimator.start();
    }

    @Override
    protected void initListeners() {


    }

    @Override
    public void onDestroyView() {
        mDeterminateCircularProgressAnimator.end();
        super.onDestroyView();
    }

}
