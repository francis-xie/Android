package com.basic.tag.animate;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AnticipateInterpolator;

/**
 * Author jpeng
 * Date: 16-11-15
 * E-mail:peng8350@gmail.com
 * 实现跳跃图标的动画类
 */
public class JumpAnimator implements Animatable {

    @Override
    public void onPressDown(View view, boolean selected) {
        view.setTranslationY(-3f);
    }

    @Override
    public void onTouchOut(View view, boolean selected) {
        view.setTranslationY(selected ? -10f : 0f);
    }

    @Override
    public void onSelectChanged(View v, boolean selected) {
        int end = selected ? -10 : 0;
        ObjectAnimator jumpAnimator = ObjectAnimator.ofFloat(v, "translationY", end);
        jumpAnimator.setDuration(300);
        jumpAnimator.setInterpolator(new AnticipateInterpolator());
        jumpAnimator.start();
    }

    @Override
    public void onPageAnimate(View view, float offset) {
        view.setTranslationY(offset * -10);
    }

    @Override
    public boolean isNeedPageAnimate() {
        return true;
    }

}
