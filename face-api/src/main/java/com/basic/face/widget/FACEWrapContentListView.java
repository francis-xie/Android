package com.basic.face.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;

import com.basic.face.R;

/**
 * 支持高度值为 wrap_content 的 {@link ListView}，解决原生 {@link ListView} 在设置高度为 wrap_content 时高度计算错误的 bug。
 *

 * @since 2018/11/14 下午1:08
 */
public class FACEWrapContentListView extends ListView {
    private int mMaxHeight = Integer.MAX_VALUE >> 2;

    public FACEWrapContentListView(Context context) {
        super(context);
    }

    public FACEWrapContentListView(Context context, int maxHeight) {
        super(context);
        mMaxHeight = maxHeight;
    }

    public FACEWrapContentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public FACEWrapContentListView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FACEWrapContentListView);
        if (ta != null) {
            mMaxHeight = ta.getDimensionPixelSize(R.styleable.FACEWrapContentListView_wclv_max_height, mMaxHeight);
            ta.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
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
}