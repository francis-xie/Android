package com.basic.http2;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.basic.http2.annotation.NetMethod;
import com.basic.http2.annotation.RequestParams;
import com.basic.http2.annotation.ThreadType;
import com.basic.http2.cache.RxCache;
import com.basic.http2.cache.converter.GsonDiskConverter;
import com.basic.http2.cache.key.ICacheKeyCreator;
import com.basic.http2.cache.key.IObjectSerializer;
import com.basic.http2.cache.key.Strings;
import com.basic.http2.cache.model.CacheMode;
import com.basic.http2.interceptor.HttpLoggingInterceptor;
import com.basic.http2.logs.HttpLog;
import com.basic.http2.model.HttpRequest;
import com.basic.http2.request.PostRequest;
import com.basic.http2.subsciber.BaseSubscriber;
import com.basic.http2.utils.ApiUtils;
import com.basic.http2.utils.HttpUtils;
import com.basic.http2.utils.Utils;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

import static com.basic.http2.Http.DEFAULT_RETRY_COUNT;
import static com.basic.http2.Http.DEFAULT_RETRY_DELAY;
import static com.basic.http2.Http.DEFAULT_TIMEOUT_MILLISECONDS;

/**
 * 网络请求工具
 */
public final class HttpSDK {

    private HttpSDK() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    //==============================================全局参数设置===============================================//

    /**
     * 初始化RxHttp
     *
     * @param application
     */
    public static void init(Application application) {
        Http.init(application);
        Http.getInstance()
                .setTimeout(DEFAULT_TIMEOUT_MILLISECONDS)
                .setRetryCount(DEFAULT_RETRY_COUNT)
                .setRetryDelay(DEFAULT_RETRY_DELAY)
                .setCacheMode(CacheMode.NO_CACHE)
                .setCacheDiskConverter(new GsonDiskConverter())//默认缓存使用序列化转化
                .setCacheMaxSize(50 * 1024 * 1024);//设置缓存大小为50M
    }

    /**
     * 增加全局拦截器
     *
     * @param interceptor
     */
    public static void addInterceptor(Interceptor interceptor) {
        Http.getInstance().addInterceptor(interceptor);
    }

    /**
     * 全局设置baseUrl
     *
     * @param baseUrl
     * @return true: 设置baseUrl成功
     */
    public static boolean setBaseUrl(String baseUrl) {
        if (verifyBaseUrl(baseUrl)) {
            Http.getInstance().setBaseUrl(baseUrl);
            return true;
        }
        return false;
    }

    /**
     * 验证BaseUrl是否合法
     *
     * @param baseUrl
     * @return true: 设置baseUrl成功
     */
    public static boolean verifyBaseUrl(String baseUrl) {
        if (!TextUtils.isEmpty(baseUrl)) {
            HttpUrl httpUrl = HttpUrl.parse(baseUrl);
            if (httpUrl != null) {
                List<String> pathSegments = httpUrl.pathSegments();
                return "".equals(pathSegments.get(pathSegments.size() - 1));
            }
        }
        return false;
    }

    /**
     * 全局设置SubUrl
     *
     * @param subUrl
     */
    public static void setSubUrl(String subUrl) {
        Http.getInstance().setSubUrl(subUrl);
    }

    /**
     * 设置debug模式
     */
    public static void debug() {
        Http.getInstance().debug(HttpLog.DEFAULT_LOG_TAG);
    }

    /**
     * 设置debug模式
     *
     * @param tag logFlag
     */
    public static void debug(String tag) {
        Http.getInstance().debug(tag);
    }

    /**
     * 设置debug模式
     *
     * @param loggingInterceptor 日志拦截器
     */
    public static void debug(HttpLoggingInterceptor loggingInterceptor) {
        Http.getInstance().debug(loggingInterceptor);
    }

    /**
     * 设置请求成功的code码
     *
     * @param successCode 标识请求成功的结果码
     */
    public static void setSuccessCode(int successCode) {
        ApiUtils.setSuccessCode(successCode);
    }

    /**
     * 设置Object转化为String的序列化器【自动生成缓存key时使用】
     *
     * @param sISerializer Object转化为String的序列化器
     */
    public static void setISerializer(@NonNull IObjectSerializer sISerializer) {
        Strings.setISerializer(sISerializer);
    }

