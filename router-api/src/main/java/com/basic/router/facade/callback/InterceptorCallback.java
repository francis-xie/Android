
package com.basic.router.facade.callback;


import com.basic.router.facade.Postcard;

/**
 * 拦截器的回调
 */
public interface InterceptorCallback {

    /**
     * 继续执行下一个拦截器
     *
     * @param postcard 路由信息
     */
    void onContinue(Postcard postcard);

    /**
     * 拦截中断, 当该方法执行后，通道将会被销毁
     *
     * @param exception 中断的原因.
     */
    void onInterrupt(Throwable exception);
}
