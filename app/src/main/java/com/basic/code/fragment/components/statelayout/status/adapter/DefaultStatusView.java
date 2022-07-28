
package com.basic.code.fragment.components.statelayout.status.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.basic.face.utils.ThemeUtils;
import com.basic.code.R;

import static com.basic.face.widget.statelayout.StatusLoader.STATUS_CUSTOM;
import static com.basic.face.widget.statelayout.StatusLoader.STATUS_EMPTY_DATA;
import static com.basic.face.widget.statelayout.StatusLoader.STATUS_LOADING;
import static com.basic.face.widget.statelayout.StatusLoader.STATUS_LOAD_FAILED;
import static com.basic.face.widget.statelayout.StatusLoader.STATUS_LOAD_SUCCESS;

/**
 * 默认状态布局
 */
@SuppressLint("ViewConstructor")
public class DefaultStatusView extends LinearLayout implements View.OnClickListener {

    private final OnClickListener mRetryListener;

    public DefaultStatusView(Context context, int status, OnClickListener retryListener) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        int layoutId = getLayoutIdByStatus(status);
        if (layoutId != 0) {
            LayoutInflater.from(context).inflate(layoutId, this, true);
        }
        mRetryListener = retryListener;
        setStatus(status);
        setBackgroundColor(ThemeUtils.resolveColor(getContext(), R.attr.face_config_color_background));
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    private void setStatus(int status) {
        switch (status) {
            case STATUS_LOAD_SUCCESS:
                setVisibility(View.GONE);
                break;
            case STATUS_LOADING:
                setVisibility(View.VISIBLE);
                break;
            case STATUS_LOAD_FAILED:
            case STATUS_EMPTY_DATA:
            case STATUS_CUSTOM:
                setVisibility(View.VISIBLE);
                setOnClickListener(this);
                break;
            default:
                break;
        }
    }

    private int getLayoutIdByStatus(int status) {
        int layoutId = 0;
        switch (status) {
            case STATUS_LOADING:
                layoutId = R.layout.msv_layout_loading_view;
                break;
            case STATUS_LOAD_FAILED:
                layoutId = R.layout.msv_layout_error_view;
                break;
            case STATUS_EMPTY_DATA:
                layoutId = R.layout.msv_layout_empty_view;
                break;
            case STATUS_CUSTOM:
                layoutId = R.layout.msv_layout_no_network_view;
                break;
            default:
                break;
        }
        return layoutId;
    }


    @Override
    public void onClick(View v) {
        if (mRetryListener != null) {
            mRetryListener.onClick(v);
        }
    }
}
