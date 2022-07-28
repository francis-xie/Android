
package com.basic.code.fragment.components.layout;

import androidx.recyclerview.widget.RecyclerView;

import com.basic.refresh.layout.RefreshLayouts;
import com.basic.page.annotation.Page;
import com.basic.face.utils.WidgetUtils;
import com.basic.face.widget.banner.widget.banner.SimpleImageBanner;
import com.basic.face.widget.layout.linkage.LinkageScrollLayout;
import com.basic.face.widget.layout.linkage.view.LinkageRecyclerView;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.CommonGridAdapter;
import com.basic.code.adapter.NewsCardViewListAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;

@Page(name = "LinkageScrollLayout\n内联布局")
public class LinkageScrollLayoutFragment extends BaseFragment {

    @BindView(R.id.sib_simple_usage)
    SimpleImageBanner sibSimpleUsage;
    @BindView(R.id.recyclerView)
    LinkageRecyclerView recyclerView;
    @BindView(R.id.lsl_container)
    LinkageScrollLayout lslContainer;
    @BindView(R.id.refreshLayout)
    RefreshLayouts refreshLayout;
    @BindView(R.id.recycler_head)
    RecyclerView recyclerHead;

    private NewsCardViewListAdapter mNewsListAdapter;
    private CommonGridAdapter mGridAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_linkage_scroll_layout;
    }

    @Override
    protected void initViews() {
        sibSimpleUsage.setSource(DemoDataProvider.getBannerList())
                .setOnItemClickListener((view, item, position) -> XToastUtils.toast("headBanner position--->" + position)).startScroll();
        WidgetUtils.initRecyclerView(recyclerView, 0);
        recyclerView.setAdapter(mNewsListAdapter = new NewsCardViewListAdapter());

        WidgetUtils.initGridRecyclerView(recyclerHead, 4, 0);
        recyclerHead.setAdapter(mGridAdapter = new CommonGridAdapter(true));
        mGridAdapter.refresh(DemoDataProvider.getGridItems(getContext()));
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            mNewsListAdapter.refresh(DemoDataProvider.getDemoNewInfos());
            refreshLayout.finishRefresh();
        }, 1000));
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            mNewsListAdapter.loadMore(DemoDataProvider.getDemoNewInfos());
            refreshLayout.finishLoadMore();
        }, 1000));
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果

        mNewsListAdapter.setOnItemClickListener((itemView, item, position) -> Utils.goWeb(getContext(), item.getDetailUrl()));
        mGridAdapter.setOnItemClickListener((itemView, item, position) -> XToastUtils.toast("点击了：" + item.getTitle()));
    }
}
