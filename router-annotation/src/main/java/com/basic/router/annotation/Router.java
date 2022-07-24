
package com.basic.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路由创建注解
 *

 * @since 2018/5/17 上午12:32
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Router {

    /**
     * @return 路由的路径，必填
     */
    String path();

    /**
     * @return 路由所在的组
     */
    String group() default "";

    /**
     * @return 路由的名称
     */
    String name() default "undefined";
    
    /**
     * @return 路由的拓展属性，这个属性是一个int值，换句话说，单个int有4字节，也就是32位，可以配置32个开关。
     * Ps. U should use the integer num sign the switch, by bits. 10001010101010
     */
    int extras() default Integer.MIN_VALUE;

    /**
     * @return 路由的优先级
     */
    int priority() default -1;

}
