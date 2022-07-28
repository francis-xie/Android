package com.basic.http2.interceptor;

import com.basic.http2.model.ExpiredInfo;

import okhttp3.Response;

/**
 * 判断响应是否失效的处理拦截器<br>
 * 继承后扩展各种失效响应处理：包括token过期、账号异地登录、时间戳过期、签名sign错误等<br>
 */
public abstract class BaseExpiredInterceptor extends BaseResponseInterceptor {

    @Override
    protected Response onAfterRequest(Response response, Chain chain, String bodyString) {
        ExpiredInfo expiredInfo = isResponseExpired(response, bodyString);
        if (expiredInfo.isExpired()) {
            Response tmp = responseExpired(response, chain, expiredInfo);
            if (tmp != null) {
                return tmp;
            }
        }
        return response;
    }


    /**
     * 判断是否是失效的响应
     *
     * @param oldResponse
     * @param bodyString
     * @return {@code true} : 失效 <br>  {@code false} : 有效
     */
    protected abstract ExpiredInfo isResponseExpired(Response oldResponse, String bodyString);

    /**
     * 失效响应的处理
     *
     * @return 获取新的有效请求响应
     */
    protected abstract Response responseExpired(Response oldResponse, Chain chain, ExpiredInfo expiredInfo);



}
