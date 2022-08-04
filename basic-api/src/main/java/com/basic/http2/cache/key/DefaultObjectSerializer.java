package com.basic.http2.cache.key;

/**
 * 默认实现将Object转化为String的序列化器
 */
public class DefaultObjectSerializer implements IObjectSerializer {
    @Override
    public String toString(Object obj) {
        return Strings._toString(obj);
    }
}
