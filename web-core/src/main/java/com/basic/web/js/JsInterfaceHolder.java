
package com.basic.web.js;

import java.util.Map;

/**
 
 * @date 2017/5/13
 * @since 1.0.0
 */
public interface JsInterfaceHolder {

    JsInterfaceHolder addJavaObjects(Map<String, Object> maps);

    JsInterfaceHolder addJavaObject(String k, Object v);

    boolean checkObject(Object v);

}
