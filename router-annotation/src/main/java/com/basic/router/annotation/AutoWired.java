
package com.basic.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实现自动装配（依赖注入）的注解
 *

 * @since 2018/5/17 上午12:15
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface AutoWired {

    /**
     * @return 参数的字段名／服务名, 默认是字段的参数名
     */
    String name() default "";

    /**
     * @return 是否是非空字段，If required, app will be crash when value is null.
     */
    boolean required() default false;

    /**
     * @return 字段的描述
     */
    String desc() default "No desc.";
}
