
package com.basic.web.security;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebView;

import androidx.collection.ArrayMap;
import com.basic.web.core.AgentWeb;
import com.basic.web.core.web.AgentWebConfig;
import com.basic.web.utils.LogUtils;


/**
 
 */
public class WebSecurityLogicImpl implements WebSecurityCheckLogic {
    private String TAG=this.getClass().getSimpleName();
    public static WebSecurityLogicImpl getInstance() {
        return new WebSecurityLogicImpl();
    }

    public WebSecurityLogicImpl(){}

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void dealHoneyComb(WebView view) {
        if (Build.VERSION_CODES.HONEYCOMB > Build.VERSION.SDK_INT || Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1){
            return;
        }
        view.removeJavascriptInterface("searchBoxJavaBridge_");
        view.removeJavascriptInterface("accessibility");
        view.removeJavascriptInterface("accessibilityTraversal");
    }

    @Override
    public void dealJsInterface(ArrayMap<String, Object> objects, AgentWeb.SecurityType securityType) {

        if (securityType== AgentWeb.SecurityType.STRICT_CHECK
                &&AgentWebConfig.WEBVIEW_TYPE !=AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE
                &&Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            LogUtils.e(TAG,"Give up all inject objects");
            objects.clear();
            objects = null;
            System.gc();
        }

    }
}
