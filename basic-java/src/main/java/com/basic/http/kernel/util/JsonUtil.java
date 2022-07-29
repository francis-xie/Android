package com.basic.http.kernel.util;

import com.basic.http.HttpModel;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * JSON工具类
 */
public class JsonUtil {
    /**
     * 将Map转换为Json字符串，转换过程中对于HttpModel，使用标注的字段名称而不是字段的变量名
     *
     * @param input 输入的Map
     * @return Json字符串
     */
    public static String toJsonString(Map<String, ?> input) {
        Map<String, Object> result = new HashMap<>();
        for (Entry<String, ?> pair : input.entrySet()) {
            if (pair.getValue() instanceof HttpModel) {
                result.put(pair.getKey(), getHttpModelMap((HttpModel) pair.getValue()));
            } else {
                result.put(pair.getKey(), pair.getValue());
            }
        }
        return new Gson().toJson(result);
    }

    private static Map<String, Object> getHttpModelMap(HttpModel httpModel) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> teaModelMap = httpModel.toMap();
        for (Entry<String, Object> pair : teaModelMap.entrySet()) {
            if (pair.getValue() instanceof HttpModel) {
                result.put(pair.getKey(), getHttpModelMap((HttpModel) pair.getValue()));
            } else {
                result.put(pair.getKey(), pair.getValue());
            }
        }
        return result;
    }
}