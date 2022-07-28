
package com.basic.code.fragment.components.statelayout.status.adapter;

import android.view.View;

import com.basic.face.widget.statelayout.StatusLoader;

/**
 * 默认状态适配器
 */
public class DefaultStatusAdapter implements StatusLoader.Adapter {

    @Override
    public View getView(StatusLoader.Holder holder, View convertView, int status) {
        return new DefaultStatusView(holder.getContext(), status, holder.getRetryListener());
    }
}
