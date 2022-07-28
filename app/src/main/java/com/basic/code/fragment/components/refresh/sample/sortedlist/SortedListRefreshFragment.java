
package com.basic.code.fragment.components.refresh.sample.sortedlist;

import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SortedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.entity.NewInfo;
import com.basic.code.base.BaseFragment;
import com.basic.tools.common.logger.Logger;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import butterknife.BindView;

@Page(name = "SortedList 自动数据排序刷新")
public class SortedListRefreshFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    /**
     * 数据缓存
     */
    private SortedList<NewInfo> mData;

    private SortedListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_diffutil_refresh;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("刷新") {
            @SingleClick
            @Override
            public void performAction(View view) {
                mockRefresh();
            }
        });
        return titleBar;
    }

    /**
     * 模拟刷新
     */
    private void mockRefresh() {
        if (mData == null || mData.size() == 0) {
            return;
        }
        int position = getRandomInsertPosition(mData.size());
        Logger.e("动态更新的位置:" + position);
        mData.add(new NewInfo("Android", "这个是刷新更新的内容")
                .setSummary("这里是内容！～～～～～")
                .setID(mData.get(position).getID())
                .setDetailUrl("https://juejin.im/post/5b480b79e51d45190905ef44")
                .setImageUrl("https://user-gold-cdn.xitu.io/2018/7/13/16492d9b7877dc21?imageView2/0/w/1280/h/960/format/webp/ignore-error/1"));
    }

    private int getRandomInsertPosition(int listSize) {
        return (int) (Math.random() * 100) % listSize;
    }

    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new SortedListAdapter();
        mData = new SortedList<>(NewInfo.class, new SortedListCallback(mAdapter));
        mAdapter.setDataSource(mData);
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    protected void initListeners() {
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
        swipeRefreshLayout.postDelayed(() -> {
            mData.replaceAll(DemoDataProvider.getDemoNewInfos());
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}
