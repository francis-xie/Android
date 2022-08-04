
package com.basic.job.thread.pool.cancel;

import java.util.Collection;

/**
 * 取消者订阅池
 */
public interface ICancellerPool {

    /**
     * 添加取消者
     *
     * @param name       取消者名称
     * @param cancelable 取消接口
     * @return 是否执行成功
     */
    boolean add(String name, ICancelable cancelable);

    /**
     * 去除取消者
     *
     * @param name 取消者名称
     * @return 是否执行成功
     */
    boolean remove(String name);

    /**
     * 指定取消者执行
     *
     * @param name 取消者名称
     * @return 是否执行成功
     */
    boolean cancel(String name);

    /**
     * 指定取消者集合执行
     *
     * @param names 取消者集合
     */
    void cancel(String... names);

    /**
     * 指定取消者集合执行
     *
     * @param names 取消者集合
     */
    void cancel(Collection<String> names);

    /**
     * 所有取消者执行
     */
    void cancelAll();

    /**
     * 清楚所有
     *
     * @param ifNeedCancel 是否在清除前取消
     */
    void clear(boolean ifNeedCancel);

}
