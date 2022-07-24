
package com.basic.code.fragment.expands;


import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.dialog.bottomsheet.BottomSheet;
import com.basic.code.R;
import com.basic.code.adapter.CommonRecyclerViewAdapter;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;

/**
 * 使用 {@link SnapHelper} 实现 {@link RecyclerView} 按页滚动。
 *

 * @since 2019/1/3 下午4:49
 */
@Page(name = "SnapHelper使用", extra = R.drawable.ic_expand_snap_helper)
public class SnapHelperFragment extends BaseFragment {

    @BindView(R.id.pagerWrap)
    ViewGroup mPagerWrap;

    RecyclerView mRecyclerView;
    LinearLayoutManager mPagerLayoutManager;
    CommonRecyclerViewAdapter mRecyclerViewAdapter;
    SnapHelper mSnapHelper;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_topbar_overflow) {
            @Override
            public void performAction(View view) {
                showBottomSheetList();
            }
        });
        return titleBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pagerlayoutmanager;
    }

    @Override
    protected void initViews() {
        mRecyclerView = new RecyclerView(getContext());
        mPagerLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mPagerLayoutManager);
        mRecyclerViewAdapter = new CommonRecyclerViewAdapter();
        mRecyclerViewAdapter.setItemCount(10);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mPagerWrap.addView(mRecyclerView);
        // PagerSnapHelper每次只能滚动一个item;用LinearSnapHelper则可以一次滚动多个，并最终保证定位
        // mSnapHelper = new LinearSnapHelper();
        mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(mRecyclerView);
    }

    private void showBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .addItem("水平方向")
                .addItem("垂直方向")
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    switch (position) {
                        case 0:
                            mPagerLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                            break;
                        case 1:
                            mPagerLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            break;
                        default:
                            break;
                    }
                })
                .build()
                .show();
    }


}
