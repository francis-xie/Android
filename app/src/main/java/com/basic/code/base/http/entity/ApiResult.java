
package com.basic.code.base.http.entity;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * 提供的默认的标注返回api
 *

 * @since 2018/5/22 下午4:22
 */
@Keep
public class ApiResult<T> {
    public final static String CODE = "Code";
    public final static String MSG = "Msg";
    public final static String DATA = "Data";

    @SerializedName(value = CODE, alternate = {"code"})
    private int Code;
    @SerializedName(value = MSG, alternate = {"msg"})
    private String Msg;
    @SerializedName(value = DATA, alternate = {"data"})
    private T Data;

    public int getCode() {
        return Code;
    }

    public ApiResult setCode(int code) {
        Code = code;
        return this;
    }

    public String getMsg() {
        return Msg;
    }

    public ApiResult setMsg(String msg) {
        Msg = msg;
        return this;
    }

    public ApiResult setData(T data) {
        Data = data;
        return this;
    }

    /**
     * 获取请求响应的数据，自定义api的时候需要重写【很关键】
     *
     * @return
     */
    public T getData() {
        return Data;
    }

    /**
     * 是否请求成功,自定义api的时候需要重写【很关键】
     *
     * @return
     */
    public boolean isSuccess() {
        return getCode() == 0;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "Code='" + Code + '\'' +
                ", Msg='" + Msg + '\'' +
                ", Data=" + Data +
                '}';
    }
}
