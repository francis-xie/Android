
package com.basic.aop.checker;

import org.aspectj.lang.JoinPoint;

/**
 * <pre>
 *     desc   : 自定义拦截切片的拦截器实现接口

 *     time   : 2018/4/24 下午10:11
 * </pre>
 */
public interface Interceptor {

    /**
     * 执行拦截
     * @param type 拦截的类型
     * @param joinPoint 切片切点
     * @return {@code true}: 拦截切片的执行 <br>{@code false}: 不拦截切片的执行
     * @throws Throwable
     */
    boolean intercept(int type, JoinPoint joinPoint) throws Throwable;

}
