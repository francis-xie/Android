
package com.basic.code.widget;

import android.content.Context;
import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 *

 * @since 2019-05-11 18:15
 */
public class BottomBarBehavior extends CoordinatorLayout.Behavior<View> {

    public BottomBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        //这个方法是说明这个子控件是依赖AppBarLayout的
        return dependency instanceof AppBarLayout;
    }


    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, View child, View dependency) {
        //获取更随布局的顶部位置
        float translationY = Math.abs(dependency.getTop());
        child.setTranslationY(translationY);
        return true;
    }

}
