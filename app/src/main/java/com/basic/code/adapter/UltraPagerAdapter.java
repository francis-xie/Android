
package com.basic.code.adapter;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.utils.ResUtils;
import com.basic.face.widget.banner.widget.banner.BannerItem;
import com.basic.face.widget.image.ImageLoader;
import com.basic.face.widget.image.strategy.DiskCacheStrategyEnum;
import com.basic.face.widget.image.strategy.LoadOption;
import com.basic.code.R;
import com.basic.tools.display.ScreenUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * UltraViewPager适配器
 */
public class UltraPagerAdapter extends PagerAdapter {

    private final List<BannerItem> mData = new ArrayList<>();

    /**
     * 默认加载图片
     */
    private Drawable mPlaceHolder;
    /**
     * 是否允许进行缓存
     */
    private boolean mEnableCache;
    /**
     * 加载图片的高／宽比率
     */
    private double mScale;

    private RecyclerViewHolder.OnItemClickListener<BannerItem> mOnItemClickListener;

    public UltraPagerAdapter(List<BannerItem> list, float scale) {
        if (list != null && !list.isEmpty()) {
            mData.addAll(list);
        }
        mPlaceHolder = new ColorDrawable(ResUtils.getColor(R.color.default_image_banner_placeholder_color));
        mEnableCache = true;
        mScale = scale;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    private boolean checkPosition(int position) {
        return position >= 0 && position <= mData.size() - 1;
    }

    public BannerItem getItem(int position) {
        return checkPosition(position) ? mData.get(position) : null;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View rootView = View.inflate(container.getContext(), R.layout.face_adapter_simple_image, null);
        ImageView iv = rootView.findViewById(R.id.iv);

        //解决Glide资源释放的问题，详细见http://blog.csdn.net/shangmingchao/article/details/51125554
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(iv);
        ImageView target = imageViewWeakReference.get();

        BannerItem item = getItem(position);
        if (item != null && target != null) {
            loadingImageView(target, item);
        }
        rootView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, item, position);
            }
        });
        container.addView(rootView);
        return rootView;
    }

    public UltraPagerAdapter setOnItemClickListener(RecyclerViewHolder.OnItemClickListener<BannerItem> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    /**
     * 默认加载图片的方法，可以重写
     *
     * @param iv
     * @param item
     */
    protected void loadingImageView(ImageView iv, BannerItem item) {
        String imgUrl = item.imgUrl;
        if (!TextUtils.isEmpty(imgUrl)) {
            if (mScale > 0) {
                int itemWidth = ScreenUtils.getScreenWidth();
                int itemHeight = (int) (itemWidth * mScale);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));

                LoadOption option = LoadOption.of(mPlaceHolder)
                        .setSize(itemWidth, itemHeight)
                        .setCacheStrategy(mEnableCache ? DiskCacheStrategyEnum.RESOURCE : DiskCacheStrategyEnum.NONE);
                ImageLoader.get().loadImage(iv, imgUrl, option);
            } else {
                ImageLoader.get().loadImage(iv, imgUrl, mPlaceHolder, mEnableCache ? DiskCacheStrategyEnum.RESOURCE : DiskCacheStrategyEnum.NONE);
            }
        } else {
            iv.setImageDrawable(mPlaceHolder);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
