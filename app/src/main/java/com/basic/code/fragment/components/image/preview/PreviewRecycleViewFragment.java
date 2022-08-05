
package com.basic.code.fragment.components.image.preview;

import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.refresh.layout.RefreshLayouts;
import com.basic.refresh.layout.api.RefreshLayout;
import com.basic.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.adapter.recyclerview.GridDividerItemDecoration;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.utils.DensityUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.image.preview.PreviewBuilder;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.PreviewRecycleAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.SettingSPUtils;
import com.basic.code.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;

@Page(name = "RecycleView 图片预览")
public class PreviewRecycleViewFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayouts mRefreshLayout;

    private GridLayoutManager mGridLayoutManager;

    private PreviewRecycleAdapter mAdapter;

    private int mPage = -1;

    private boolean mIsVideo = false;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("切换") {
            @Override
            public void performAction(View view) {
                onChanged(view);
            }
        });
        return titleBar;
    }

    @SingleClick
    private void onChanged(View view) {
        mIsVideo = !mIsVideo;
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_preview_recycleview;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mRecyclerView.setLayoutManager(mGridLayoutManager = new GridLayoutManager(getContext(), 2));
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(getContext(), 2, DensityUtils.dp2px(3)));

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter = new PreviewRecycleAdapter());
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }

    @Override
    protected void initListeners() {
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(final @NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    mPage++;
                    if (mPage < getMediaRes().size()) {
                        mAdapter.loadMore(getMediaRes().get(mPage));
                        refreshLayout.finishLoadMore();
                    } else {
                        ToastUtils.toast("数据全部加载完毕");
                        refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                    }
                }, 1000);
            }

            @Override
            public void onRefresh(final @NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    mPage = 0;
                    mAdapter.refresh(getMediaRes().get(mPage));
                    refreshLayout.finishRefresh();
                }, 1000);

            }
        });

        mAdapter.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<ImageViewInfo>() {
            @SingleClick
            @Override
            public void onItemClick(View itemView, ImageViewInfo item, int position) {
                computeBoundsBackward(mGridLayoutManager.findFirstVisibleItemPosition());
                PreviewBuilder.from(getActivity())
                        .setImgs(mAdapter.getData())
                        .setCurrentIndex(position)
                        .setSingleFling(true)
                        .setProgressColor(SettingSPUtils.getInstance().isUseCustomTheme() ? R.color.custom_color_main_theme : R.color.face_config_color_main_theme)
                        .setType(PreviewBuilder.IndicatorType.Number)
                        .start();
            }
        });
    }

    /**
     * 查找信息
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < mAdapter.getItemCount(); i++) {
            View itemView = mGridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView imageView = itemView.findViewById(R.id.iv);
                imageView.getGlobalVisibleRect(bounds);
            }
            mAdapter.getItem(i).setBounds(bounds);
        }
    }

    private List<List<ImageViewInfo>> getMediaRes() {
        if (mIsVideo) {
            return DemoDataProvider.sVideos;
        } else {
            return DemoDataProvider.sPics;
        }
    }
}
