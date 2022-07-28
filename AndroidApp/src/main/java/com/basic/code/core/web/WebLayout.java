package com.basic.code.core.web;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.basic.web.widget.IWebLayout;
import com.basic.refresh.layout.RefreshLayouts;
import com.basic.code.R;

/**
 * 定义支持下来回弹的WebView
 */
public class WebLayout implements IWebLayout {

    private final RefreshLayouts mSmartRefreshLayout;
    private WebView mWebView;

    public WebLayout(Activity activity) {
        mSmartRefreshLayout = (RefreshLayouts) LayoutInflater.from(activity).inflate(R.layout.fragment_pulldown_web, null);
        mWebView = mSmartRefreshLayout.findViewById(R.id.webView);
    }

    @NonNull
    @Override
    public ViewGroup getLayout() {
        return mSmartRefreshLayout;
    }

    @Nullable
    @Override
    public WebView getWebView() {
        return mWebView;
    }


}
