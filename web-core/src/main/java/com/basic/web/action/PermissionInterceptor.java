
package com.basic.web.action;

/**
 
 * @since 3.0.0
 */
public interface PermissionInterceptor {

    boolean intercept(String url, String[] permissions, String action);

}
