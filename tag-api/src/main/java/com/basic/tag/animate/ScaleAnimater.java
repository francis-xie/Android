package com.basic.tag.animate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Author jpeng
 * Date: 16-11-14
 * E-mail:peng8350@gmail.com
 * 实现图标缩放动画者
 */
public class ScaleAnimater implements Animatable {

    @Override
    public void onPressDown(View view, boolean selected) {
        view.setScaleX(1.1f);
        view.setScaleY(1.1f);
    }

    @Override
    public void onTouchOut(View view, boolean selected) {
        view.setScaleX(selected ? 1.2f : 1f);
        view.setScaleY(selected ? 1.2f : 1f);
    }

    @Override
    public void onSelectChanged(View view, boolean selected) {
        AnimatorSet scaleAnimator = new AnimatorSet();
        float end = selected ? 1.2f : 1f;
        ObjectAnimator scaleX;
        ObjectAnimator scaleY;
        scaleX = ObjectAnimator.ofFloat(view, "scaleX", end);
        scaleY = ObjectAnimator.ofFloat(view, "scaleY", end);
        scaleAnimator.playTogether(scaleX, scaleY);
        scaleAnimator.setDuration(300);
        scaleAnimator.start();
    }

    @Override
    public void onPageAnimate(View view, float offset) {
        view.setScaleX(offset * 0.2f + 1f);
        view.setScaleY(offset * 0.2f + 1f);
    }

    @Override
    public boolean isNeedPageAnimate() {
        return true;
    }

}