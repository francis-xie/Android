
package com.basic.router.facade.service;

import android.content.Context;

import com.basic.router.facade.Postcard;
import com.basic.router.facade.template.IProvider;

/**
 * 路由降级服务
 */
public interface DegradeService extends IProvider {

    /**
     * 路由丢失.
     *
     * @param postcard 路由信息
     */
    void onLost(Context context, Postcard postcard);
}
