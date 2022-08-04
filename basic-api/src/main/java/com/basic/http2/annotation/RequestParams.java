package com.basic.http2.annotation;

import com.basic.http2.Http;
import com.basic.http2.cache.model.CacheMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 网络请求实体的网络请求配置的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequestParams {
    /**
     * @return 基础请求地址
     */
    String baseUrl() default "";

    /**
     * @return 请求网络接口地址
     */
    String url() default "";

    /**
     * @return 请求超时时间
     */
    long timeout() default Http.DEFAULT_TIMEOUT_MILLISECONDS;

    /**
     * @return 是否保存json
     */
    boolean keepJson() default false;

    /**
     * @return 请求是否需要验证Token
     */
    boolean accessToken() default true;

    /**
     * @return 缓存模式
     */
    CacheMode cacheMode() default CacheMode.NO_CACHE;

    /**
     * @return 缓存有效时间
     */
    long cacheTime() default NetMethod.NOT_SET_CACHE_TIME;

}
