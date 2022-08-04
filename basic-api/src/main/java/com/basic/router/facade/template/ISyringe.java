
package com.basic.router.facade.template;

/**
 * 注射器实现接口，实现依赖注入的方法
 */
public interface ISyringe {
    /**
     * 依赖注入
     * @param target 依赖注入的目标
     */
    void inject(Object target);
}
