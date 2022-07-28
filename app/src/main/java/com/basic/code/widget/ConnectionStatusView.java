
package com.basic.code.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.basic.face.widget.popupwindow.status.StatusView;
import com.basic.code.R;

/**
 * 连接状态
 */
public class ConnectionStatusView extends StatusView {

    public ConnectionStatusView(Context context) {
        super(context, R.layout.sv_layout_complete, R.layout.sv_layout_error, R.layout.sv_layout_loading, R.layout.sv_layout_custom);
    }

    public ConnectionStatusView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.sv_layout_complete, R.layout.sv_layout_error, R.layout.sv_layout_loading,  R.layout.sv_layout_custom);
    }

    public ConnectionStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, R.layout.sv_layout_complete, R.layout.sv_layout_error, R.layout.sv_layout_loading,  R.layout.sv_layout_custom);
    }

}
