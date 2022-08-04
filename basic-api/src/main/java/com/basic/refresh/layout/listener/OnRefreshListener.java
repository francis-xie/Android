package com.basic.refresh.layout.listener;

import androidx.annotation.NonNull;
import com.basic.refresh.layout.api.RefreshLayout;

/**
 * 刷新监听器
 * Created by SCWANG on 2017/5/26.
 */

public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}
