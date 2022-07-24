
package com.basic.router.facade.service;

import android.net.Uri;

import com.basic.router.facade.template.IProvider;

/**
 * 路由路径重定向
 *
 
 * @since 2018/5/17 上午1:01
 */
public interface PathReplaceService extends IProvider {

    /**
     * 重定向普通String类型的路由路径
     *
     * @param path raw path
     */
    String forString(String path);

    /**
     * 重定向资源uri类型的路由路径
     *
     * @param uri raw uri
     */
    Uri forUri(Uri uri);
}
