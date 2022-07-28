package com.basic.http2.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 获取范型的类型
 */
public abstract class TypeToken<T> {
    private final Type type;

    public TypeToken() {
        Type superclass = getClass().getGenericSuperclass();
        if(superclass instanceof Class) {
            throw new TypeException("No generics found!");
        } else {
            ParameterizedType type = (ParameterizedType)superclass;
            this.type = type.getActualTypeArguments()[0];
        }
    }

    public Type getType() {
        return type;
    }
}
