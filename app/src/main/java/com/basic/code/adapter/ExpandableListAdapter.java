
package com.basic.code.adapter;

import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.aop.annotation.SingleClick;
import com.basic.face.adapter.recyclerview.BaseRecyclerAdapter;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.utils.ResUtils;
import com.basic.face.widget.layout.ExpandableLayout;
import com.basic.code.R;

import java.util.Collection;

/**
 * 可伸缩布局适配器
 */
public class ExpandableListAdapter extends BaseRecyclerAdapter<String> {

    private RecyclerView mRecyclerView;

    public ExpandableListAdapter(RecyclerView recyclerView, Collection<String> data) {
        super(data);
        mRecyclerView = recyclerView;
    }

    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.adapter_expandable_list_item;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position 索引
     * @param item     列表项
     */
    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, String item) {
        ExpandableLayout expandableLayout = holder.findViewById(R.id.expandable_layout);
        AppCompatImageView ivIndicator = holder.findViewById(R.id.iv_indicator);
        expandableLayout.setInterpolator(new OvershootInterpolator());
        expandableLayout.setOnExpansionChangedListener((expansion, state) -> {
            if (mRecyclerView != null && state == ExpandableLayout.State.EXPANDING) {
                mRecyclerView.smoothScrollToPosition(position);
            }
            if (ivIndicator != null) {
                ivIndicator.setRotation(expansion * 90);
            }
        });

        boolean isSelected = position == mSelectPosition;
        expandableLayout.setExpanded(isSelected, false);

        holder.select(R.id.fl_title, isSelected);
        holder.text(R.id.tv_title, ResUtils.getResources().getString(R.string.item_example_number_title, position + 1));
        holder.text(R.id.tv_content, ResUtils.getResources().getString(R.string.item_example_number_abstract, position + 1));
        holder.click(R.id.fl_title, new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                onClickItem(v, expandableLayout, position);
            }
        });
    }

    private void onClickItem(View view, final ExpandableLayout expandableLayout, final int position) {
        RecyclerViewHolder holder = (RecyclerViewHolder) mRecyclerView.findViewHolderForAdapterPosition(mSelectPosition);
        if (holder != null) {
            holder.select(R.id.fl_title, false);
            ((ExpandableLayout) holder.findViewById(R.id.expandable_layout)).collapse();
        }

        if (position == mSelectPosition) {
            mSelectPosition = -1;
        } else {
            view.setSelected(true);
            expandableLayout.expand();
            mSelectPosition = position;
        }
    }
}
