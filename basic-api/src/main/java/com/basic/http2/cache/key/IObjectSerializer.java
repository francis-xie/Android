package com.basic.http2.cache.key;

/**
 * 将Object转化为String的序列化器
 */
public interface IObjectSerializer {

    /**
     * 将Object转化为String
     *
     * @param obj 转化对象
     * @return 转化结果
     */
    String toString(Object obj);
}
