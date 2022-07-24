
package com.basic.tools.net.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <pre>
 *     desc   : 获取范型的类型

 *     time   : 2018/4/28 上午12:51
 * </pre>
 */
public abstract class TypeToken<T> {
    private Type type;

    public TypeToken() {
        Type superclass = getClass().getGenericSuperclass();
        if(superclass instanceof Class) {
            throw new TypeException("No generics found!");
        } else {
            ParameterizedType type = (ParameterizedType)superclass;
            if (type != null) {
                this.type = type.getActualTypeArguments()[0];
            }
        }
    }

    public Type getType() {
        return type;
    }
}
