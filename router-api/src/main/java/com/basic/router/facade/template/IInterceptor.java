
package com.basic.router.facade.template;

import com.basic.router.facade.Postcard;
import com.basic.router.facade.callback.InterceptorCallback;

/**
 * 路由拦截器，在路由导航时可注入一些自定义逻辑
 *

 * @since 2018/5/17 上午12:13
 */
public interface IInterceptor extends IProvider {

    /**
     * 拦截器的执行操作
     *
     * @param postcard 路由信息
     * @param callback 拦截回调
     */
    void process(Postcard postcard, InterceptorCallback callback);
}
