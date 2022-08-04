
package com.basic.job.thread.utils;

import android.os.Looper;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

/**
 * 线程池工具
 */
public final class ExecutorUtils {

    private ExecutorUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 关闭线程池
     *
     * @param executor 线程池
     */
    public static void shutdown(ExecutorService executor) {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    /**
     * 关闭线程池
     *
     * @param executors 线程池集合
     */
    public static void shutdown(Collection<? extends ExecutorService> executors) {
        if (executors == null || executors.isEmpty()) {
            return;
        }
        for (ExecutorService executor : executors) {
            shutdown(executor);
        }
    }

    /**
     * 是否是主线程
     *
     * @return 是否是主线程
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

}
