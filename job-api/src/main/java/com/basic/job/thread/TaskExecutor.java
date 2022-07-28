
package com.basic.job.thread;

import androidx.annotation.NonNull;

import com.basic.job.thread.executor.ICategoryExecutorCore;
import com.basic.job.thread.executor.IPriorityExecutorCore;
import com.basic.job.thread.executor.IScheduledExecutorCore;
import com.basic.job.thread.executor.impl.CategoryExecutorCore;
import com.basic.job.thread.executor.impl.PriorityExecutorCore;
import com.basic.job.thread.executor.impl.ScheduledExecutorCore;
import com.basic.job.thread.pool.cancel.ICancelable;

import java.util.concurrent.TimeUnit;

/**
 * Task的执行者
 */
public class TaskExecutor implements IPriorityExecutorCore, ICategoryExecutorCore, IScheduledExecutorCore {

    private static volatile TaskExecutor sInstance = null;

    /**
     * 优先级执行内核实现接口
     */
    private IPriorityExecutorCore mPriorityExecutorCore;
    /**
     * 分类执行内核实现接口
     */
    private ICategoryExecutorCore mCategoryExecutorCore;
    /**
     * 分类执行内核实现接口
     */
    private IScheduledExecutorCore mScheduledExecutorCore;

    /**
     * 获取Task的执行者
     *
     * @return Task的执行者
     */
    public static TaskExecutor get() {
        if (sInstance == null) {
            synchronized (TaskExecutor.class) {
                if (sInstance == null) {
                    sInstance = new TaskExecutor();
                }
            }
        }
        return sInstance;
    }

    /**
     * 私有构造方法
     */
    private TaskExecutor() {
        mCategoryExecutorCore = new CategoryExecutorCore();
        mPriorityExecutorCore = new PriorityExecutorCore();
        mScheduledExecutorCore = new ScheduledExecutorCore();
    }

    /**
     * 设置优先级控制的执行内核实现接口
     *
     * @param priorityExecutorCore 优先级控制的执行内核实现接口
     * @return this
     */
    public TaskExecutor setPriorityExecutorCore(@NonNull IPriorityExecutorCore priorityExecutorCore) {
        mPriorityExecutorCore = priorityExecutorCore;
        return this;
    }

    /**
     * 设置类别执行内核实现接口
     *
     * @param categoryExecutorCore 类别执行内核实现接口
     * @return this
     */
    public TaskExecutor setCategoryExecutorCore(@NonNull ICategoryExecutorCore categoryExecutorCore) {
        mCategoryExecutorCore = categoryExecutorCore;
        return this;
    }

    /**
     * 设置周期执行内核的实现接口
     *
     * @param scheduledExecutorCore 周期执行内核的实现接口
     * @return this
     */
    public TaskExecutor setScheduledExecutorCore(@NonNull IScheduledExecutorCore scheduledExecutorCore) {
        mScheduledExecutorCore = scheduledExecutorCore;
        return this;
    }

    @Override
    public void shutdown() {
        mCategoryExecutorCore.shutdown();
        mPriorityExecutorCore.shutdown();
        mScheduledExecutorCore.shutdown();
    }

    //================PriorityExecutorCore==================//

    @Override
    public ICancelable submit(Runnable task, int priority) {
        return mPriorityExecutorCore.submit(task, priority);
    }

    @Override
    public ICancelable submit(String groupName, Runnable task, int priority) {
        return mPriorityExecutorCore.submit(groupName, task, priority);
    }

    //================CategoryExecutorCore==================//

    @Override
    public boolean postToMain(Runnable task) {
        return mCategoryExecutorCore.postToMain(task);
    }

    @Override
    public ICancelable postToMainDelay(Runnable task, long delayMillis) {
        return mCategoryExecutorCore.postToMainDelay(task, delayMillis);
    }

    @Override
    public ICancelable emergentSubmit(Runnable task) {
        return mCategoryExecutorCore.emergentSubmit(task);
    }

    @Override
    public ICancelable submit(Runnable task) {
        return mCategoryExecutorCore.submit(task);
    }

    @Override
    public ICancelable backgroundSubmit(Runnable task) {
        return mCategoryExecutorCore.backgroundSubmit(task);
    }

    @Override
    public ICancelable ioSubmit(Runnable task) {
        return mCategoryExecutorCore.ioSubmit(task);
    }

    @Override
    public ICancelable groupSubmit(String groupName, Runnable task) {
        return mCategoryExecutorCore.groupSubmit(groupName, task);
    }

    //================ScheduledExecutorCore==================//

    @Override
    public ICancelable schedule(Runnable task, long delay, TimeUnit unit) {
        return mScheduledExecutorCore.schedule(task, delay, unit);
    }

    @Override
    public ICancelable scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return mScheduledExecutorCore.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    @Override
    public ICancelable scheduleWithFixedDelay(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return mScheduledExecutorCore.scheduleWithFixedDelay(task, initialDelay, period, unit);
    }
}
