
package com.basic.code.fragment.components.refresh.sample.edit;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.basic.refresh.layout.RefreshLayouts;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.utils.ViewUtils;
import com.basic.face.utils.WidgetUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.button.SmoothCheckBox;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "列表长按编辑案例")
public class NewsListEditFragment extends BaseFragment {


    @BindView(R.id.refreshLayout)
    RefreshLayouts refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private NewsListEditAdapter mAdapter;

    @BindView(R.id.fl_edit)
    FrameLayout flEdit;
    @BindView(R.id.scb_select_all)
    SmoothCheckBox scbSelectAll;

    private TextView mTvSwitch;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_list_edit;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        mTvSwitch = (TextView) titleBar.addAction(new TitleBar.TextAction(getString(R.string.title_enter_manage_mode)) {
            @SingleClick
            @Override
            public void performAction(View view) {
                if (mAdapter == null) {
                    return;
                }
                mAdapter.switchManageMode();
                refreshManageMode();
            }
        });
        return titleBar;
    }

    private void refreshManageMode() {
        if (mTvSwitch != null) {
            mTvSwitch.setText(mAdapter.isManageMode() ? R.string.title_exit_manage_mode : R.string.title_enter_manage_mode);
        }
        ViewUtils.setVisibility(flEdit, mAdapter.isManageMode());
    }

    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(recyclerView, 0);
        recyclerView.setAdapter(mAdapter = new NewsListEditAdapter(isSelectAll -> {
            if (scbSelectAll != null) {
                scbSelectAll.setCheckedSilent(isSelectAll);
            }
        }));
        scbSelectAll.setOnCheckedChangeListener((checkBox, isChecked) -> mAdapter.setSelectAll(isChecked));
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            mAdapter.refresh(DemoDataProvider.getDemoNewInfos());
            refreshLayout.finishRefresh();
        }, 1000));
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> refreshLayout.getLayout().postDelayed(() -> {
            mAdapter.loadMore(DemoDataProvider.getDemoNewInfos());
            refreshLayout.finishLoadMore();
        }, 1000));
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果

        mAdapter.setOnItemClickListener((itemView, item, position) -> {
            if (mAdapter.isManageMode()) {
                mAdapter.updateSelectStatus(position);
            } else {
                Utils.goWeb(getContext(), item.getDetailUrl());
            }
        });
        mAdapter.setOnItemLongClickListener((itemView, item, position) -> {
            if (!mAdapter.isManageMode()) {
                mAdapter.enterManageMode(position);
                refreshManageMode();
            }
        });
    }

    @SingleClick
    @OnClick(R.id.btn_submit)
    public void onViewClicked(View view) {
        XToastUtils.toast("选中了" + mAdapter.getSelectedIndexList().size() + "个选项！");

    }
}
