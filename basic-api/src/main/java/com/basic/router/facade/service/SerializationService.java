
package com.basic.router.facade.service;

import com.basic.router.facade.template.IProvider;

import java.lang.reflect.Type;

/**
 * 序列化服务
 */
public interface SerializationService extends IProvider {

    /**
     * 对象序列化为json
     *
     * @param instance obj
     * @return json string
     */
    String object2Json(Object instance);

    /**
     * json反序列化为对象
     *
     * @param input json string
     * @param clazz object type
     * @return instance of object
     */
    <T> T parseObject(String input, Type clazz);
}
