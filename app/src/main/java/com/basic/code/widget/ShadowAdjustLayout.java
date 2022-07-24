
package com.basic.code.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;

import com.basic.face.widget.layout.FACEFrameLayout;
import com.basic.code.R;

/**
 
 * @since 2019-06-02 17:12
 */
public class ShadowAdjustLayout extends FACEFrameLayout {

    private ViewDragHelper mViewDragHelper;


    public ShadowAdjustLayout(Context context) {
        this(context, null);
    }

    public ShadowAdjustLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return child.getId() == R.id.layout_for_test;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                return top;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
