
package com.basic.code.base.webview;

import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebView;

import com.basic.web.core.client.MiddlewareWebChromeBase;

/**
 * WebChrome（WebChromeClient主要辅助WebView处理JavaScript的对话框、网站图片、网站title、加载进度等）中间件
 * 【浏览器】

 * @since 2019/1/4 下午11:31
 */
public class MiddlewareChromeClient extends MiddlewareWebChromeBase {

    public MiddlewareChromeClient() {

    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Log.i("Info", "onJsAlert:" + url);
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Log.i("Info", "onProgressChanged:");
    }
}
