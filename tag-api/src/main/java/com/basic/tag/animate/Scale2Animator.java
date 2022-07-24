package com.basic.tag.animate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Author jpeng
 * Date: 17-9-3
 * E-mail:peng8350@gmail.com
 */
public class Scale2Animator implements Animatable {

    @Override
    public void onPressDown(View view, boolean selected) {
        if (!selected) {
            view.setScaleX(0.75f);
            view.setScaleY(0.75f);
        }
    }

    @Override
    public void onTouchOut(View view, boolean selected) {
        if (!selected) {
            view.setScaleX(1f);
            view.setScaleY(1f);
        }
    }

    @Override
    public void onSelectChanged(View view, boolean selected) {
        if (!selected) return;
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "scaleX", 0.75f, 1.3f, 1f, 1.2f, 1f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleY", 0.75f, 1.3f, 1f, 1.2f, 1f);
        set.playTogether(animator1, animator2);
        set.setDuration(800);
        set.start();

    }

    @Override
    public void onPageAnimate(View v, float offset) {

    }

    @Override
    public boolean isNeedPageAnimate() {
        return false;
    }
}
