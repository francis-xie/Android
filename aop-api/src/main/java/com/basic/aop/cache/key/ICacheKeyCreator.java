
package com.basic.aop.cache.key;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 缓存Key的生成器
 *
 
 * @since 2019/4/7 下午3:54
 */
public interface ICacheKeyCreator {
    /**
     * 根据签名，自动生成缓存的Key
     * @param joinPoint
     * @return
     */
    String getCacheKey(ProceedingJoinPoint joinPoint);
}
