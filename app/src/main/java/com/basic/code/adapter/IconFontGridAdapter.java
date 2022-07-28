
package com.basic.code.adapter;

import androidx.annotation.NonNull;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.basic.face.adapter.recyclerview.BaseRecyclerAdapter;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.code.R;
import com.basic.code.widget.iconfont.FACEIconFont;

public class IconFontGridAdapter extends BaseRecyclerAdapter<FACEIconFont.Icon> {

    public IconFontGridAdapter(FACEIconFont.Icon[] data) {
        super(data);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.layout_widget_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, FACEIconFont.Icon item) {
        if (item != null) {
            holder.text(R.id.item_name, item.getName());

            IconicsDrawable drawable = new IconicsDrawable(holder.getContext())
                    .icon(item)
                    .size(IconicsSize.dp(80));
            holder.image(R.id.item_icon, drawable);
        }
    }
}
