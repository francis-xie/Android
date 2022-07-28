package com.basic.http2.model;

import com.basic.http2.annotation.RequestParams;
import com.basic.http2.cache.model.CacheMode;
import com.basic.http2.utils.HttpUtils;
import com.basic.http2.utils.Utils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 基础网络请求实体
 */
@RequestParams
public abstract class HttpRequest {

    @Override
    public String toString() {
        return HttpUtils.toJson(this);
    }

    /**
     * 获取网络请求配置参数
     *
     * @param clazz
     * @return
     */
    public static RequestParams getHttpRequestParams(Class<? extends HttpRequest> clazz) {
        return Utils.checkNotNull(clazz.getAnnotation(RequestParams.class), "requestParams == null");
    }

    public RequestParams getRequestParams() {
        return getHttpRequestParams(getClass());
    }

    /**
     * 获取请求的url地址【作为网络请求订阅的key和缓存的key】
     *
     * @param clazz
     * @return
     */
    public static String getUrl(Class<? extends HttpRequest> clazz) {
        return getHttpRequestParams(clazz).url();
    }

    /**
     * @return 获取请求地址
     */
    public String getUrl() {
        return getRequestParams().url();
    }

    /**
     * 请求超时时间
     *
     * @return 超时时间
     */
    public long getTimeout() {
        return getRequestParams().timeout();
    }

    /**
     * 请求是否需要验证token
     *
     * @return 是否需要携带token
     */
    public boolean isAccessToken() {
        return getRequestParams().accessToken();
    }

    /**
     * @return 获取基础url
     */
    public String getBaseUrl() {
        return getRequestParams().baseUrl();
    }

    /**
     * @return 获得缓存模式
     */
    public CacheMode getCacheMode() {
        return getRequestParams().cacheMode();
    }

    /**
     * 用于通过反射获取请求返回类型的方法【不要调用】
     *
     * @return
     */
    protected abstract <T> T getResponseEntityType();

    /**
     * 解析获取请求的返回类型【默认类型：String】
     *
     * @return
     */
    public Type parseReturnType() {
        Type type = String.class;
        try {
            Method m = getClass().getDeclaredMethod("getResponseEntityType");
            m.setAccessible(true);
            type = m.getGenericReturnType();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return type;
    }
}
