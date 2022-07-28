
package com.basic.code.fragment.utils;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.utils.ColorUtils;
import com.basic.face.utils.DensityUtils;
import com.basic.face.utils.DrawableUtils;
import com.basic.face.utils.ViewUtils;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * {@link DrawableUtils} 的使用示例。
 */
@Page(name = "DrawableUtils", extra = R.drawable.ic_util_drawable)
public class DrawableUtilsFragment extends BaseFragment {

    @BindView(R.id.solidImage)
    ImageView mSolidImageView;
    @BindView(R.id.circleImage)
    AppCompatImageView circleImage;
    @BindView(R.id.circleGradient)
    ImageView mCircleGradientView;
    @BindView(R.id.tintColor)
    ImageView mTintColorImageView;
    @BindView(R.id.tintColorOrigin)
    ImageView mTintColorOriginImageView;
    @BindView(R.id.separator)
    View mSeparatorView;
    @BindView(R.id.contentWrap)
    NestedScrollView contentWrap;
    @BindView(R.id.btn_action)
    Button btnAction;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_util_drawable;
    }

    @Override
    protected void initViews() {
        initContent();
    }

    private void initContent() {
        int commonShapeSize = getResources().getDimensionPixelSize(R.dimen.drawable_utils_common_shape_size);
        int commonShapeRadius = DensityUtils.dp2px(getContext(), 10);

        // 创建一张指定大小的纯色图片，支持圆角
        BitmapDrawable solidImageBitmapDrawable = DrawableUtils.createDrawableWithSize(getResources(), commonShapeSize, commonShapeSize, commonShapeRadius, ContextCompat.getColor(getContext(), R.color.app_color_theme_3));
        mSolidImageView.setImageDrawable(solidImageBitmapDrawable);

        // 创建一张指定大小的圆形图片，并带文字
        BitmapDrawable circleImageBitmapDrawable = DrawableUtils.createCircleDrawableWithText(getResources(), commonShapeSize, ContextCompat.getColor(getContext(), R.color.app_color_theme_2), "薛", DensityUtils.sp2px(getContext(), 20), Color.WHITE);
        circleImage.setImageDrawable(circleImageBitmapDrawable);

        // 创建一张圆形渐变图片，支持圆角
        GradientDrawable gradientCircleGradientDrawable = DrawableUtils.createCircleGradientDrawable(ContextCompat.getColor(getContext(), R.color.app_color_theme_4),
                ContextCompat.getColor(getContext(), R.color.face_config_color_transparent), commonShapeRadius, 0.5f, 0.5f);
        mCircleGradientView.setImageDrawable(gradientCircleGradientDrawable);

        // 设置 Drawable 的颜色
        // 创建两张表现相同的图片
        BitmapDrawable tintColorBitmapDrawable = DrawableUtils.createDrawableWithSize(getResources(), commonShapeSize, commonShapeSize, commonShapeRadius, ContextCompat.getColor(getContext(), R.color.app_color_theme_1));
        BitmapDrawable tintColorOriginBitmapDrawable = DrawableUtils.createDrawableWithSize(getResources(), commonShapeSize, commonShapeSize, commonShapeRadius, ContextCompat.getColor(getContext(), R.color.app_color_theme_1));
        // 其中一张重新设置颜色
        DrawableUtils.setDrawableTintColor(tintColorBitmapDrawable, ContextCompat.getColor(getContext(), R.color.app_color_theme_7));
        mTintColorImageView.setImageDrawable(tintColorBitmapDrawable);
        mTintColorOriginImageView.setImageDrawable(tintColorOriginBitmapDrawable);

        int color = ColorUtils.getRandomColor();
        btnAction.setBackground(DrawableUtils.getDrawable(16, color));
        btnAction.setTextColor(ColorUtils.isColorDark(color) ? Color.WHITE : Color.BLACK);

        // 创建带上分隔线或下分隔线的 Drawable
        LayerDrawable separatorLayerDrawable = DrawableUtils.createItemSeparatorBg(ContextCompat.getColor(getContext(), R.color.app_color_theme_7),
                ContextCompat.getColor(getContext(), R.color.app_color_theme_6), DensityUtils.dp2px(getContext(), 2), true);
        ViewUtils.setBackgroundKeepingPadding(mSeparatorView, separatorLayerDrawable);

    }


    @SingleClick
    @OnClick({R.id.createFromView, R.id.createFromView1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.createFromView:
                Utils.showCaptureBitmap(getRootView());
                break;
            case R.id.createFromView1:
                Utils.showCaptureBitmap(contentWrap);
                break;
            default:
                break;
        }
    }


}
