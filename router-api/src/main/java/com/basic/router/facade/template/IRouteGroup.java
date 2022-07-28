
package com.basic.router.facade.template;

import com.basic.router.model.RouteInfo;

import java.util.Map;

/**
 * 路由的组元素
 */
public interface IRouteGroup {
    /**
     * 将路由信息填充至路由组
     */
    void loadInto(Map<String, RouteInfo> atlas);
}
