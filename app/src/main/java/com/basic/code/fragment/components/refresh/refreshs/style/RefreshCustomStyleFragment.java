
package com.basic.code.fragment.components.refresh.refreshs.style;

import androidx.annotation.MainThread;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.refresh.layout.RefreshLayouts;
import com.basic.util.rxjava.DisposablePool;
import com.basic.util.rxjava.RxJavaUtils;
import com.basic.page.annotation.Page;
import com.basic.face.utils.WidgetUtils;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.SimpleRecyclerAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.widget.MaterialFooter;
import com.basic.code.widget.CustomRefreshHeader;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

@Page(name = "自定义下拉刷新样式")
public class RefreshCustomStyleFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayouts mRefreshLayout;

    private SimpleRecyclerAdapter mAdapter;

    private CustomRefreshHeader mRefreshHeader;

    private int progress = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_all_style;
    }

    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter = new SimpleRecyclerAdapter());

        mRefreshLayout.setRefreshHeader(mRefreshHeader = new CustomRefreshHeader(getContext()));

        mRefreshLayout.setRefreshFooter(new MaterialFooter(getContext()));
    }


    @Override
    protected void initListeners() {
        mRefreshLayout.setOnRefreshListener(refreshLayout -> handleRefresh());
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            mAdapter.loadMore(DemoDataProvider.getDemoData());
            refreshLayout.finishLoadMore();
        }, 2000));
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果


    }


    private void handleRefresh() {
        progress = 0;
        DisposablePool.get().add(RxJavaUtils.polling(0, 50, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (progress <= 100) {
                        updateProgress(progress++);
                    } else {
                        mAdapter.refresh(DemoDataProvider.getDemoData());
                        if (mRefreshLayout != null) {
                            mRefreshLayout.finishRefresh(true);
                        }
                        DisposablePool.get().remove("refresh_polling");
                    }

                }), "refresh_polling");
    }

    @MainThread
    private void updateProgress(int progress) {
        if (mRefreshHeader != null) {
            mRefreshHeader.refreshMessage("正在同步数据（" + progress + "%）");
        }
    }

    @Override
    public void onDestroyView() {
        DisposablePool.get().remove("refresh_polling");
        super.onDestroyView();
    }
}
