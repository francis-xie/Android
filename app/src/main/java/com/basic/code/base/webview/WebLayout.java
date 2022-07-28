
package com.basic.code.base.webview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.basic.refresh.layout.RefreshLayouts;
import com.basic.web.widget.IWebLayout;
import com.basic.code.R;

/**
 * 定义支持下来回弹的WebView
 */
public class WebLayout implements IWebLayout {

    private final RefreshLayouts mRefreshLayouts;
    private WebView mWebView;

    public WebLayout(Activity activity) {
        mRefreshLayouts = (RefreshLayouts) LayoutInflater.from(activity).inflate(R.layout.fragment_pulldown_web, null);
        mWebView = mRefreshLayouts.findViewById(R.id.webView);
    }

    @NonNull
    @Override
    public ViewGroup getLayout() {
        return mRefreshLayouts;
    }

    @Nullable
    @Override
    public WebView getWebView() {
        return mWebView;
    }

}
