
package com.basic.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     desc   : 自动try-catch的注解

 *     time   : 2018/5/14 下午10:33
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Safe {

    /**
     * @return flag 捕获异常的标志
     */
    String value() default "";
}
