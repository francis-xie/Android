
package com.basic.job.thread.pool.base;

import com.basic.job.logger.TaskLogger;
import com.basic.job.thread.priority.IPriority;
import com.basic.job.thread.utils.PriorityUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolExecutor线程池基类
 * <p>
 * 线程池的执行顺序：
 * <p>
 * 核心线程池(A) -> 阻塞队列(B) -> 最大线程数(C)[新建线程] -> RejectedExecutionHandler
 * <p>
 * 一个线程最多同时容纳的任务数= 阻塞队列(B) + 最大线程数(C)
 * <p>
 * allowCoreThreadTimeOut为true, 线程池数量最后销毁到0个。为false时, 线程池数量最后销毁到核心线程数。
 *
 * 阻塞队列:
 *
 * 1.ArrayBlockingQueue、LinkedBlockingQueue是有界阻塞队列。如果直接使用LinkedBlockingQueue的默认构造函数（默认大小是Integer.MAX_VALUE）可视为无界阻塞队列。
 * 2.PriorityBlockingQueue是无界阻塞队列。
 * 3.SynchronousQueue是无缓冲队列，一般设置这个的时候maximumPoolSize需要设置为Integer.MAX_VALUE
 *

 * @since 2021/12/16 2:05 AM
 */
public class BaseThreadPoolExecutor extends ThreadPoolExecutor {

    private static final String TAG = TaskLogger.getLogTag("BaseThreadPoolExecutor");

    /**
     * 构造方法
     *
     * @param corePoolSize    核心(执行)线程池的线程数
     * @param maximumPoolSize 线程池最大能容纳的线程数
     * @param keepAliveTime   当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
     * @param unit            等待时长的单位
     * @param workQueue       线程池工作队列，用于在任务完成之前保留任务的队列执行
     */
    public BaseThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * 构造方法
     *
     * @param corePoolSize    核心(执行)线程池的线程数
     * @param maximumPoolSize 线程池最大能容纳的线程数
     * @param keepAliveTime   当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
     * @param unit            等待时长的单位
     * @param workQueue       线程池工作队列，用于在任务完成之前保留任务的队列执行
     * @param threadFactory   线程创建工厂
     */
    public BaseThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    /**
     * 构造方法
     *
     * @param corePoolSize    核心(执行)线程池的线程数
     * @param maximumPoolSize 线程池最大能容纳的线程数
     * @param keepAliveTime   当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
     * @param unit            等待时长的单位
     * @param workQueue       线程池工作队列，用于在任务完成之前保留任务的队列执行
     * @param handler         拒绝执行的处理
     */
    public BaseThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    /**
     * 构造方法
     *
     * @param corePoolSize    核心(执行)线程池的线程数
     * @param maximumPoolSize 线程池最大能容纳的线程数
     * @param keepAliveTime   当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
     * @param unit            等待时长的单位
     * @param workQueue       线程池工作队列，用于在任务完成之前保留任务的队列执行
     * @param threadFactory   线程创建工厂
     * @param handler         拒绝执行的处理
     */
    public BaseThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
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
