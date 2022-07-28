
package com.basic.code.fragment.expands.alibaba.tangram.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.basic.face.utils.ViewUtils;
import com.basic.face.widget.imageview.ImageLoader;
import com.basic.tools.display.DensityUtils;

/**
 * 使用自定义model方式的自定义View
 *
 */
public class CustomCellView extends LinearLayout {
    private ImageView mImageView;
    private TextView mTextView;

    public CustomCellView(Context context) {
        super(context);
        init();
    }

    public CustomCellView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        int padding = DensityUtils.dip2px(getContext(), 10);
        setPadding(padding, padding, padding, padding);
        mImageView = new ImageView(getContext());
        addView(mImageView, DensityUtils.dip2px(getContext(), 110), DensityUtils.dip2px(getContext(), 72));
        mTextView = new TextView(getContext());
        mTextView.setPadding(0, padding, 0, 0);
        addView(mTextView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        ViewUtils.setViewsFont(this);
    }

    public void setImageUrl(String url) {
        ImageLoader.get().loadImage(mImageView, url);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setTextColor(@ColorInt int color) {
        mTextView.setTextColor(color);
    }
}
