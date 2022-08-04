
package com.basic.job.thread.pool.base;

import com.basic.job.logger.TaskLogger;
import com.basic.job.thread.priority.IPriority;
import com.basic.job.thread.utils.PriorityUtils;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * ScheduledThreadPoolExecutor线程池基类
 * <p>
 * maximumPoolSize：Integer.MAX_VALUE
 * keepAliveTime：10L
 * unit：MILLISECONDS
 * workQueue：DelayedWorkQueue
 */
public class BaseScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    private static final String TAG = TaskLogger.getLogTag("BaseScheduledThreadPoolExecutor");

    /**
     * 构造方法
     *
     * @param corePoolSize 核心(执行)线程池的线程数
     */
    public BaseScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    /**
     * 构造方法
     *
     * @param corePoolSize  核心(执行)线程池的线程数
     * @param threadFactory 线程创建工厂
     */
    public BaseScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    /**
     * 构造方法
     *
     * @param corePoolSize 核心(执行)线程池的线程数
     * @param handler      拒绝执行的处理
     */
    public BaseScheduledThreadPoolExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    /**
     * 构造方法
     *
     * @param corePoolSize  核心(执行)线程池的线程数
     * @param threadFactory 线程创建工厂
     * @param handler       拒绝执行的处理
     */
    public BaseScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        if (t == null) {
            return;
        }
        if (!TaskLogger.isLogThreadName()) {
            return;
        }
        if (r instanceof IPriority) {
            TaskLogger.dTag(TAG, "Running task start execute, id:" + ((IPriority) r).getId() + ", priority:" + ((IPriority) r).priority() + ", in Thread [" + Thread.currentThread().getName() + "]");
        } else {
            TaskLogger.dTag(TAG, "Running task start execute, in Thread [" + Thread.currentThread().getName() + "]");
        }
    }

    /**
     * 线程执行结束，顺便看一下有么有什么乱七八糟的异常
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t == null && r instanceof Future<?> && ((Future<?>) r).isDone()) {
            try {
                ((Future<?>) r).get();
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            TaskLogger.wTag(TAG, "Running task appeared exception! Thread [" + Thread.currentThread().getName() + "], because [" + t.getMessage() + "]\n" + PriorityUtils.formatStackTrace(t.getStackTrace()));
        }
    }
}
