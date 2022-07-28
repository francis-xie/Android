
package com.basic.web.widget.indicator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;


/**
 * 基础进度条
 *
 
 * @since 2019/1/4 上午11:00
 */
public abstract class BaseIndicatorView extends FrameLayout implements BaseIndicatorSpec,LayoutParamsOffer {
    public BaseIndicatorView(Context context) {
        super(context);
    }

    public BaseIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void reset() {

    }

    @Override
    public void setProgress(int newProgress) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

}
