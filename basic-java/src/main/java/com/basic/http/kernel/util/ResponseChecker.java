package com.basic.http.kernel.util;

import com.basic.http.HttpModel;
import com.google.common.base.Strings;

import java.lang.reflect.Field;

/**
 * 响应检查工具类
 */
public class ResponseChecker {

    public static final String SUB_CODE_FIELD_NAME = "subCode";

    /**
     * 判断一个请求返回的响应是否成功
     *
     * @param response 响应对象
     * @return true：成功；false：失败
     */
    public static boolean success(HttpModel response) {
        try {
            Field subCodeField = response.getClass().getField(SUB_CODE_FIELD_NAME);
            subCodeField.setAccessible(true);
            String subCode = (String) subCodeField.get(response);
            return Strings.isNullOrEmpty(subCode);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //没有subCode字段的响应对象，通常是那些无需跟网关远程通信的API，只要本地执行完成都视为成功
            return true;
        }
    }
}