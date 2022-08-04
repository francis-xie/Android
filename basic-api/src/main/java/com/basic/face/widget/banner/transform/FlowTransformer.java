package com.basic.face.widget.banner.transform;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * 翻转切换
 */
public class FlowTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setRotationY(position * -30f);
    }
}
