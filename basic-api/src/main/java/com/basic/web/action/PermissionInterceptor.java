
package com.basic.web.action;

public interface PermissionInterceptor {

    boolean intercept(String url, String[] permissions, String action);

}
