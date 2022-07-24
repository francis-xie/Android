
package com.basic.router.utils;

/**
 * XRouter的常量
 *
 
 * @since 2018/5/17 下午11:44
 */
public final class Consts {
    public static final String SDK_NAME = "Router";
    public static final String TAG = SDK_NAME + "::";
    public static final String SEPARATOR = "$$";
    public static final String SUFFIX_ROOT = "Root";
    public static final String SUFFIX_INTERCEPTORS = "Interceptors";
    public static final String SUFFIX_PROVIDERS = "Providers";
    public static final String SUFFIX_AUTOWIRED = SEPARATOR + SDK_NAME + SEPARATOR + "AutoWired";
    public static final String DOT = ".";
    public static final String ROUTE_ROOT_PAKCAGE = "com.basic.router.routes";

    public static final String ROUTE_ROOT_SEIVICE = "com.basic.router.facade.service";

    public static final String ROUTE_SERVICE_INTERCEPTORS = "/router/service/interceptor";
    public static final String ROUTE_SERVICE_AUTOWIRED = "/router/service/autowired";
    /**
     * 路由缓存
     */
    public static final String XROUTER_SP_CACHE_KEY = "SP_XROUTER_CACHE";
    public static final String XROUTER_SP_KEY_MAP = "SP_XROUTER_MAP";
    public static final String LAST_VERSION_NAME = "LAST_VERSION_NAME";
    public static final String LAST_VERSION_CODE = "LAST_VERSION_CODE";
}
