
package com.basic.code.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.basic.face.utils.ThemeUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;

/**
 * 演示NestedScrollingParent2的使用
 */
public class StickyNavigationLayout extends LinearLayout implements NestedScrollingParent2 {

    private NestedScrollingParentHelper mNestedScrollingParentHelper;

    private View mTopView;
    private View mNavigationView;
    private ViewPager mViewPager;
    private OnScrollChangeListener mOnScrollChangeListener;

    /**
     * 父控件可以滚动的距离
     */
    private float mCanScrollDistance = 0f;

    public StickyNavigationLayout(Context context) {
        this(context, null);
    }

    public StickyNavigationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyNavigationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }


    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //如果子view欲向上滑动，则先交给父view滑动
        boolean hideTop = dy > 0 && getScrollY() < mCanScrollDistance;
        //如果子view欲向下滑动，必须要子view不能向下滑动后，才能交给父view滑动
        boolean showTop = dy < 0 && getScrollY() >= 0 && !target.canScrollVertically(-1);
        if (hideTop || showTop) {
            scrollBy(0, dy);
            // consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离
            consumed[1] = dy;
        }
    }


    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mNestedScrollingParentHelper.onStopNestedScroll(target, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (dyUnconsumed < 0) {
            //表示已经向下滑动到头，这里不用区分手势还是fling
            scrollBy(0, dyUnconsumed);
        }
    }

    /**
     * 嵌套滑动时，如果父View处理了fling,那子view就没有办法处理fling了，所以这里要返回为false
     */
    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView = findViewById(R.id.top_view);
        mNavigationView = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //先测量一次
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //ViewPager修改后的高度= 总高度-TabLayout高度
        ViewGroup.LayoutParams lp = mViewPager.getLayoutParams();
        lp.height = getMeasuredHeight() - mNavigationView.getMeasuredHeight() - getTitleBarHeight();
        mViewPager.setLayoutParams(lp);
        //因为ViewPager修改了高度，所以需要重新测量
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCanScrollDistance = mTopView.getMeasuredHeight() - getTitleBarHeight();
    }

    private int getTitleBarHeight() {
        return ThemeUtils.resolveDimension(getContext(), R.attr.face_actionbar_height) + TitleBar.getStatusBarHeight();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mCanScrollDistance) {
            y = (int) mCanScrollDistance;
        }
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScroll(y / mCanScrollDistance);
        }
        if (getScrollY() != y) {
            super.scrollTo(x, y);
        }
    }


    /**
     * 滑动监听
     */
    public interface OnScrollChangeListener {
        /**
         * 移动监听
         *
         * @param moveRatio 移动比例
         */
        void onScroll(float moveRatio);
    }

    public StickyNavigationLayout setOnScrollChangeListener(OnScrollChangeListener listener) {
        mOnScrollChangeListener = listener;
        return this;
    }

}
