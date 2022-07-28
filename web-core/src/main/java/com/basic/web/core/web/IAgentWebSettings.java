
package com.basic.web.core.web;

import android.webkit.WebView;

public interface IAgentWebSettings<T extends android.webkit.WebSettings> {

    IAgentWebSettings toSetting(WebView webView);

    T getWebSettings();

}
