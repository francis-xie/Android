
package com.basic.web.widget;

import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 填充Web的布局容器实现接口
 *

 * @since 2019/1/4 上午10:44
 */
public interface IWebLayout<T extends WebView, V extends ViewGroup> {

    /**
     * @return WebView 的父控件
     */
    @NonNull
    V getLayout();

    /**
     * @return 返回 WebView  或 WebView 的子View ，返回null AgentWeb 内部会创建适当 WebView
     */
    @Nullable
    T getWebView();
}
