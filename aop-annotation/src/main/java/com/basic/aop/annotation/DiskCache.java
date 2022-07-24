
package com.basic.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 磁盘缓存代理注解，通过aop切片的方式在编译期间织入源代码中
 * <p>功能：缓存某方法的返回值，下次执行该方法时，直接从缓存里获取。</p>
 *

 * @since 2020/10/25 5:08 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DiskCache {
    /**
     * @return 缓存的key
     */
    String value() default "";

    /**
     * @return 缓存时间[单位：s]，默认是永久有效
     */
    long cacheTime() default -1;

    /**
     * @return 对于String、数组和集合等，是否允许缓存为空, 默认为true
     */
    boolean enableEmpty() default true;
}