package com.basic.face.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.basic.face.R;

/**
 * 支持高度值为 wrap_content 的 {@link ScrollView}，解决原生 {@link ScrollView} 在设置高度为 wrap_content 时高度计算错误的 bug。
 */
public class FACEWrapContentScrollView extends FACEObservableScrollView {
    private int mMaxHeight = Integer.MAX_VALUE >> 2;

    public FACEWrapContentScrollView(Context context) {
        super(context);
    }

    public FACEWrapContentScrollView(Context context, int maxHeight) {
        super(context);
        mMaxHeight = maxHeight;
    }

    public FACEWrapContentScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public FACEWrapContentScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FACEWrapContentScrollView);
        if (ta != null) {
            mMaxHeight = ta.getDimensionPixelSize(R.styleable.FACEWrapContentScrollView_wcsv_max_height, mMaxHeight);
            ta.recycle();
        }
    }

    /**
     * 设置最大高度
     *
     * @param maxHeight 最大高度[px]
     */
    public void setMaxHeight(int maxHeight) {
        if (mMaxHeight != maxHeight) {
            mMaxHeight = maxHeight;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        int expandSpec;
        if (lp.height > 0 && lp.height <= mMaxHeight) {
            expandSpec = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
        } else {
            expandSpec = View.MeasureSpec.makeMeasureSpec(mMaxHeight, View.MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
