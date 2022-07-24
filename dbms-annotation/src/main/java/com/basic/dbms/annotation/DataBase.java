
package com.basic.dbms.annotation;

import com.basic.dbms.enums.DataBaseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     desc   : 数据库配置注解

 *     time   : 2018/5/9 上午12:23
 * </pre>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface DataBase {

    /**
     * 默认数据库版本
     */
    int DEFAULT_VERSION = 1;

    /**
     * @return 数据库的名称
     */
    String name();

    /**
     * @return 数据库的版本, 默认版本为1
     */
    int version() default DEFAULT_VERSION;

    /**
     * @return 数据库的存放类型，默认是内部存储
     */
    DataBaseType type() default DataBaseType.INTERNAL;

    /**
     * @return 数据库存放的路径，只对{@link DataBaseType#EXTERNAL}外部存储的数据库起作用
     */
    String path() default "";

}