    /**
     * 设置缓存Key的生成器
     *
     * @param sICacheKeyCreator 缓存Key的生成器
     */
    public static void setICacheKeyCreator(@NonNull ICacheKeyCreator sICacheKeyCreator) {
        RxCache.setICacheKeyCreator(sICacheKeyCreator);
    }

    //==============================================通用Post请求===============================================//

    /**
     * 获取PostRequest请求
     *
     * @param url        请求地址（子地址）
     * @param json       请求json参数
     * @param threadType 线程调度类型
     * @return
     */
    public static PostRequest post(String url, String json, @ThreadType String threadType) {
        return post(url, true, json, threadType);
    }

    /**
     * 获取PostRequest请求
     *
     * @param url           请求地址（子地址）
     * @param isAccessToken 是否验证Token
     * @param json          请求json参数
     * @param threadType    线程调度类型
     * @return
     */
    public static PostRequest post(String url, boolean isAccessToken, String json, @ThreadType String threadType) {
        return Http.post(url)
                .accessToken(isAccessToken)
                .upJson(json)
                .threadType(threadType);
    }

    /**
     * 获取PostRequest请求
     *
     * @param url           请求地址（子地址）
     * @param isAccessToken 是否验证Token
     * @param json          请求json参数
     * @param isSyncRequest 是否是同步请求
     * @param toMainThread  是否回到主线程
     * @return
     */
    public static PostRequest post(String url, boolean isAccessToken, String json, boolean isSyncRequest, boolean toMainThread) {
        return Http.post(url)
                .accessToken(isAccessToken)
                .upJson(json)
                .syncRequest(isSyncRequest)
                .onMainThread(toMainThread);
    }

    /**
     * 执行post请求，返回对象
     *
     * @param url           请求地址（子地址）
     * @param isAccessToken 是否验证Token
     * @param json          请求json参数
     * @param isSyncRequest 是否是同步请求
     * @param toMainThread  是否回到主线程
     * @param clazz         请求返回的类型
     * @return
     */
    public static <T> Observable<T> execute(String url, boolean isAccessToken, String json, boolean isSyncRequest, boolean toMainThread, Class<T> clazz) {
        return post(url, isAccessToken, json, isSyncRequest, toMainThread).execute(clazz);
    }

    /**
     * 执行post请求，返回对象
     *
     * @param url           请求地址（子地址）
     * @param isAccessToken 是否验证Token
     * @param json          请求json参数
     * @param isSyncRequest 是否是同步请求
     * @param toMainThread  是否回到主线程
     * @param type          请求返回的类型
     * @return
     */
    public static <T> Observable<T> execute(String url, boolean isAccessToken, String json, boolean isSyncRequest, boolean toMainThread, Type type) {
        return post(url, isAccessToken, json, isSyncRequest, toMainThread).execute(type);
    }

    //==========================================执行post请求，返回对象======================================================//

    /**
     * 执行post请求，返回对象
     *
     * @param postRequest post请求
     * @param type        请求返回的类型
     * @return
     */
    public static <T> Observable<T> execute(PostRequest postRequest, Type type) {
        return postRequest.execute(type);
    }

    /**
     * 执行post请求，返回对象
     *
     * @param postRequest post请求
     * @param clazz       请求返回的类型
     * @return
     */
    public static <T> Observable<T> execute(PostRequest postRequest, Class<T> clazz) {
        return postRequest.execute(clazz);
    }

    /**
     * 执行post请求，返回订阅信息
     *
     * @param postRequest post请求
     * @param clazz       请求返回的类型
     * @param subscriber  订阅者
     * @return
     */
    public static <T> Disposable execute(PostRequest postRequest, Class<T> clazz, BaseSubscriber<T> subscriber) {
        return addRequest(postRequest.getUrl(), postRequest.execute(clazz).subscribeWith(subscriber));
    }

    //========================================特定线程Post请求====================================//

    //==============主线程->主线程==================//

    /**
     * 获取PostRequest请求（主线程->主线程， 需要验证token）
     *
     * @param url  请求地址（子地址）
     * @param json 请求json参数
     * @return
     */
    public static PostRequest postToMain(String url, String json) {
        return post(url, true, json, false, true);
    }

    /**
     * 执行PostRequest请求，返回对象（主线程->主线程，需要验证token）
     *
     * @param url   请求地址（子地址）
     * @param json  请求json参数
     * @param clazz 请求返回的类型
     * @return
     */
    public static <T> Observable<T> postToMain(String url, String json, Class<T> clazz) {
        return execute(postToMain(url, json), clazz);
    }

