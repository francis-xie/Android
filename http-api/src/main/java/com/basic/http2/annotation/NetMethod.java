package com.basic.http2.annotation;

import com.basic.http2.Http;
import com.basic.http2.cache.model.CacheMode;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 网络请求的请求接口方法的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NetMethod {
    /**
     * JSON Map({"key1": value1, "key2": value2})
     */
    int JSON = 1;
    /**
     * 表单(URL?appId=XXX)
     */
    int FORM_BODY = 2;
    /**
     * 直接拼接到get的url后面（URL/appId）,只有get有效，且只有第一个参数为有效路径。
     */
    int URL_GET = 3;
    /**
     * JSON Object(请求参数必须是Object，也可以是Object Array)，但是请求接口的方法参数有且只能有一个，不需要设置parameterNames。
     */
    int JSON_OBJECT = 4;

    String POST = "post";
    String GET = "get";
    String PUT = "put";
    String DELETE = "delete";

    /**
     * -1代表永久有效，-2就代表不设置,使用全局缓存有效时间
     */
    long NOT_SET_CACHE_TIME = -2;

    /**
     * 所有请求参数都作为缓存key的索引
     */
    int ALL_PARAMS_INDEX = -1;

    /**
     * 参数名集合, 当paramType为 JSON_OBJECT 时不起作用
     *
     * @return 参数名集合
     */
    String[] parameterNames() default {};

    /**
     * @return param的类型，默认是json
     */
    int paramType() default JSON;

    /**
     * @return 请求动作
     */
    String action() default POST;

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
     * @return 作为缓存key的请求参数索引，默认是-1，也就是全部参数
     */
    int cacheKeyIndex() default ALL_PARAMS_INDEX;

    /**
     * @return 缓存有效时间
     */
    long cacheTime() default NOT_SET_CACHE_TIME;
}

