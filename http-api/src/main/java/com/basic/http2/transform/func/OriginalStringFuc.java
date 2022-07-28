package com.basic.http2.transform.func;

import java.io.IOException;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * 获取原始String返回
 */
public class OriginalStringFuc implements Function<ResponseBody, String> {

    @Override
    public String apply(ResponseBody responseBody) throws Exception {
        try {
            return responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