    /**
     * 获取PostRequest请求（主线程->主线程）
     *
     * @param httpRequest 请求实体
     * @return
     */
    public static PostRequest postToMain(HttpRequest httpRequest) {
        return post(httpRequest, false, true);
    }

    /**
     * 执行post请求，返回对象（主线程->主线程）
     *
     * @param httpRequest 请求实体
     * @return
     */
    public static Observable executeToMain(HttpRequest httpRequest) {
        return execute(httpRequest, false, true);
    }

    /**
     * 执行post请求并订阅，返回订阅信息（主线程->主线程）
     *
     * @param httpRequest 请求实体
     * @param subscriber   订阅者
     * @return
     */
    public static <T> Disposable executeToMain(HttpRequest httpRequest, BaseSubscriber<T> subscriber) {
        return addRequest(httpRequest.getUrl(), (Disposable) executeToMain(httpRequest).subscribeWith(subscriber));
    }

    /**
     * 执行post请求并订阅，返回订阅信息（主线程->主线程）
     *
     * @param httpRequest 请求实体
     * @param subscriber   订阅者
     * @param tagName      请求标志
     * @return
     */
    public static <T> Disposable executeToMain(HttpRequest httpRequest, BaseSubscriber<T> subscriber, Object tagName) {
        return addRequest(tagName, (Disposable) executeToMain(httpRequest).subscribeWith(subscriber));
    }

    /**
     * 执行PostRequest请求，返回对象（主线程->主线程）
     *
     * @param httpRequest 请求实体
     * @param clazz        请求返回的类型
     * @return
     */
    public static <T> Observable<T> postToMain(HttpRequest httpRequest, Class<T> clazz) {
        return execute(postToMain(httpRequest), clazz);

    }

    /**
     * 执行PostRequest请求并订阅，返回订阅信息（主线程->主线程）
     *
     * @param httpRequest 请求实体
     * @param clazz        请求返回的类型
     * @param subscriber   订阅者
     * @return
     */
    public static <T> Disposable postToMain(HttpRequest httpRequest, Class<T> clazz, BaseSubscriber<T> subscriber) {
        return execute(postToMain(httpRequest), clazz, subscriber);
    }

    /**
     * 获取PostRequest请求（主线程->主线程）
     *
     * @param json  请求json参数
     * @param clazz 网络请求实体类【使用了注解配置】
     * @return
     */
    public static PostRequest postToMain(String json, Class<?> clazz) {
        return post(json, clazz, false, true);
    }

    /**
     * 获取PostRequest请求（主线程->主线程）
     *
     * @param httpRequest 请求实体【使用了注解配置请求key】
     * @return
     */
    public static PostRequest postJsonToMain(HttpRequest httpRequest) {
        return postJson(httpRequest, false, true);
    }

    //==============主线程->子线程==================//

    /**
     * 获取PostRequest请求（主线程->子线程，需要验证token）
     *
     * @param url  请求地址（子地址）
     * @param json 请求json参数
     * @return
     */
    public static PostRequest postToIO(String url, String json) {
        return post(url, true, json, false, false);
    }

    /**
     * 执行PostRequest请求，返回对象（主线程->子线程，需要验证token）
     *
     * @param url   请求地址（子地址）
     * @param json  请求json参数
     * @param clazz 请求返回的类型
     * @return
     */
    public static <T> Observable<T> postToIO(String url, String json, Class<T> clazz) {
        return execute(postToIO(url, json), clazz);
    }

    /**
     * 获取PostRequest请求（主线程->子线程）
     *
     * @param httpRequest 请求实体
     * @return
     */
    public static PostRequest postToIO(HttpRequest httpRequest) {
        return post(httpRequest, false, false);
    }

    /**
     * 执行post请求，返回对象（主线程->子线程）
     *
     * @param httpRequest 请求实体
     * @return
     */
    public static Observable executeToIO(HttpRequest httpRequest) {
        return execute(httpRequest, false, false);
    }

    /**
     * 执行post请求，返回订阅信息（主线程->子线程）
     *
     * @param httpRequest 请求实体
     * @param subscriber   订阅者
     * @return
     */
    public static <T> Disposable executeToIO(HttpRequest httpRequest, BaseSubscriber<T> subscriber) {
        return addRequest(httpRequest.getUrl(), (Disposable) executeToIO(httpRequest).subscribeWith(subscriber));
    }

