
package com.basic.router.model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <pre>
 *     desc   : 获得泛型的类型

 *     time   : 2018/5/15 下午11:25
 * </pre>
 */
public class TypeWrapper<T> {
    protected final Type type;

    protected TypeWrapper() {
        Type superClass = getClass().getGenericSuperclass();
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }
}
