package com.basic.code.widget;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.basic.refresh.layout.util.DensityUtil;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

/**
 * 简单的Material Design风格的加载进度条
 */
public class MaterialLoadMoreView extends ProgressBar implements SwipeRecyclerView.LoadMoreView {

    public MaterialLoadMoreView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setVisibility(GONE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        setPadding(0, DensityUtil.dp2px(10), 0, DensityUtil.dp2px(10));
        setLayoutParams(params);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLoading() {
        setVisibility(VISIBLE);
    }

    @Override
    public void onLoadFinish(boolean dataEmpty, boolean hasMore) {
        setVisibility(GONE);
    }

    @Override
    public void onWaitToLoadMore(SwipeRecyclerView.LoadMoreListener loadMoreListener) {

    }

    @Override
    public void onLoadError(int errorCode, String errorMessage) {
        setVisibility(GONE);
    }
}