    /**
     * 执行post请求，返回订阅信息（主线程->子线程）
     *
     * @param httpRequest 请求实体
     * @param subscriber   订阅者
     * @param tagName      请求标志
     * @return
     */
    public static <T> Disposable executeToIO(HttpRequest httpRequest, BaseSubscriber<T> subscriber, Object tagName) {
        return addRequest(tagName, (Disposable) executeToIO(httpRequest).subscribeWith(subscriber));
    }

    /**
     * 执行PostRequest请求，返回对象（主线程->子线程）
     *
     * @param httpRequest 请求实体
     * @param clazz        请求返回的类型
     * @return
     */
    public static <T> Observable<T> postToIO(HttpRequest httpRequest, Class<T> clazz) {
        return execute(postToIO(httpRequest), clazz);
    }

    //==============子线程->子线程==================//

    /**
     * 获取PostRequest请求（子线程->子线程，需要验证token）
     *
     * @param url  请求地址（子地址）
     * @param json 请求json参数
     * @return
     */
    public static PostRequest postInThread(String url, String json) {
        return post(url, true, json, true, false);
    }

    /**
     * 执行PostRequest请求，返回对象（子线程->子线程，需要验证token）
     *
     * @param url   请求地址（子地址）
     * @param json  请求json参数
     * @param clazz 请求返回的类型
     * @return
     */
    public static <T> Observable<T> postInThread(String url, String json, Class<T> clazz) {
        return execute(postInThread(url, json), clazz);
    }

    /**
     * 获取PostRequest请求（子线程->子线程）
     *
     * @param httpRequest 请求实体
     * @return
     */
    public static PostRequest postInThread(HttpRequest httpRequest) {
        return post(httpRequest, true, false);
    }

    /**
     * 执行post请求，返回对象（子线程->子线程）
     *
     * @param httpRequest 请求实体
     * @return
     */
    public static Observable executeInThread(HttpRequest httpRequest) {
        return execute(httpRequest, true, false);
    }

    /**
     * 执行post请求，返回订阅信息（子线程->子线程）
     *
     * @param httpRequest 请求实体
     * @param subscriber   订阅者
     * @return
     */
    public static <T> Disposable executeInThread(HttpRequest httpRequest, BaseSubscriber<T> subscriber) {
        return addRequest(httpRequest.getUrl(), (Disposable) executeInThread(httpRequest).subscribeWith(subscriber));
    }

    /**
     * 执行post请求，返回订阅信息（子线程->子线程）
     *
     * @param httpRequest 请求实体
     * @param subscriber   订阅者
     * @return
     */
    public static <T> Disposable executeInThread(HttpRequest httpRequest, BaseSubscriber<T> subscriber, Object tagName) {
        return addRequest(tagName, (Disposable) executeInThread(httpRequest).subscribeWith(subscriber));
    }

    /**
     * 执行PostRequest请求，返回对象（子线程->子线程）
     *
     * @param httpRequest 请求实体
     * @param clazz        请求返回的类型
     * @return
     */
    public static <T> Observable<T> postInThread(HttpRequest httpRequest, Class<T> clazz) {
        return execute(postInThread(httpRequest), clazz);
    }

    //======================================注解请求=======================================//

    /**
     * 【推荐使用】
     * 获取PostRequest请求（使用实体参数名作为请求Key）
     *
     * @param httpRequest  请求实体
     * @param isSyncRequest 是否是同步请求
     * @param toMainThread  是否回到主线程
     * @return
     */
    public static PostRequest post(HttpRequest httpRequest, boolean isSyncRequest, boolean toMainThread) {
        return post(httpRequest.toString(), httpRequest.getClass(), isSyncRequest, toMainThread);
    }

    /**
     * 【推荐使用】
     * 执行post请求，返回对象（使用实体参数名作为请求Key）
     *
     * @param httpRequest  请求实体
     * @param isSyncRequest 是否是同步请求
     * @param toMainThread  是否回到主线程
     * @return
     */
    public static Observable execute(HttpRequest httpRequest, boolean isSyncRequest, boolean toMainThread) {
        return execute(post(httpRequest, isSyncRequest, toMainThread), httpRequest.parseReturnType());
    }

    /**
     * 执行post请求，返回对象（使用实体参数名作为请求Key）
     *
     * @param postRequest  请求实体
     * @param httpRequest 请求实体
     * @return
     */
    public static Observable execute(PostRequest postRequest, HttpRequest httpRequest) {
        return execute(postRequest, httpRequest.parseReturnType());
    }

