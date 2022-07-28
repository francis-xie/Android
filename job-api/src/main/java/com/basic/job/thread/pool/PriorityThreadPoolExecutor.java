
package com.basic.job.thread.pool;

import com.basic.job.logger.TaskLogger;
import com.basic.job.thread.pool.base.BaseThreadPoolExecutor;
import com.basic.job.thread.priority.IPriority;
import com.basic.job.thread.priority.IPriorityComparable;
import com.basic.job.thread.priority.IPriorityFuture;
import com.basic.job.thread.priority.IPriorityRunnable;
import com.basic.job.thread.priority.impl.DefaultPriorityCallable;
import com.basic.job.thread.priority.impl.DefaultPriorityFuture;
import com.basic.job.thread.priority.impl.DefaultPriorityRunnable;
import com.basic.job.thread.priority.impl.Priority;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 一个具有指定和动态调整任务优先级能力的Java线程池，阻塞队列是PriorityBlockingQueue(无界），按优先级进行排序。
 * 注意：PriorityBlockingQueue是无界阻塞队列。
 * <p>
 * 线程池的执行顺序：
 * <p>
 * 核心线程池 -> 阻塞队列 -> 最大线程数(新建线程） -> RejectedExecutionHandler
 * <p>
 * 因此，该线程池起作用的主要是在阻塞队列这一层。
 */
public class PriorityThreadPoolExecutor extends BaseThreadPoolExecutor {

    private static final String TAG = TaskLogger.getLogTag("PriorityThreadPoolExecutor");

    /**
     * 获取默认配置的优先级线程池
     *
     * @return 线程池
     */
    public static PriorityThreadPoolExecutor getDefault() {
        return new Builder().build();
    }

    /**
     * 获取优先级线程池的构建者
     *
     * @return 构建者
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * 获取优先级线程池的构建者
     *
     * @param corePoolSize 核心(执行)线程池的线程数
     * @return 构建者
     */
    public static Builder newBuilder(int corePoolSize) {
        return new Builder(corePoolSize);
    }

    /**
     * 获取优先级线程池的构建者
     *
     * @param corePoolSize  核心(执行)线程池的线程数
     * @param keepAliveTime 当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
     * @param unit          等待时长的单位
     * @return 构建者
     */
    public static Builder newBuilder(int corePoolSize, long keepAliveTime, TimeUnit unit) {
        return new Builder(corePoolSize, keepAliveTime, unit);
    }

    /**
     * 构造方法
     *
     * @param corePoolSize    核心(执行)线程池的线程数
     * @param maximumPoolSize 线程池最大能容纳的线程数，因为PriorityBlockingQueue是无界阻塞队列，因此maximumPoolSize没有任何意义。
     * @param keepAliveTime   当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
     * @param unit            等待时长的单位
     * @param workQueue       线程池工作队列，用于在任务完成之前保留任务的队列执行
     * @param threadFactory   线程创建工厂
     * @param handler         拒绝执行的处理
     */
    private PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, PriorityBlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * 指定优先级执行Runnable
     *
     * @param command  命令
     * @param priority 优先级
     * @return IPriorityRunnable
     */
    public IPriorityRunnable execute(Runnable command, int priority) {
        if (command instanceof IPriorityRunnable) {
            this.execute(command);
            return (IPriorityRunnable) command;
        }
        IPriorityRunnable runnable = new DefaultPriorityRunnable(new Priority(priority), command);
        this.execute(runnable);
        return runnable;
    }

    /**
     * 指定优先级执行Runnable
     *
     * @param task     任务
     * @param priority 优先级
     * @return IPriorityFuture
     */
    public IPriorityFuture<?> submit(Runnable task, int priority) {
        if (task instanceof IPriority) {
            return (IPriorityFuture<?>) this.submit(task);
        }
        return (IPriorityFuture<?>) this.submit(new DefaultPriorityRunnable(new Priority(priority), task));
    }

    /**
     * 指定优先级执行Runnable
     *
     * @param task     任务
     * @param result   结果
     * @param priority 优先级
     * @return IPriorityFuture
     */
    public <T> IPriorityFuture<T> submit(Runnable task, T result, int priority) {
        if (task instanceof IPriority) {
            return (IPriorityFuture<T>) this.submit(task, result);
        }
        return (IPriorityFuture<T>) this.submit(new DefaultPriorityRunnable(new Priority(priority), task),
                result);
    }

    /**
     * 指定优先级执行Callable
     *
     * @param task     任务
     * @param priority 优先级
     * @return IPriorityFuture
     */
    public <T> IPriorityFuture<T> submit(Callable<T> task, int priority) {
        if (task instanceof IPriority) {
            return (IPriorityFuture<T>) this.submit(task);
        }
        return (IPriorityFuture<T>) this.submit(new DefaultPriorityCallable<>(new Priority(priority), task));
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new DefaultPriorityFuture<>(callable);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new DefaultPriorityFuture<>(runnable, value);
    }

    /**
     * 传递子类PriorityRunnable，传递Runnable而非PriorityRunnable的话，将不支持优先级调整
     * 如果要使用Runnable又需要支持优先级可用扩展方法{{@link #execute(Runnable, int)}}并使用其返回值进行优先级调整
     *
     * @param command 命令
     */
    @Override
    public void execute(Runnable command) {
        if (command instanceof IPriorityComparable) {
            super.execute(command);
            return;
        }
        if (command instanceof IPriority) {
            super.execute(new DefaultPriorityRunnable((IPriority) command, command));
            return;
        }
        super.execute(new DefaultPriorityRunnable(new Priority(), command));
    }

    /**
     * 传递子类PriorityRunnable，传递Runnable而非PriorityRunnable的话，将不支持优先级调整
     * 如果要使用Runnable又需要支持优先级可用扩展方法{{@link #submit(Runnable, int)}}并使用其返回值进行优先级调整
     *
     * @param task 任务
     * @return Future
     */
    @Override
    public Future<?> submit(Runnable task) {
        if (task instanceof IPriorityComparable) {
            return super.submit(task);
        }
        if (task instanceof IPriority) {
            return super.submit(new DefaultPriorityRunnable((IPriority) task, task));
        }
        return super.submit(new DefaultPriorityRunnable(new Priority(), task));
    }

    /**
     * 传递子类PriorityRunnable，传递Runnable而非PriorityRunnable的话，将不支持优先级调整
     * 如果要使用Runnable又需要支持优先级可用扩展方法{{@link #submit(Runnable, Object, int)}}并使用其返回值进行优先级调整
     *
     * @param task   任务
     * @param result 结果
     * @return Future
     */
    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        if (task instanceof IPriorityComparable) {
            return super.submit(task, result);
        }
        if (task instanceof IPriority) {
            return super.submit(new DefaultPriorityRunnable((IPriority) task, task), result);
        }
        return super.submit(new DefaultPriorityRunnable(new Priority(), task), result);
    }

    /**
     * 传递子类PriorityCallable，传递Callable而非PriorityCallable的话，将不支持优先级调整
     * 如果要使用Callable又需要支持优先级可用扩展方法{{@link #submit(Callable, int)}}并使用其返回值进行优先级调整
     *
     * @param task 任务
     * @return Future
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (task instanceof IPriorityComparable) {
            return super.submit(task);
        }
        if (task instanceof IPriority) {
            return super.submit(new DefaultPriorityCallable<>((IPriority) task, task));
        }
        return super.submit(new DefaultPriorityCallable<>(new Priority(), task));
    }

    //==============================构建者===================================//

    /**
     * 优先级线程池构建者
     */
    public static final class Builder {
        /**
         * 默认核心线程数【非IO操作】
         */
        private static final int DEFAULT_CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
        /**
         * 默认的等待时长
         */
        private static final long DEFAULT_KEEP_ALIVE_TIME = 30;
        /**
         * 默认线程工厂名
         */
        private static final String DEFAULT_FACTORY_NAME = "Priority";
        /**
         * 核心(执行)线程池的线程数
         */
        int corePoolSize;
        /**
         * 当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
         */
        long keepAliveTime;
        /**
         * 等待时长的单位
         */
        TimeUnit unit;
        /**
         * 线程池工作队列，用于在任务完成之前保留任务的队列执行
         */
        PriorityBlockingQueue<Runnable> workQueue;
        /**
         * 线程创建工厂
         */
        ThreadFactory threadFactory;
        /**
         * 拒绝执行的处理
         */
        RejectedExecutionHandler handler;

        /**
         * 构造方法
         */
        public Builder() {
            this(DEFAULT_CORE_POOL_SIZE);
        }

        /**
         * 构造方法
         *
         * @param corePoolSize 核心(执行)线程池的线程数
         */
        public Builder(int corePoolSize) {
            this(corePoolSize, DEFAULT_KEEP_ALIVE_TIME, TimeUnit.SECONDS);
        }

        /**
         * 构造方法
         *
         * @param corePoolSize  核心(执行)线程池的线程数
         * @param keepAliveTime 当线程数大于核心数时，多余空闲线程在终止前等待新任务的最长时间。
         * @param unit          等待时长的单位
         */
        public Builder(int corePoolSize, long keepAliveTime, TimeUnit unit) {
            this.corePoolSize = corePoolSize;
            this.keepAliveTime = keepAliveTime;
            this.unit = unit;
        }

        public Builder setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
            return this;
        }

        public Builder setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
            return this;
        }

        public Builder setUnit(TimeUnit unit) {
            this.unit = unit;
            return this;
        }

        public Builder setWorkQueue(PriorityBlockingQueue<Runnable> workQueue) {
            this.workQueue = workQueue;
            return this;
        }

        public Builder setThreadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        public Builder setHandler(RejectedExecutionHandler handler) {
            this.handler = handler;
            return this;
        }

        /**
         * 构建
         *
         * @return 优先级线程池
         */
        public PriorityThreadPoolExecutor build() {
            if (workQueue == null) {
                workQueue = new PriorityBlockingQueue<>();
            }
            if (threadFactory == null) {
                threadFactory = TaskThreadFactory.getFactory(DEFAULT_FACTORY_NAME);
            }
            if (handler == null) {
                handler = new TaskRecordPolicy();
            }
            return new PriorityThreadPoolExecutor(corePoolSize, corePoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }
    }

}