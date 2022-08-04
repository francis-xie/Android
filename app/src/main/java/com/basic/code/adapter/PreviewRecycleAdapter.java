
package com.basic.code.adapter;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.basic.face.adapter.recyclerview.BaseRecyclerAdapter;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.widget.imageview.IconImageView;
import com.basic.face.widget.imageview.preview.loader.GlideMediaLoader;
import com.basic.code.R;
import com.basic.code.fragment.components.image.preview.ImageViewInfo;

/**
 * 图片预览的适配器
 */
public class PreviewRecycleAdapter extends BaseRecyclerAdapter<ImageViewInfo> {

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.adapter_item_image_preview;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, ImageViewInfo item) {
        if (item != null) {
            IconImageView imageView = holder.findViewById(R.id.iv);

            imageView.setIsShowIcon(item.getVideoUrl() != null);

            Glide.with(imageView.getContext())
                    .load(item.getUrl())
                    .apply(GlideMediaLoader.getRequestOptions())
                    .into(imageView);

            imageView.setTag(R.id.iv, item.getUrl());
        }
    }
}
