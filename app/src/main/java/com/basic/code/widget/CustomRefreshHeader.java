
package com.basic.code.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.basic.refresh.layout.api.RefreshHeader;
import com.basic.refresh.layout.api.RefreshKernel;
import com.basic.refresh.layout.api.RefreshLayout;
import com.basic.refresh.layout.constant.RefreshState;
import com.basic.refresh.layout.constant.SpinnerStyle;
import com.basic.refresh.layout.internal.ArrowDrawable;
import com.basic.refresh.layout.internal.ProgressDrawable;
import com.basic.refresh.layout.util.DensityUtil;
import com.basic.face.FACE;
import com.basic.face.utils.ViewUtils;

/**
 * 自定义下拉刷新头
 *

 * @since 2019-07-22 23:50
 */
public class CustomRefreshHeader extends LinearLayout implements RefreshHeader {

    /**
     * 标题文本
     */
    private TextView mHeaderText;
    /**
     * 下拉箭头
     */
    private ImageView mArrowView;
    /**
     * 刷新动画视图
     */
    private ImageView mProgressView;
    /**
     * 刷新动画
     */
    private ProgressDrawable mProgressDrawable;

    public CustomRefreshHeader(Context context) {
        this(context, null);
    }

    public CustomRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        mHeaderText = new TextView(context);
        mProgressDrawable = new ProgressDrawable();
        mArrowView = new ImageView(context);
        mProgressView = new ImageView(context);
        mProgressView.setImageDrawable(mProgressDrawable);
        mArrowView.setImageDrawable(new ArrowDrawable());
        addView(mProgressView, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
        addView(mArrowView, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
        addView(new Space(context), DensityUtil.dp2px(20), DensityUtil.dp2px(20));
        addView(mHeaderText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setMinimumHeight(DensityUtil.dp2px(60));

        ViewUtils.setViewsFont(FACE.getDefaultTypeface(), mHeaderText);
    }

    @Override
    @NonNull
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
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int maxDragHeight) {
        mProgressDrawable.start();//开始动画
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        //停止动画
        mProgressDrawable.stop();
        //隐藏动画
        mProgressView.setVisibility(GONE);
        if (success) {
            mHeaderText.setText("数据同步完成");
        } else {
            mHeaderText.setText("数据同步失败");
        }
        //延迟500毫秒之后再弹回
        return 500;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mHeaderText.setText("下拉开始同步数据");
                //显示下拉箭头
                mArrowView.setVisibility(VISIBLE);
                //隐藏动画
                mProgressView.setVisibility(GONE);
                //还原箭头方向
                mArrowView.animate().rotation(0);
                break;
            case Refreshing:
                mHeaderText.setText("正在同步数据");
                //显示加载动画
                mProgressView.setVisibility(VISIBLE);
                //隐藏箭头
                mArrowView.setVisibility(GONE);
                break;
            case ReleaseToRefresh:
                mHeaderText.setText("释放立即同步数据");
                //显示箭头改为朝上
                mArrowView.animate().rotation(180);
                break;
            default:
                break;
        }
    }

    /**
     * 刷新更新文字
     *
     * @param message
     */
    public void refreshMessage(String message) {
        if (mProgressView.getVisibility() == VISIBLE) {
            if (mHeaderText != null) {
                mHeaderText.setText(message);
            }
        }
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
