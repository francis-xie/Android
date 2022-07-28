package com.basic.http2;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.basic.http2.annotation.NetMethod;
import com.basic.http2.annotation.ThreadType;
import com.basic.http2.cache.RxCache;
import com.basic.http2.cache.model.CacheMode;
import com.basic.http2.exception.ApiException;
import com.basic.http2.request.BaseBodyRequest;
import com.basic.http2.request.BaseRequest;
import com.basic.http2.utils.HttpUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

import static com.basic.http2.annotation.NetMethod.GET;
import static com.basic.http2.annotation.NetMethod.POST;
import static com.basic.http2.annotation.NetMethod.PUT;

/**
 * 网络请求代理[这里只是一种演示，可以模仿着自定义]
 * <p>
 * 请结合@NetMethod注解注释的接口使用
 */
public class HttpProxy implements InvocationHandler {

    /**
     * paramType为JSON_OBJECT时，参数有且只能有一个
     */
    public static final int JSON_OBJECT_METHOD_PARAM_NUMBER = 1;

    /**
     * 网络请求代理
     *
     * @param cls 代理的请求接口
     * @param <T>
     * @return
     */
    public static <T> T proxy(Class<T> cls) {
        return new HttpProxy().create(cls);
    }

    /**
     * 网络请求代理
     *
     * @param cls        代理的请求接口
     * @param threadType 线程调度类型
     * @param <T>
     * @return
     */
    public static <T> T proxy(Class<T> cls, @ThreadType String threadType) {
        return new HttpProxy(threadType).create(cls);
    }

    /**
     * 线程调度类型
     */
    private String mThreadType;

    /**
     * 构造方法
     */
    public HttpProxy() {
        this(ThreadType.TO_MAIN);
    }

    /**
     * 构造方法
     *
     * @param threadType 线程调度类型
     */
    public HttpProxy(@ThreadType String threadType) {
        mThreadType = threadType;
    }


    public <T> T create(Class<T> cls) {
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, this);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        NetMethod netMethod = method.getAnnotation(NetMethod.class);
        if (netMethod == null) {
            throw new ApiException(method.getName() + "方法无NetMethod注释", ApiException.ERROR.NET_METHOD_ANNOTATION_ERROR);
        }
        if (netMethod.paramType() == NetMethod.JSON_OBJECT) {
            if (method.getGenericParameterTypes().length != JSON_OBJECT_METHOD_PARAM_NUMBER) {
                throw new ApiException(method.getName() + "方法NetMethod的paramType为JSON_OBJECT时，接口的方法参数必须是一个", ApiException.ERROR.NET_METHOD_ANNOTATION_ERROR);
            }
        } else {
            if (netMethod.parameterNames().length != method.getGenericParameterTypes().length) {
                throw new ApiException(method.getName() + "方法NetMethod注释与实际参数个数不对应", ApiException.ERROR.NET_METHOD_ANNOTATION_ERROR);
            }
        }

        BaseRequest request = getHttpRequest(method, args, netMethod);
        if (request instanceof BaseBodyRequest) {
            if (netMethod.paramType() == NetMethod.JSON) {
                ((BaseBodyRequest) request).upJson(HttpUtils.toJson(getParamsMap(method, args, netMethod)));
            } else if (netMethod.paramType() == NetMethod.JSON_OBJECT) {
                // JSON_OBJECT直接取第一个参数进行序列化
                ((BaseBodyRequest) request).upJson(HttpUtils.toJson(args[0]));
            } else {
                request.params(getParamsMap(method, args, netMethod));
            }
        } else {
            if (netMethod.paramType() == NetMethod.URL_GET) {
                if (args.length > 0) {
                    request.url(netMethod.url() + "/" + args[0]);
                    request.params(getParamsMap(method, args, netMethod, 1));
                }
            } else {
                request.params(getParamsMap(method, args, netMethod));
            }
        }
        return request.execute(getReturnType(method));
    }

    /**
     * 获取网络请求接口返回值的类型
     *
     * @param method 方法信息
     * @return
     * @throws ApiException
     */
    private Type getReturnType(Method method) throws ApiException {
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            type = ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            throw new ApiException("接口方法:" + method.getName() + "的返回值类型不是泛型, 必须返回Observable<T>类型", ApiException.ERROR.NET_METHOD_ANNOTATION_ERROR);
        }
        return type;
    }

    /**
     * 获取请求实体
     *
     * @param method    请求方法
     * @param args      请求参数
     * @param apiMethod 请求信息
     * @return 请求实体
     */
    private BaseRequest getHttpRequest(Method method, Object[] args, NetMethod apiMethod) {
        String baseUrl = apiMethod.baseUrl();
        String url = apiMethod.url();
        long timeout = apiMethod.timeout();
        CacheMode cacheMode = apiMethod.cacheMode();

        BaseRequest request;
        switch (apiMethod.action()) {
            case POST:
                request = Http.post(url);
                break;
            case GET:
                request = Http.get(url);
                break;
            case PUT:
                request = Http.put(url);
                break;
            default:
                request = Http.delete(url);
                break;
        }
        if (!TextUtils.isEmpty(baseUrl)) {
            request.baseUrl(baseUrl);
        }
        if (!CacheMode.NO_CACHE.equals(cacheMode)) {
            request.cacheMode(cacheMode).cacheKey(getCacheKey(method, args, apiMethod));
            long cacheTime = apiMethod.cacheTime();
            if (cacheTime != NetMethod.NOT_SET_CACHE_TIME) {
                request.cacheTime(cacheTime);
            }
        }
        //如果超时时间小于等于0，使用默认的超时时间
        if (timeout <= 0) {
            timeout = Http.DEFAULT_TIMEOUT_MILLISECONDS;
        }
        return request
                .threadType(mThreadType)
                .keepJson(apiMethod.keepJson())
                .accessToken(apiMethod.accessToken())
                .timeOut(timeout);
    }


    /**
     * 获取请求参数集合
     *
     * @param method    请求方法
     * @param args      请求参数
     * @param apiMethod 请求信息
     * @return 请求参数集合
     */
    @NonNull
    private Map<String, Object> getParamsMap(Method method, Object[] args, NetMethod apiMethod) {
        return getParamsMap(method, args, apiMethod, 0);
    }

    /**
     * 获取请求参数集合
     *
     * @param method    请求方法
     * @param args      请求参数
     * @param apiMethod 请求信息
     * @param index     参数索引
     * @return 请求参数集合
     */
    @NonNull
    private Map<String, Object> getParamsMap(Method method, Object[] args, NetMethod apiMethod, int index) {
        Map<String, Object> params = new TreeMap<>();
        Type[] parameters = method.getGenericParameterTypes();
        for (int i = index; i < parameters.length; i++) {
            params.put(apiMethod.parameterNames()[i], args[i]);
        }
        return params;
    }

    /**
     * 根据网络请求的请求接口方法，自动生成缓存的Key
     *
     * @param method    请求方法
     * @param args      请求参数
     * @param apiMethod 请求信息
     * @return 缓存的key
     */
    @NonNull
    private String getCacheKey(Method method, Object[] args, NetMethod apiMethod) {
        return RxCache.getICacheKeyCreator().getCacheKey(method, args, apiMethod);
    }

}
