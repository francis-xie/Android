
package com.basic.aop.cache.key;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 缓存Key的生成器
 */
public interface ICacheKeyCreator {
    /**
     * 根据签名，自动生成缓存的Key
     * @param joinPoint
     * @return
     */
    String getCacheKey(ProceedingJoinPoint joinPoint);
}
