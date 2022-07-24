
package com.basic.router.facade.template;

import com.basic.router.model.RouteInfo;

import java.util.Map;


/**
 * Provider的组
 *

 * @since 2018/5/17 上午12:10
 */
public interface IProviderGroup {
    /**
     * 加载Provider注册信息表
     *
     * @param providers input
     */
    void loadInto(Map<String, RouteInfo> providers);
}