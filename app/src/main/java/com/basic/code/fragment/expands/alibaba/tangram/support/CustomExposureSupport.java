
package com.basic.code.fragment.expands.alibaba.tangram.support;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.ExposureSupport;

/**
 * 自定义曝光事件
 */
public class CustomExposureSupport extends ExposureSupport {

    private static final String TAG = "CustomExposureSupport";

    public CustomExposureSupport() {
        setOptimizedMode(true);
    }

    /**
     * 布局的整体曝光
     *
     * @param card
     * @param offset
     * @param position
     */
    @Override
    public void onExposure(@NonNull Card card, int offset, int position) {
        Log.e(TAG, "onExposure: card=" + card.getClass().getSimpleName() + ", offset=" + offset + ", position=" + position);
    }

    /**
     * 布局的整体曝光
     *
     * @param targetView
     * @param cell
     * @param type
     */
    @Override
    public void defaultExposureCell(@NonNull View targetView, @NonNull BaseCell cell, int type) {
        Log.e(TAG, "defaultExposureCell: targetView=" + targetView.getClass().getSimpleName() + ", pos=" + cell.pos + ", type=" + type);
    }

    /**
     * 组件的局部区域曝光
     *
     * @param targetView
     * @param cell
     * @param type
     */
    @Override
    public void defaultTrace(@NonNull View targetView, @NonNull BaseCell cell, int type) {
        Log.e(TAG, "defaultTrace: targetView=" + targetView.getClass().getSimpleName() + ", pos=" + cell.pos + ", type=" + type);
    }
}
