
package com.basic.code.fragment.components.statelayout.status.adapter;

import android.view.View;

import com.basic.face.utils.ThemeUtils;
import com.basic.face.widget.statelayout.StatefulLayout;
import com.basic.face.widget.statelayout.StatusLoader;
import com.basic.code.R;

import static com.basic.face.widget.statelayout.StatusLoader.STATUS_CUSTOM;
import static com.basic.face.widget.statelayout.StatusLoader.STATUS_EMPTY_DATA;
import static com.basic.face.widget.statelayout.StatusLoader.STATUS_LOADING;
import static com.basic.face.widget.statelayout.StatusLoader.STATUS_LOAD_FAILED;
import static com.basic.face.widget.statelayout.StatusLoader.STATUS_LOAD_SUCCESS;

/**
 * 复用组件
 */
public class SingleViewAdapter implements StatusLoader.Adapter {

    @Override
    public View getView(StatusLoader.Holder holder, View convertView, int status) {
        StatefulLayout statefulLayout = null;
        // 进行复用
        if (convertView instanceof StatefulLayout) {
            statefulLayout = (StatefulLayout) convertView;
        }
        if (statefulLayout == null) {
            statefulLayout = new StatefulLayout(holder.getContext());
            statefulLayout.setAnimationEnabled(false);
            statefulLayout.attachTemplate();
            statefulLayout.setBackgroundColor(ThemeUtils.resolveColor(holder.getContext(), R.attr.face_config_color_background));
        }

        statefulLayout.setVisibility(status == STATUS_LOAD_SUCCESS ? View.GONE : View.VISIBLE);
        switch (status) {
            case STATUS_LOADING:
                statefulLayout.showLoading();
                break;
            case STATUS_LOAD_FAILED:
                statefulLayout.showError(holder.getRetryListener());
                break;
            case STATUS_EMPTY_DATA:
                statefulLayout.showEmpty();
                break;
            case STATUS_CUSTOM:
                statefulLayout.showOffline(holder.getRetryListener());
                break;
            default:
                break;
        }
        return statefulLayout;
    }


}
