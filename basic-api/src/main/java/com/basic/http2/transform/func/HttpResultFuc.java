package com.basic.http2.transform.func;

import com.basic.http2.exception.ServerException;
import com.basic.http2.model.ApiResult;
import com.basic.http2.utils.ApiUtils;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * ApiResult<T>转换T
 */
public class HttpResultFuc<T> implements Function<ApiResult<T>, T> {

    @Override
    public T apply(@NonNull ApiResult<T> response) throws Exception {
        if (ApiUtils.isRequestSuccess(response)) {
            return response.getData();
        } else {
            throw new ServerException(response.getCode(), response.getMsg());
        }
    }
}
