
package com.basic.face.widget.slideback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 处理事件拦截的Layout
 *

 * @since 2019-08-30 9:31
 */
public class SlideBackInterceptLayout extends FrameLayout {

    /**
     * 边缘滑动响应距离
     */
    private float mSideSlideLength = 0;

    public SlideBackInterceptLayout(Context context) {
        this(context, null);
    }

    public SlideBackInterceptLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideBackInterceptLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return ev.getAction() == MotionEvent.ACTION_DOWN && (ev.getRawX() <= mSideSlideLength || ev.getRawX() >= getWidth() - mSideSlideLength);
    }

    public void setSideSlideLength(float sideSlideLength) {
        mSideSlideLength = sideSlideLength;
    }
}