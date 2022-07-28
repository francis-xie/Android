
package com.basic.web.widget.indicator;

import android.webkit.WebView;
import android.widget.FrameLayout;

/**
 
 * @since 1.0.0
 */
public interface WebCreator extends IWebIndicator {
    WebCreator create();

    WebView getWebView();

    FrameLayout getWebParentLayout();
}
