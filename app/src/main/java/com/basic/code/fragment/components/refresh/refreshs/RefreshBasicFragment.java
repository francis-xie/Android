
package com.basic.code.fragment.components.refresh.refreshs;

import androidx.recyclerview.widget.RecyclerView;

import com.basic.refresh.layout.api.RefreshLayout;
import com.basic.page.annotation.Page;
import com.basic.face.utils.WidgetUtils;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.SimpleRecyclerAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;
/**
 
 * @since 2018/12/6 下午5:57
 */
@Page(name = "下拉刷新基础用法\n上拉加载、下拉刷新、自动刷新和点击事件")
public class RefreshBasicFragment extends BaseFragment {

    private SimpleRecyclerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_basic;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        WidgetUtils.initRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter = new SimpleRecyclerAdapter());

        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        // 开启自动加载功能（非必须）
        refreshLayout.setEnableAutoLoadMore(true);
        // 下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout12 -> refreshLayout12.getLayout().postDelayed(() -> {
            mAdapter.refresh(DemoDataProvider.getDemoData());
            refreshLayout12.finishRefresh();
            refreshLayout12.resetNoMoreData();//setNoMoreData(false);
        }, 2000));
        // 上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> refreshLayout1.getLayout().postDelayed(() -> {
            if (mAdapter.getItemCount() > 30) {
                XToastUtils.toast("数据全部加载完毕");
                refreshLayout1.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            } else {
                mAdapter.loadMore(DemoDataProvider.getDemoData());
                refreshLayout1.finishLoadMore();
            }
        }, 2000));

        // 触发自动刷新
        refreshLayout.autoRefresh();
        // item 点击测试
        mAdapter.setOnItemClickListener((itemView, item, position) -> XToastUtils.toast("点击:" + position));

        mAdapter.setOnItemLongClickListener((itemView, item, position) -> XToastUtils.toast("长按:" + position));

//        // 点击测试
//        RefreshFooter footer = refreshLayout.getRefreshFooter();
//        if (footer != null) {
//            refreshLayout.getRefreshFooter().getView().findViewById(ClassicsFooter.ID_TEXT_TITLE).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    XToastUtils.toast("已经到底了！");
//                }
//            });
//        }
    }

}
