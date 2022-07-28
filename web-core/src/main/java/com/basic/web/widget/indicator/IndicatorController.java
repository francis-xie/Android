
package com.basic.web.widget.indicator;

import android.webkit.WebView;

/**
 * 进度条控制器
 */
public interface IndicatorController {

    void progress(WebView v, int newProgress);

    BaseIndicatorSpec offerIndicator();

    void showIndicator();

    void setProgress(int newProgress);

    void finish();
}
