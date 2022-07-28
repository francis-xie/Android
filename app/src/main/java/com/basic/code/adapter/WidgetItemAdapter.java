package com.basic.code.adapter;

import androidx.annotation.NonNull;

import com.basic.page.model.PageInfo;
import com.basic.face.adapter.recyclerview.BaseRecyclerAdapter;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.code.R;

import java.util.List;

public class WidgetItemAdapter extends BaseRecyclerAdapter<PageInfo> {

    public WidgetItemAdapter(List<PageInfo> list) {
        super(list);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.layout_widget_item;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, PageInfo item) {
        holder.text(R.id.item_name, item.getName());
        if (item.getExtra() != 0) {
            holder.image(R.id.item_icon, item.getExtra());
        }
    }

}
