
package com.basic.job.thread.utils;

import com.basic.job.thread.priority.IPriority;
import com.basic.job.thread.priority.IPriorityStrategy;
import com.basic.job.thread.priority.impl.DefaultPriorityStrategy;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 优先级工具类
 *

 * @since 2021/10/9 12:08 PM
 */
public final class PriorityUtils {

    private static final AtomicLong SEQ = new AtomicLong(0);

    private static IPriorityStrategy sPriorityStrategy = new DefaultPriorityStrategy();

    private PriorityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 自动生成id
     *
     * @return 生成的id
     */
    public static long generateId() {
        return SEQ.getAndIncrement();
    }

    /**
     * 设置优先级策略实现接口
     *
     * @param sPriorityStrategy 优先级策略
     */
    public static void setPriorityStrategy(IPriorityStrategy sPriorityStrategy) {
        PriorityUtils.sPriorityStrategy = sPriorityStrategy;
    }

    /**
     * 比较两个优先级
     *
     * @param priority 优先级
     * @param other    比较的优先级
     * @return 比较结果，如果priority比other的优先级高，那么结果<0;
     */
    public static int compare(IPriority priority, IPriority other) {
        if (sPriorityStrategy == null) {
            sPriorityStrategy = new DefaultPriorityStrategy();
        }
        return sPriorityStrategy.compare(priority, other);
    }

    /**
     * 格式化堆栈信息
     *
     * @param stackTrace 堆栈信息数组
     */
    public static String formatStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            sb.append("    at ").append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

}
