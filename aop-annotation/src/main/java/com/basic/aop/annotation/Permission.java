
package com.basic.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     desc   : 申请系统权限注解

 *     time   : 2018/4/22 下午6:34
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Permission {
    /**
     * @return 需要申请权限的集合
     */
    String[] value();
}