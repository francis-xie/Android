
package com.basic.code.fragment.expands.alibaba.tangram.viewholder;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.ExposureSupport;

import java.util.Locale;

/**
 * 自定义ViewHolder的model
 *

 * @since 2020/4/10 12:39 AM
 */
public class CustomHolderCell extends BaseCell<TextView> {

    @Override
    public void bindView(@NonNull TextView view) {
        if (pos % 2 == 0) {
            view.setBackgroundColor(0xff000fff);
        } else {
            view.setBackgroundColor(0xfffff000);
        }
        view.setText(String.format(Locale.CHINA, "%s%d: %s", getClass().getSimpleName(), pos, optParam("text")));
        view.setOnClickListener(this);
        if (serviceManager != null) {
            ExposureSupport exposureSupport = serviceManager.getService(ExposureSupport.class);
            if (exposureSupport != null) {
                exposureSupport.onTrace(view, this, type);
            }
        }
    }
}
