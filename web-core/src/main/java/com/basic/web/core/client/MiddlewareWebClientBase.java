
package com.basic.web.core.client;

import android.webkit.WebViewClient;

/**
 
 * @date 2017/12/15
 * @since 3.0.0
 */
public class MiddlewareWebClientBase extends WebViewClientDelegate {
    private MiddlewareWebClientBase mMiddleWrareWebClientBase;
    private static String TAG = MiddlewareWebClientBase.class.getSimpleName();

    MiddlewareWebClientBase(MiddlewareWebClientBase client) {
        super(client);
        this.mMiddleWrareWebClientBase = client;
    }

    protected MiddlewareWebClientBase(WebViewClient client) {
        super(client);
    }

    protected MiddlewareWebClientBase() {
        super(null);
    }

    public final MiddlewareWebClientBase next() {
        return this.mMiddleWrareWebClientBase;
    }


    @Override
    public final void setDelegate(WebViewClient delegate) {
        super.setDelegate(delegate);

    }

    public final MiddlewareWebClientBase enq(MiddlewareWebClientBase middleWrareWebClientBase) {
        setDelegate(middleWrareWebClientBase);
        this.mMiddleWrareWebClientBase = middleWrareWebClientBase;
        return middleWrareWebClientBase;
    }


}
