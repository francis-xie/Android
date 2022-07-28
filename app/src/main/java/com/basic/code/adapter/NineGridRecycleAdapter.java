
package com.basic.code.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.basic.aop.annotation.SingleClick;
import com.basic.face.adapter.recyclerview.XRecyclerAdapter;
import com.basic.face.widget.imageview.nine.ItemImageClickListener;
import com.basic.face.widget.imageview.nine.NineGridImageView;
import com.basic.face.widget.imageview.nine.NineGridImageViewAdapter;
import com.basic.face.widget.imageview.preview.PreviewBuilder;
import com.basic.face.widget.imageview.preview.loader.GlideMediaLoader;
import com.basic.code.R;
import com.basic.code.fragment.components.imageview.preview.ImageViewInfo;
import com.basic.code.fragment.components.imageview.preview.NineGridInfo;
import com.basic.code.utils.SettingSPUtils;

import java.util.List;

/**
 * 九宫格适配器
 */
public class NineGridRecycleAdapter extends XRecyclerAdapter<NineGridInfo, NineGridRecycleAdapter.NineGridHolder> {

    @NonNull
    @Override
    protected NineGridHolder getViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == NineGridImageView.STYLE_GRID) {
            return new NineGridHolder(inflateView(parent, R.layout.adapter_item_nine_grid_grid_style));
        } else {
            return new NineGridHolder(inflateView(parent, R.layout.adapter_item_nine_grid_fill_style));
        }
    }

    @Override
    protected void bindData(@NonNull NineGridHolder holder, int position, NineGridInfo item) {
        holder.bind(item);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getShowType();
    }


    public static class NineGridHolder extends RecyclerView.ViewHolder {
        private NineGridImageView<ImageViewInfo> mNglContent;
        private TextView mTvContent;

        public NineGridHolder(View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tv_content);
            mNglContent = itemView.findViewById(R.id.ngl_images);
            /**
             * 图片加载
             *
             * @param context
             * @param imageView
             * @param imageViewInfo 图片信息
             */
            NineGridImageViewAdapter<ImageViewInfo> mAdapter = new NineGridImageViewAdapter<ImageViewInfo>() {
                /**
                 * 图片加载
                 *
                 * @param context
                 * @param imageView
                 * @param imageViewInfo 图片信息
                 */
                @Override
                protected void onDisplayImage(Context context, ImageView imageView, ImageViewInfo imageViewInfo) {
                    Glide.with(imageView.getContext())
                            .load(imageViewInfo.getUrl())
                            .apply(GlideMediaLoader.getRequestOptions())
                            .into(imageView);
                }

                @Override
                protected ImageView generateImageView(Context context) {
                    return super.generateImageView(context);
                }
            };
            mNglContent.setAdapter(mAdapter);
            mNglContent.setItemImageClickListener(new ItemImageClickListener<ImageViewInfo>() {
                @SingleClick
                @Override
                public void onItemImageClick(ImageView imageView, int index, List<ImageViewInfo> list) {
                    computeBoundsBackward(list);//组成数据
                    PreviewBuilder.from((Activity) imageView.getContext())
                            .setImgs(list)
                            .setCurrentIndex(index)
                            .setProgressColor(SettingSPUtils.getInstance().isUseCustomTheme() ? R.color.custom_color_main_theme : R.color.face_config_color_main_theme)
                            .setType(PreviewBuilder.IndicatorType.Dot)
                            .start();//启动
                }
            });
        }

        /**
         * 查找信息
         * @param list 图片集合
         */
        private void computeBoundsBackward(List<ImageViewInfo> list) {
            for (int i = 0;i < mNglContent.getChildCount(); i++) {
                View itemView = mNglContent.getChildAt(i);
                Rect bounds = new Rect();
                if (itemView != null) {
                    ImageView thumbView = (ImageView) itemView;
                    thumbView.getGlobalVisibleRect(bounds);
                }
                list.get(i).setBounds(bounds);
                list.get(i).setUrl(list.get(i).getUrl());
            }

        }

        public void bind(NineGridInfo gridInfo) {
            mNglContent.setImagesData(gridInfo.getImgUrlList(), gridInfo.getSpanType());
            mTvContent.setText(gridInfo.getContent());
        }
    }
}