    /**
     * 执行post请求，返回对象（使用实体参数名作为请求Key）
     *
     * @param postRequest 请求实体
     * @param type        请求返回的类型
     * @return
     */
    public static Observable executePost(PostRequest postRequest, Type type) {
        return execute(postRequest, type);
    }

    /**
     * 执行post请求，返回订阅信息
     *
     * @param postRequest  请求对象
     * @param httpRequest 请求体
     * @param subscriber   订阅信息
     * @param tagName      请求标志
     * @param <T>
     * @return
     */
    public static <T> Disposable execute(PostRequest postRequest, HttpRequest httpRequest, BaseSubscriber<T> subscriber, Object tagName) {
        return addRequest(tagName, (Disposable) execute(postRequest, httpRequest).subscribeWith(subscriber));
    }

    /**
     * 执行post请求，返回订阅信息
     *
     * @param postRequest 请求对象
     * @param type        请求返回的类型
     * @param subscriber  订阅信息
     * @param tagName     请求标志
     * @param <T>
     * @return
     */
    public static <T> Disposable execute(PostRequest postRequest, Type type, BaseSubscriber<T> subscriber, Object tagName) {
        return addRequest(tagName, (Disposable) executePost(postRequest, type).subscribeWith(subscriber));
    }

    /**
     * 获取PostRequest请求（使用注解作为请求Key）
     *
     * @param httpRequest  请求实体【使用了注解配置请求key】
     * @param isSyncRequest 是否是同步请求
     * @param toMainThread  是否回到主线程
     * @return
     */
    public static PostRequest postJson(HttpRequest httpRequest, boolean isSyncRequest, boolean toMainThread) {
        String jsonString = httpRequest.toString();
        try {
            jsonString = HttpUtils.getAnnotationParamString(httpRequest);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return post(jsonString, httpRequest.getClass(), isSyncRequest, toMainThread);
    }

    /**
     * 获取PostRequest请求（使用实体参数名作为请求Key）
     *
     * @param json          请求json参数
     * @param clazz         网络请求实体类【使用了注解配置】
     * @param isSyncRequest 是否是同步请求
     * @param toMainThread  是否回到主线程
     * @return
     */
    public static PostRequest post(String json, Class<?> clazz, boolean isSyncRequest, boolean toMainThread) {
        RequestParams requestParams = clazz.getAnnotation(RequestParams.class);
        Utils.checkNotNull(requestParams, "requestParams == null");

        String baseUrl = requestParams.baseUrl();
        String url = requestParams.url();
        long timeout = requestParams.timeout();
        boolean accessToken = requestParams.accessToken();
        CacheMode cacheMode = requestParams.cacheMode();

        PostRequest postRequest = Http.post(url);
        if (!TextUtils.isEmpty(baseUrl)) {
            postRequest.baseUrl(baseUrl);
        }
        if (!CacheMode.NO_CACHE.equals(cacheMode)) {
            postRequest.cacheMode(cacheMode).cacheKey(url);
            long cacheTime = requestParams.cacheTime();
            if (cacheTime != NetMethod.NOT_SET_CACHE_TIME) {
                postRequest.cacheTime(cacheTime);
            }
        }
        if (timeout <= 0) {   //如果超时时间小于等于0，使用默认的超时时间
            timeout = Http.DEFAULT_TIMEOUT_MILLISECONDS;
        }
        return postRequest
                .accessToken(accessToken)
                .timeOut(timeout)
                .upJson(json)
                .keepJson(requestParams.keepJson())
                .syncRequest(isSyncRequest)
                .onMainThread(toMainThread);
    }


    //================================网络请求取消=================================================//

    /**
     * 注册网络请求的订阅
     *
     * @param tagName
     * @param disposable
     */
    public static Disposable addRequest(Object tagName, Disposable disposable) {
        return HttpRequestPool.get().add(tagName, disposable);
    }

    /**
     * 注册网络请求的订阅
     *
     * @param tagName
     * @param disposable
     */
    public static Disposable addRequest(Disposable disposable, Object tagName) {
        return HttpRequestPool.get().add(disposable, tagName);
    }

    /**
     * 取消注册网络请求的订阅
     *
     * @param tagName
     */
    public static void cancelRequest(Object tagName) {
        HttpRequestPool.get().remove(tagName);
    }

    /**
     * 取消所有的网络请求订阅
     */
    public static void cancelAll() {
        HttpRequestPool.get().removeAll();
    }

}
