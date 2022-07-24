
package com.basic.router.facade.service;


import com.basic.router.facade.template.IProvider;

/**
 * 实现自动装配（依赖注入）的服务
 *

 * @since 2018/5/17 上午12:56
 */
public interface AutoWiredService extends IProvider {

    /**
     * 自动装配
     * @param instance 自动装配的目标
     */
    void autoWire(Object instance);
}
