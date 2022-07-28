
package com.basic.web.core.web.lifecycle;

import android.os.Build;
import android.webkit.WebView;

import com.basic.web.utils.WebUtils;

/**
 
 * @date 2017/6/3
 * @since 2.0.0
 */
public class DefaultWebLifeCycleImpl implements WebLifeCycle {
    private WebView mWebView;

    public DefaultWebLifeCycleImpl(WebView webView) {
        this.mWebView = webView;
    }

    @Override
    public void onResume() {
        if (this.mWebView != null) {

            if (Build.VERSION.SDK_INT >= 11){
                this.mWebView.onResume();
            }
            this.mWebView.resumeTimers();
        }


    }

    @Override
    public void onPause() {

        if (this.mWebView != null) {

            if (Build.VERSION.SDK_INT >= 11){
                this.mWebView.onPause();
            }
            this.mWebView.pauseTimers();
        }
    }

    @Override
    public void onDestroy() {

        if(this.mWebView!=null){
            this.mWebView.resumeTimers();
        }
        WebUtils.clearWebView(this.mWebView);

    }
}
