package com.basic.code.core.http.api;

import com.basic.code.core.http.entity.TipInfo;
import com.basic.http2.model.ApiResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public class ApiService {

    /**
     * 使用的是retrofit的接口定义
     */
    public interface IGetService {

        /**
         * 获得小贴士
         */
        @GET("/zhiqiang/Resource/raw/master/jsonapi/tips.json")
        Observable<ApiResult<List<TipInfo>>> getTips();
    }

}
