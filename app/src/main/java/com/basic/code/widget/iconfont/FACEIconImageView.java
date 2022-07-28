
package com.basic.code.widget.iconfont;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.basic.face.utils.ThemeUtils;

import static androidx.annotation.Dimension.DP;

/**
 * 好好玩字体库图标
 */
public class FACEIconImageView extends AppCompatImageView {

    private String mIconText;

    public FACEIconImageView(Context context) {
        super(context);
    }

    public FACEIconImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FACEIconImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FACEIconImageView setIconText(@NonNull String text) {
        FACEIconFont.Icon icon = FACEIconFont.Icon.get(text);
        if (icon != null) {
            mIconText = text;
            IconicsDrawable drawable = new IconicsDrawable(getContext())
                    .icon(icon)
                    .color(IconicsColor.colorInt(ThemeUtils.getMainThemeColor(getContext())));
            setImageDrawable(drawable);
        }
        return this;
    }

    public FACEIconImageView setIconText(@NonNull String text, @Dimension(unit = DP) int dpSize) {
        FACEIconFont.Icon icon = FACEIconFont.Icon.get(text);
        if (icon != null) {
            mIconText = text;
            IconicsDrawable drawable = new IconicsDrawable(getContext())
                    .icon(icon)
                    .color(IconicsColor.colorInt(ThemeUtils.getMainThemeColor(getContext())))
                    .size(IconicsSize.dp(dpSize));
            setImageDrawable(drawable);
        }
        return this;
    }

    public FACEIconImageView setIconText(@NonNull String text, @Dimension(unit = DP) int dpSize, @ColorInt int color) {
        FACEIconFont.Icon icon = FACEIconFont.Icon.get(text);
        if (icon != null) {
            mIconText = text;
            IconicsDrawable drawable = new IconicsDrawable(getContext())
                    .icon(icon)
                    .color(IconicsColor.colorInt(color))
                    .size(IconicsSize.dp(dpSize));
            setImageDrawable(drawable);
        }
        return this;
    }

    public String getIconText() {
        return mIconText;
    }
}
