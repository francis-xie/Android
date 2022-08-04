
package com.basic.code.fragment.components.refresh.sticky;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.adapter.recyclerview.sticky.StickyHeadContainer;
import com.basic.face.adapter.recyclerview.sticky.StickyItemDecoration;
import com.basic.face.utils.WidgetUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.StickyListAdapter;
import com.basic.code.adapter.entity.StickyItem;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.basic.code.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "StickyItemDecoration\n通过装饰实现粘顶效果")
public class StickyItemDecorationFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.sticky_container)
    StickyHeadContainer stickyContainer;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private StickyListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sticky_item_decoration;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("清除") {
            @Override
            public void performAction(View view) {
                mAdapter.clear();
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {
        stickyContainer.setOnStickyPositionChangedListener(position -> {
            StickyItem item = mAdapter.getItem(position);
            if (item != null) {
                tvTitle.setText(item.getHeadTitle());
            }
        });

        WidgetUtils.initRecyclerView(recyclerView, 0);
        StickyItemDecoration stickyItemDecoration = new StickyItemDecoration(stickyContainer, StickyListAdapter.TYPE_HEAD_STICKY);
        recyclerView.addItemDecoration(stickyItemDecoration);
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

    @SingleClick
    @OnClick(R.id.tv_action)
    public void onViewClicked(View view) {
        ToastUtils.toast("点击更多");
    }


    @Override
    public void onDestroyView() {
        stickyContainer.recycle();
        super.onDestroyView();
    }
}
