
package com.basic.router.facade.template;

import java.util.Map;

/**
 * 拦截器组
 *

 * @since 2018/5/17 上午12:12
 */
public interface IInterceptorGroup {
    /**
     * 加载拦截器
     *
     * @param interceptor input
     */
    void loadInto(Map<Integer, Class<? extends IInterceptor>> interceptor);
}
