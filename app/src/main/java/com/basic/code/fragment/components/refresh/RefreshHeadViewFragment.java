package com.basic.code.fragment.components.refresh;

import androidx.recyclerview.widget.RecyclerView;

import com.basic.refresh.layout.SmartRefreshLayout;
import com.basic.page.annotation.Page;
import com.basic.face.utils.WidgetUtils;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.RefreshHeadViewAdapter;
import com.basic.code.base.BaseFragment;

import java.util.Arrays;
import java.util.Collection;

import butterknife.BindView;

/**

 * @since 2019/3/31 下午11:36
 */
@Page(name = "增加HeadView和FootView\n嵌套轮播组件")
public class RefreshHeadViewFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    RefreshHeadViewAdapter mAdapter;
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_head_view;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(recyclerView);

        recyclerView.setAdapter(mAdapter = new RefreshHeadViewAdapter(DemoDataProvider.getBannerList()));
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            mAdapter.refresh(getData());
            refreshLayout.finishRefresh();
        }, 1000));
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> refreshLayout.finishLoadMore(1000));
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }

    private Collection<String> getData() {
        return Arrays.asList("这个是头部轮播图", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "这个是底部轮播图");
    }


    @Override
    public void onDestroyView() {
        mAdapter.recycle();
        super.onDestroyView();
    }
}

