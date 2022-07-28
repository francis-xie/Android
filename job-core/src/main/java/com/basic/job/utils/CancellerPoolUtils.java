
package com.basic.job.utils;

import com.basic.job.thread.pool.cancel.ICancelable;
import com.basic.job.thread.pool.cancel.ICancellerPool;
import com.basic.job.thread.pool.cancel.TaskCancellerPool;

import java.util.Collection;

/**
 * 取消者订阅池工具类
 */
public final class CancellerPoolUtils {

    private static ICancellerPool sCancellerPool = new TaskCancellerPool();

    private CancellerPoolUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 设置自定义的取消者订阅池
     *
     * @param sCancellerPool 取消者订阅池
     */
    public static void setCancellerPool(ICancellerPool sCancellerPool) {
        CancellerPoolUtils.sCancellerPool = sCancellerPool;
    }

    /**
     * 添加取消者
     *
     * @param name       取消者名称
     * @param cancelable 取消接口
     * @return 是否执行成功
     */
    public static boolean add(String name, ICancelable cancelable) {
        return sCancellerPool.add(name, cancelable);
    }

    /**
     * 去除取消者
     *
     * @param name 取消者名称
     * @return 是否执行成功
     */
    public static boolean remove(String name) {
        return sCancellerPool.remove(name);
    }

    /**
     * 指定取消者执行
     *
     * @param name 取消者名称
     * @return 是否执行成功
     */
    public static boolean cancel(String name) {
        return sCancellerPool.cancel(name);
    }

    /**
     * 指定取消者集合执行
     *
     * @param names 取消者集合
     */
    public static void cancel(String... names) {
        sCancellerPool.cancel(names);
    }


    /**
     * 指定取消者集合执行
     *
     * @param names 取消者集合
     */
    public static void cancel(Collection<String> names) {
        sCancellerPool.cancel(names);
    }

    /**
     * 所有取消者执行
     */
    public static void cancelAll() {
        sCancellerPool.cancelAll();
    }

    /**
     * 清除所有
     *
     * @param ifNeedCancel 是否在清除前取消
     */
    public static void clear(boolean ifNeedCancel) {
        sCancellerPool.clear(ifNeedCancel);
    }

}
