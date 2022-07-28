package com.basic.http2.exception;

import androidx.annotation.NonNull;

/**
 * 错误信息处理
 */
public final class ApiExceptionHandler {

    /**
     * 默认的错误信息处理者
     */
    private static IExceptionHandler sIExceptionHandler;

    /**
     * 设置错误信息处理者
     * @param exceptionHandler
     */
    public static void setExceptionHandler(@NonNull IExceptionHandler exceptionHandler) {
        sIExceptionHandler = exceptionHandler;
    }

    /**
     * 处理过滤错误信息
     * @param e
     * @return
     */
    public static ApiException handleException(Throwable e) {
        if (sIExceptionHandler != null) {
            return sIExceptionHandler.handleException(e);
        } else {
            return ApiException.handleException(e);
        }
    }
}
