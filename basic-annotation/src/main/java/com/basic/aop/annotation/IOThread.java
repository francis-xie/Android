
package com.basic.aop.annotation;

import com.basic.aop.enums.ThreadType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     desc   : 子线程注解

 *     time   : 2018/4/23 上午1:00
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IOThread {

    /**
     * @return 子线程的类型，默认是多线程池
     */
    ThreadType value() default ThreadType.Fixed;
}
