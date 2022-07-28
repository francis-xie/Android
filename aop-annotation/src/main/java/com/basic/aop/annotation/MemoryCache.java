
package com.basic.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 内存缓存代理注解，通过aop切片的方式在编译期间织入源代码中
 * <p>功能：缓存某方法的返回值，下次执行该方法时，直接从缓存里获取。</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MemoryCache {
    /**
     * @return 内存缓存的key
     */
    String value() default "";

    /**
     * @return 对于String、数组和集合等，是否允许缓存为空, 默认为true
     */
    boolean enableEmpty() default true;
}
