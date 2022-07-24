
package com.basic.code.adapter;

import androidx.annotation.NonNull;

import com.basic.face.adapter.recyclerview.BaseRecyclerAdapter;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.adapter.simple.AdapterItem;
import com.basic.face.widget.imageview.ImageLoader;
import com.basic.face.widget.imageview.RadiusImageView;
import com.basic.code.R;

/**
 * 普通九宫格
 *

 * @since 2020/3/11 8:33 PM
 */
public class CommonGridAdapter extends BaseRecyclerAdapter<AdapterItem> {

    private boolean mIsCircle;

    public CommonGridAdapter(boolean isCircle) {
        super();
        mIsCircle = isCircle;
    }


    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.adapter_common_grid_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, AdapterItem item) {
        if (item != null) {
            RadiusImageView imageView = holder.findViewById(R.id.riv_item);
            imageView.setCircle(mIsCircle);
            ImageLoader.get().loadImage(imageView, item.getIcon());

            holder.text(R.id.tv_title, item.getTitle().toString().substring(0, 1));
            holder.text(R.id.tv_sub_title, item.getTitle());
        }
    }
}
