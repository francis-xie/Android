
package com.basic.code.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.basic.face.adapter.recyclerview.BaseRecyclerAdapter;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.widget.banner.recycler.BannerLayout;
import com.basic.face.widget.imageview.ImageLoader;
import com.basic.face.widget.imageview.strategy.DiskCacheStrategyEnum;
import com.basic.code.R;

import java.util.Arrays;
import java.util.List;

public class RecyclerViewBannerAdapter extends BaseRecyclerAdapter<String> {

    /**
     * 默认加载图片
     */
    private ColorDrawable mColorDrawable;

    /**
     * 是否允许进行缓存
     */
    private boolean mEnableCache = true;

    private BannerLayout.OnBannerItemClickListener mOnBannerItemClickListener;


    public RecyclerViewBannerAdapter() {
        super();
        mColorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    public RecyclerViewBannerAdapter(List<String> list) {
        super(list);
        mColorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    public RecyclerViewBannerAdapter(String[] list) {
        super(Arrays.asList(list));
        mColorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_recycler_view_banner_image_item;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     * @param imgUrl
     */
    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, final int position, String imgUrl) {
        ImageView imageView = holder.findViewById(R.id.iv_item);

        if (!TextUtils.isEmpty(imgUrl)) {
            ImageLoader.get().loadImage(imageView, imgUrl, mColorDrawable,
                    mEnableCache ? DiskCacheStrategyEnum.RESOURCE : DiskCacheStrategyEnum.NONE);
        } else {
            imageView.setImageDrawable(mColorDrawable);
        }

        imageView.setOnClickListener(v -> {
            if (mOnBannerItemClickListener != null) {
                mOnBannerItemClickListener.onItemClick(position);
            }
        });
    }

    /**
     * 设置是否允许缓存
     *
     * @param enableCache
     * @return
     */
    public RecyclerViewBannerAdapter enableCache(boolean enableCache) {
        mEnableCache = enableCache;
        return this;
    }

    /**
     * 获取是否允许缓存
     *
     * @return
     */
    public boolean getEnableCache() {
        return mEnableCache;
    }

    public ColorDrawable getColorDrawable() {
        return mColorDrawable;
    }

    public RecyclerViewBannerAdapter setColorDrawable(ColorDrawable colorDrawable) {
        mColorDrawable = colorDrawable;
        return this;
    }

    public RecyclerViewBannerAdapter setOnBannerItemClickListener(BannerLayout.OnBannerItemClickListener onBannerItemClickListener) {
        mOnBannerItemClickListener = onBannerItemClickListener;
        return this;
    }
}
