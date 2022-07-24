
package com.basic.router.facade.callback;

import com.basic.router.facade.Postcard;

/**
 * 执行navigation（导航）后的回调
 *

 * @since 2018/5/16 下午11:45
 */
public interface NavigationCallback {

    /**
     * 发现导航目标的回调
     *
     * @param postcard 路由信息
     */
    void onFound(Postcard postcard);

    /**
     * 路由丢失（找不到）的回调
     *
     * @param postcard 路由信息
     */
    void onLost(Postcard postcard);

    /**
     * 导航到达的回调
     *
     * @param postcard 路由信息
     */
    void onArrival(Postcard postcard);

    /**
     * 被拦截的回调
     *
     * @param postcard 路由信息
     */
    void onInterrupt(Postcard postcard);
}
