
package com.basic.web.core.web;

import android.webkit.WebView;

public interface IWebSettings<T extends android.webkit.WebSettings> {

    IWebSettings toSetting(WebView webView);

    T getWebSettings();

}
