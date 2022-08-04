
package com.basic.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     desc   : 防止View被连续点击

 *     time   : 2018/4/22 下午6:35
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SingleClick {

    long DEFAULT_INTERVAL_MILLIS = 1000;

    /**
     * @return 快速点击的间隔（ms），默认是1000ms
     */
    long value() default DEFAULT_INTERVAL_MILLIS;
}
