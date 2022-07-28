package com.basic.http2.callback.impl;

import java.lang.reflect.Type;

/**
 * 获取类型接口
 */
public interface IType<T> {

    /**
     * @return 获取需要实际解析的类型
     */
    Type getType();

    /**
     * @return 获取最顶层的类型
     */
    Type getRawType();

}
