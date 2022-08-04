
package com.basic.web.js;

import android.os.Build;
import android.webkit.JavascriptInterface;

import com.basic.web.core.Web;
import com.basic.web.core.web.WebConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class JsBaseInterfaceHolder implements JsInterfaceHolder {

    private Web.SecurityType mSecurityType;

    protected JsBaseInterfaceHolder(Web.SecurityType securityType) {
        this.mSecurityType = securityType;
    }

    @Override
    public boolean checkObject(Object v) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1){
            return true;
        }
        if (WebConfig.WEBVIEW_TYPE == WebConfig.WEBVIEW_WEB_SAFE_TYPE){
            return true;
        }
        boolean tag = false;
        Class clazz = v.getClass();

        Method[] mMethods = clazz.getMethods();

        for (Method mMethod : mMethods) {

            Annotation[] mAnnotations = mMethod.getAnnotations();

            for (Annotation mAnnotation : mAnnotations) {

                if (mAnnotation instanceof JavascriptInterface) {
                    tag = true;
                    break;
                }

            }
            if (tag){
                break;
            }
        }

        return tag;
    }

    protected boolean checkSecurity() {
        return mSecurityType != Web.SecurityType.STRICT_CHECK
                ? true : WebConfig.WEBVIEW_TYPE == WebConfig.WEBVIEW_WEB_SAFE_TYPE
                ? true : Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1;
    }


}
