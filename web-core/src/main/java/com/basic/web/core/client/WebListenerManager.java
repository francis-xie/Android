
package com.basic.web.core.client;

import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public interface WebListenerManager {

    WebListenerManager setWebChromeClient(WebView webview, WebChromeClient webChromeClient);
    WebListenerManager setWebViewClient(WebView webView, WebViewClient webViewClient);
    WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener);

}
