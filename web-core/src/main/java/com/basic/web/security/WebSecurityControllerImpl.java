
package com.basic.web.security;

import android.os.Build;
import android.webkit.WebView;

import androidx.collection.ArrayMap;
import com.basic.web.core.AgentWeb;


/**
 
 */
public class WebSecurityControllerImpl implements WebSecurityController<WebSecurityCheckLogic> {

    private WebView mWebView;
    private ArrayMap<String, Object> mMap;
    private AgentWeb.SecurityType mSecurityType;

    public WebSecurityControllerImpl(WebView view, ArrayMap<String, Object> map, AgentWeb.SecurityType securityType) {
        this.mWebView = view;
        this.mMap = map;
        this.mSecurityType = securityType;
    }

    @Override
    public void check(WebSecurityCheckLogic webSecurityCheckLogic) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            webSecurityCheckLogic.dealHoneyComb(mWebView);
        }

        if (mMap != null && mSecurityType == AgentWeb.SecurityType.STRICT_CHECK && !mMap.isEmpty()) {
            webSecurityCheckLogic.dealJsInterface(mMap, mSecurityType);
        }
    }
}
