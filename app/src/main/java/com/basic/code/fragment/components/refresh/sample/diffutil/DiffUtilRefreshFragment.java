
package com.basic.code.fragment.components.refresh.sample.diffutil;

import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.basic.util.rxjava.RxJavaUtils;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.NewsCardViewListAdapter;
import com.basic.code.adapter.entity.NewInfo;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.basic.tools.common.logger.Logger;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Function;

/**

 * @since 2020/6/21 11:45 PM
 */
@Page(name = "DiffUtil 局部刷新")
public class DiffUtilRefreshFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    /**
     * 增加一个变量暂存newList
     */
    private List<NewInfo> mNewDatas;

    private NewsCardViewListAdapter mAdapter;

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
        if (mAdapter.getItemCount() == 0) {
            return;
        }
        mNewDatas = new ArrayList<>();
        for (NewInfo info : mAdapter.getData()) {
            //clone一遍旧数据 ，模拟刷新操作
            mNewDatas.add(info.clone().resetContent());
        }
        mNewDatas.get(0).setUserName("zhiqiang" + (int)(Math.random() * 10 + 5));
        int position = getRandomInsertPosition(mNewDatas.size());
        Logger.e("动态插入的位置:" + position);
        mNewDatas.add(position, new NewInfo("Android", "这个是刷新新增的内容")
                .setSummary("这里是内容！～～～～～")
                .setDetailUrl("https://juejin.im/post/5b480b79e51d45190905ef44")
                .setImageUrl("https://user-gold-cdn.xitu.io/2018/7/13/16492d9b7877dc21?imageView2/0/w/1280/h/960/format/webp/ignore-error/1"));

        RxJavaUtils.executeAsyncTask(mNewDatas, new Function<List<NewInfo>, DiffUtil.DiffResult>() {
            @Override
            public DiffUtil.DiffResult apply(List<NewInfo> newInfos) throws Exception {
                return DiffUtil.calculateDiff(new DiffUtilCallback(mAdapter.getData(), newInfos), true);
            }
        }, diffResult -> {
            if (mAdapter != null) {
                diffResult.dispatchUpdatesTo(mAdapter);
                mAdapter.resetDataSource(mNewDatas);
            }
        });

    }


    private int getRandomInsertPosition(int listSize) {
        return (int) (Math.random() * 100) % listSize;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_diffutil_refresh;
    }

    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new NewsCardViewListAdapter());
        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    protected void initListeners() {
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
        swipeRefreshLayout.postDelayed(() -> {
            mAdapter.refresh(DemoDataProvider.getDemoNewInfos());
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

}
