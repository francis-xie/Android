
package com.basic.job.thread.priority.impl;

import com.basic.job.thread.priority.IPriority;
import com.basic.job.thread.priority.IPriorityRunnable;
import com.basic.job.thread.utils.PriorityUtils;

/**
 * 具有优先级排序的Runnable
 */
public class DefaultPriorityRunnable implements IPriorityRunnable {
    /**
     * 优先级
     */
    private IPriority mPriority;
    /**
     * 执行任务
     */
    private Runnable mRunnable;

    public DefaultPriorityRunnable(IPriority priority, Runnable runnable) {
        mPriority = priority;
        mRunnable = runnable;
        setId(PriorityUtils.generateId());
    }

    @Override
    public void priority(int priority) {
        if (mPriority != null) {
            mPriority.priority(priority);
        }
    }

    @Override
    public long getId() {
        return mPriority != null ? mPriority.getId() : 0;
    }

    @Override
    public void setId(long id) {
        if (mPriority != null) {
            mPriority.setId(id);
        }
    }

    @Override
    public int priority() {
        return mPriority != null ? mPriority.priority() : 0;
    }

    @Override
    public int compareTo(IPriority other) {
        return PriorityUtils.compare(this, other);
    }

    @Override
    public void run() {
        if (mRunnable != null) {
            mRunnable.run();
        }
    }

    @Override
    public String toString() {
        return "DefaultPriorityRunnable{" +
                ", mPriority=" + mPriority +
                ", mRunnable=" + mRunnable +
                '}';
    }
}
