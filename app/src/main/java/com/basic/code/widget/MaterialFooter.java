
package com.basic.code.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.basic.refresh.layout.api.RefreshFooter;
import com.basic.refresh.layout.api.RefreshKernel;
import com.basic.refresh.layout.api.RefreshLayout;
import com.basic.refresh.layout.constant.RefreshState;
import com.basic.refresh.layout.constant.SpinnerStyle;
import com.basic.refresh.layout.util.DensityUtil;

/**
 * 自定义上拉加载
 *

 * @since 2019-08-03 11:14
 */
public class MaterialFooter extends ProgressBar implements RefreshFooter {

    public MaterialFooter(Context context) {
        this(context, null);
    }

    public MaterialFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setVisibility(GONE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        setPadding(0, DensityUtil.dp2px(10), 0, DensityUtil.dp2px(10));
        setLayoutParams(params);
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        //指定为平移，不能null
        return SpinnerStyle.Translate;
    }


    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        setVisibility(VISIBLE);
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        setVisibility(GONE);
        return 100;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

}
