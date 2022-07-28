
package com.basic.code.fragment.components.imageview.preview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.refresh.layout.RefreshLayouts;
import com.basic.refresh.layout.api.RefreshLayout;
import com.basic.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.adapter.recyclerview.XLinearLayoutManager;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.NineGridRecycleAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import java.util.List;

import butterknife.BindView;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

/**

 * @since 2018/12/9 下午11:23
 */
@Page(name = "NineGrid 九宫格预览")
public class NineGridImageViewFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayouts mRefreshLayout;

    private NineGridRecycleAdapter mAdapter;

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
        mRecyclerView.setLayoutManager(new XLinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        mRecyclerView.setAdapter(mAdapter = new NineGridRecycleAdapter());
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
                        XToastUtils.toast("数据全部加载完毕");
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
    }

    private List<List<NineGridInfo>> getMediaRes() {
        if (mIsVideo) {
            return DemoDataProvider.sNineGridVideos;
        } else {
            return DemoDataProvider.sNineGridPics;
        }
    }
}
