
package com.basic.job.thread.utils;

import com.basic.job.thread.pool.cancel.ICancelable;

import java.util.Collection;

/**
 * 取消任务工具类
 *

 * @since 3/20/22 2:34 AM
 */
public final class CancelUtils {


    /**
     * 取消执行接口
     *
     * @param cancelable 可取消执行的实现接口
     * @return 取消执行的结果
     */
    public static boolean cancel(ICancelable cancelable) {
        if (cancelable != null && !cancelable.isCancelled()) {
            cancelable.cancel();
            return true;
        }
        return false;
    }

    /**
     * 取消执行接口集合
     *
     * @param cancelables 可取消执行的实现接口集合
     */
    public static void cancel(Collection<ICancelable> cancelables) {
        if (cancelables == null || cancelables.isEmpty()) {
            return;
        }
        for (ICancelable cancelable : cancelables) {
            cancel(cancelable);
        }
    }

    /**
     * 取消执行接口集合
     *
     * @param cancelables 可取消执行的实现接口集合
     */
    public static void cancel(ICancelable... cancelables) {
        if (cancelables == null || cancelables.length == 0) {
            return;
        }
        for (ICancelable cancelable : cancelables) {
            cancel(cancelable);
        }
    }

}
