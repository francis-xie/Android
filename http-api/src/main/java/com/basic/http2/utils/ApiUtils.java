package com.basic.http2.utils;

import androidx.annotation.NonNull;

import com.basic.http2.model.ApiResult;

/**
 * API请求工具类
 */
public final class ApiUtils {

    private ApiUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 请求结果处理者，判断是否请求成功
     */
    private static IRequestHandler sRequestHandler = getDefaultRequestHandler();

    /**
     * 请求成功的结果码，默认为0
     */
    private static int SUCCESS_CODE = 0;

    /**
     * 设置请求结果处理者
     *
     * @param sRequestHandler 请求结果处理者
     */
    public static void setIRequestHandler(@NonNull IRequestHandler sRequestHandler) {
        ApiUtils.sRequestHandler = sRequestHandler;
    }

    /**
     * 设置请求成功的code码
     *
     * @param successCode 标识请求成功的结果码
     */
    public static void setSuccessCode(int successCode) {
        SUCCESS_CODE = successCode;
    }

    public static int getSuccessCode() {
        return SUCCESS_CODE;
    }

    /**
     * 请求是否成功
     *
     * @param apiResult 请求结果
     * @return true: 请求成功；false：请求失败
     */
    public static boolean isRequestSuccess(ApiResult apiResult) {
        return sRequestHandler != null && sRequestHandler.isRequestSuccess(apiResult);
    }

    private static IRequestHandler getDefaultRequestHandler() {
        return new IRequestHandler() {
            @Override
            public boolean isRequestSuccess(ApiResult apiResult) {
                return apiResult != null && apiResult.isSuccess();
            }
        };
    }

    /**
     * 请求结果处理者
     */
    public interface IRequestHandler {
        /**
         * 请求是否成功
         *
         * @param apiResult 请求结果
         * @return true: 请求成功；false：请求失败
         */
        boolean isRequestSuccess(ApiResult apiResult);
    }
}
