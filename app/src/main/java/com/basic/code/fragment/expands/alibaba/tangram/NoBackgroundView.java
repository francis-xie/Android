
package com.basic.code.fragment.expands.alibaba.tangram;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;
import com.tmall.wireless.tangram.support.ExposureSupport;
import com.basic.face.utils.ViewUtils;
import com.basic.tools.display.DensityUtils;

import java.util.Locale;

/**
 * 不设置背景的自定义View
 *
 */
public class NoBackgroundView extends LinearLayout implements ITangramViewLifeCycle {

    private TextView mTextView;

    public NoBackgroundView(Context context) {
        super(context);
        init();
    }

    public NoBackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        int padding = DensityUtils.dip2px(getContext(), 10);
        setPadding(padding, padding, padding, padding);
        mTextView = new TextView(getContext());
        mTextView.setTextSize(12);
        addView(mTextView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        ViewUtils.setViewsFont(this);
    }

    @Override
    public void cellInited(BaseCell cell) {
        setOnClickListener(cell);
        if (cell.serviceManager != null) {
            ExposureSupport exposureSupport = cell.serviceManager.getService(ExposureSupport.class);
            if (exposureSupport != null) {
                exposureSupport.onTrace(this, cell, cell.type);
            }
        }
    }

    @Override
    public void postBindView(BaseCell cell) {
        mTextView.setText(String.format(Locale.CHINA, "%s%d: %s", getClass().getSimpleName(), cell.pos, cell.optParam("text")));
    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }
}
