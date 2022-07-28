
package com.basic.web.js;

import android.webkit.ValueCallback;

/**
 
 * @date 2017/5/14
 * @since 1.0.0
 */
public interface JsAccessEntrace extends QuickCallJs {


    void callJs(String js, ValueCallback<String> callback);

    void callJs(String js);


}
