
package com.basic.router.facade.template;

import java.util.Map;

/**
 * 路由的根元素
 *

 * @since 2018/5/16 下午11:58
 */
public interface IRouteRoot {

    /**
     * 加载路由组元素
     * @param routes input
     */
    void loadInto(Map<String, Class<? extends IRouteGroup>> routes);
}
