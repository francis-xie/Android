
package com.basic.code.fragment.components.refresh.sticky;

import androidx.recyclerview.widget.RecyclerView;

import com.basic.page.annotation.Page;
import com.basic.face.utils.WidgetUtils;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.StickyListAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;

import butterknife.BindView;

@Page(name = "StickyNestedScrollView\n粘顶嵌套滚动布局")
public class StickyNestedScrollViewFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private StickyListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sticky_nested_scrollview;
    }

    @Override
    protected void initViews() {
        // 注意，这里使用StickyNestedScrollView嵌套RecyclerView会导致RecyclerView复用机制失效，
        // 在数据量大的情况下，加载容易卡顿
        recyclerView.setNestedScrollingEnabled(false);
        WidgetUtils.initRecyclerView(recyclerView, 0);
        recyclerView.setAdapter(mAdapter = new StickyListAdapter());

        mAdapter.refresh(DemoDataProvider.getStickyDemoData());
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnItemClickListener((itemView, item, position) -> {
            if (item != null && item.getNewInfo() != null) {
                Utils.goWeb(getContext(), item.getNewInfo().getDetailUrl());
            }
        });
    }

}
