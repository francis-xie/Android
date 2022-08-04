
package com.basic.router.facade.template;

import com.basic.router.model.RouteInfo;

import java.util.Map;


/**
 * Provider的组
 */
public interface IProviderGroup {
    /**
     * 加载Provider注册信息表
     *
     * @param providers input
     */
    void loadInto(Map<String, RouteInfo> providers);
}