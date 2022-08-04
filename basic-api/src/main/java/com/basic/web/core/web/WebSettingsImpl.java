
package com.basic.web.core.web;

import android.app.Activity;
import android.webkit.DownloadListener;
import android.webkit.WebView;

import com.basic.web.action.PermissionInterceptor;
import com.basic.web.core.Web;
import com.basic.web.core.client.WebListenerManager;
import com.basic.web.utils.LogUtils;

public class WebSettingsImpl extends AbsWebSettings {
    private Web mWeb;

    @Override
    protected void bindWebSupport(Web web) {
        this.mWeb = web;
    }

    @Override
    public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {
        Class<?> clazz = null;
        Object mDefaultDownloadImpl$Extra = null;
        try {
            clazz = Class.forName("com.basic.web.download.DefaultDownloadImpl");
            mDefaultDownloadImpl$Extra =
                    clazz.getDeclaredMethod("create", Activity.class, WebView.class,
                            Class.forName("com.basic.web.download.DownloadListener"),
                            Class.forName("com.basic.web.download.DownloadingListener"),
                            PermissionInterceptor.class)
                            .invoke(mDefaultDownloadImpl$Extra, (Activity) webView.getContext()
                                    , webView, null, null, mWeb.getPermissionInterceptor());

        } catch (Throwable ignore) {
            if (LogUtils.isDebug()) {
                ignore.printStackTrace();
            }
        }
        return super.setDownloader(webView, mDefaultDownloadImpl$Extra == null ? downloadListener : (DownloadListener) mDefaultDownloadImpl$Extra);
    }
}
