
package com.basic.router.facade.callback;


import com.basic.router.facade.Postcard;

/**
 * 简单的路由导航回调
 *

 * @since 2018/5/16 下午11:50
 */
public abstract class NavCallback implements NavigationCallback {
    @Override
    public void onFound(Postcard postcard) {
        // Do nothing
    }

    @Override
    public void onLost(Postcard postcard) {
        // Do nothing
    }

    @Override
    public abstract void onArrival(Postcard postcard);

    @Override
    public void onInterrupt(Postcard postcard) {
        // Do nothing
    }
}
