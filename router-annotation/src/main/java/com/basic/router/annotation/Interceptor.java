
package com.basic.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路由拦截器 <br>注意 : 该注解只能表注#{IInterceptor}的实现类
 *

 * @since 2018/5/17 上午12:16
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Interceptor {
    /**
     * @return 拦截器的优先级, Router将按优先级高低依次执行拦截.
     */
    int priority();

    /**
     * @return 拦截器的名称
     */
    String name() default "Default";
}
