
package com.basic.code.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.basic.face.utils.DensityUtils;
import com.basic.face.widget.banner.widget.banner.base.BaseImageBanner;
import com.basic.code.R;

/**
 * 带圆角的图片轮播
 *

 * @since 2019/1/14 下午10:07
 */
public class RadiusImageBanner extends BaseImageBanner<RadiusImageBanner> {

    public RadiusImageBanner(Context context) {
        super(context);
    }

    public RadiusImageBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadiusImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @return 轮播布局的ID
     */
    @Override
    protected int getItemLayoutId() {
        return R.layout.face_adapter_radius_image;
    }

    /**
     * @return 图片控件的ID
     */
    @Override
    protected int getImageViewId() {
        return R.id.riv;
    }

    @Override
    public int getItemWidth() {
        //需要距离边一点距离
        return super.getItemWidth() - DensityUtils.dp2px(getContext(), 32);
    }
}
