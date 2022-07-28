package com.basic.code.utils.service;

import android.content.Context;

import com.basic.router.annotation.Router;
import com.basic.router.facade.service.SerializationService;
import com.basic.tools.net.JsonUtil;

import java.lang.reflect.Type;

@Router(path = "/service/json")
public class JsonSerializationService implements SerializationService {
    /**
     * 对象序列化为json
     *
     * @param instance obj
     * @return json string
     */
    @Override
    public String object2Json(Object instance) {
        return JsonUtil.toJson(instance);
    }

    /**
     * json反序列化为对象
     *
     * @param input json string
     * @param clazz object type
     * @return instance of object
     */
    @Override
    public <T> T parseObject(String input, Type clazz) {
        return JsonUtil.fromJson(input, clazz);
    }

    /**
     * 进程初始化的方法
     *
     * @param context 上下文
     */
    @Override
    public void init(Context context) {

    }
}
