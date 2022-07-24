package com.basic.code.fragment.expands.materialdesign.behavior;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.basic.router.annotation.AutoWired;
import com.basic.router.launcher.Router;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.NewsCardViewListAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import butterknife.BindView;

/**
 * @author XUE
 * @since 2019/5/9 11:54
 */
public class SimpleListFragment extends BaseFragment {

    private static final String KEY_IS_SPECIAL = "key_is_special";

    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private NewsCardViewListAdapter mAdapter;

    @AutoWired(name = KEY_IS_SPECIAL)
    boolean isSpecial;

    public static SimpleListFragment newInstance(boolean isSpecial) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_IS_SPECIAL, isSpecial);
        SimpleListFragment fragment = new SimpleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs() {
        Router.getInstance().inject(this);
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.include_recycler_view_refresh;
    }

    @Override
    protected void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new NewsCardViewListAdapter());
        mAdapter.refresh(isSpecial ? DemoDataProvider.getSpecialDemoNewInfos() : DemoDataProvider.getDemoNewInfos());

        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnItemClickListener((itemView, item, position) -> Utils.goWeb(getContext(), item.getDetailUrl()));
    }
}
