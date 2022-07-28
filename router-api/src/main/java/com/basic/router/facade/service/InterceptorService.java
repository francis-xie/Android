
package com.basic.router.facade.service;

import com.basic.router.facade.Postcard;
import com.basic.router.facade.callback.InterceptorCallback;
import com.basic.router.facade.template.IProvider;

/**
 * 拦截服务
 */
public interface InterceptorService extends IProvider {

    /**
     * 执行拦截操作
     */
    void doInterceptions(Postcard postcard, InterceptorCallback callback);
}
