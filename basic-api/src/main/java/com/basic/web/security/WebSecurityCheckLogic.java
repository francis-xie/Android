
package com.basic.web.security;

import android.webkit.WebView;

import androidx.collection.ArrayMap;
import com.basic.web.core.Web;


/**
 
 */
public interface WebSecurityCheckLogic {
    void dealHoneyComb(WebView view);

    void dealJsInterface(ArrayMap<String, Object> objects, Web.SecurityType securityType);

}
