package com.basic.code.fragment.expands.materialdesign.behavior;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.basic.page.annotation.Page;
import com.basic.face.adapter.recyclerview.XLinearLayoutManager;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.NewsCardViewListAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;
import com.basic.code.widget.MaterialLoadMoreView;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import butterknife.BindView;

@Page(name = "RecyclerView + CardView\n常见的列表展示组合")
public class RecyclerViewBehaviorFragment extends BaseFragment {
    @BindView(R.id.toolbar_recycler_view)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fab_recycler_view)
    FloatingActionButton fab;

    private NewsCardViewListAdapter mAdapter;
    private boolean mEnableLoadMore;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_behavior_recyclerview;
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new XLinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new NewsCardViewListAdapter());
        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
    }

    @Override
    protected void initListeners() {
        toolbar.setNavigationOnClickListener(v -> popToBack());
        fab.setOnClickListener(v -> XToastUtils.toast("新建"));
        mAdapter.setOnItemClickListener((itemView, item, position) -> Utils.goWeb(getContext(), item.getDetailUrl()));
        // 刷新监听。
        swipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        refresh();
    }

    /**
     * 刷新。
     */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = this::loadData;

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    private void loadData() {
        if (swipeRefreshLayout == null) {
            return;
        }
        swipeRefreshLayout.postDelayed(() -> {
            mAdapter.refresh(DemoDataProvider.getDemoNewInfos());
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
            enableLoadMore();
        }, 1000);
    }

    /**
     * 确保有数据加载了才开启加载更多
     */
    private void enableLoadMore() {
        if (recyclerView != null && !mEnableLoadMore) {
            mEnableLoadMore = true;
            useMaterialLoadMore();
            // 加载更多的监听。
            recyclerView.setLoadMoreListener(mLoadMoreListener);
            recyclerView.loadMoreFinish(false, true);
        }
    }

    private void useMaterialLoadMore() {
        MaterialLoadMoreView loadMoreView = new MaterialLoadMoreView(getContext());
        recyclerView.addFooterView(loadMoreView);
        recyclerView.setLoadMoreView(loadMoreView);
    }

    /**
     * 加载更多。
     */
    private SwipeRecyclerView.LoadMoreListener mLoadMoreListener = new SwipeRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            if (swipeRefreshLayout == null) {
                return;
            }
            swipeRefreshLayout.postDelayed(() -> {
                mAdapter.loadMore(DemoDataProvider.getDemoNewInfos());
                if (recyclerView != null) {
                    recyclerView.loadMoreFinish(false, true);
                }
            }, 1000);
        }
    };

}
