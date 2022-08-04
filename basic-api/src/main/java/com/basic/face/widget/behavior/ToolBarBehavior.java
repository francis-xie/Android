
package com.basic.face.widget.behavior;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Behavior for ToolBar
 */
public class ToolBarBehavior extends BaseBehavior {

    public ToolBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        if (canInit) {
            mAnimateHelper = TranslateAnimateHelper.get(child);
            canInit = false;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }


    @Override
    protected void onNestPreScrollInit(View child) {
        if (isFirstMove) {
            isFirstMove = false;
            mAnimateHelper.setStartY(child.getY());
            mAnimateHelper.setMode(TranslateAnimateHelper.MODE_TITLE);
        }
    }
}

