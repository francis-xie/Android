
package com.basic.job.thread.priority.impl;

import com.basic.job.thread.priority.IPriority;

/**
 * 优先级【先比较优先级，再比较序号】
 */
public class Priority implements IPriority {

    /**
     * 优先级
     */
    private int mPriority;

    /**
     * 序号
     */
    private long mId;

    public Priority() {

    }

    public Priority(int priority) {
        mPriority = priority;
    }

    @Override
    public int priority() {
        return mPriority;
    }

    @Override
    public void priority(int priority) {
        mPriority = priority;
    }

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public void setId(long id) {
        mId = id;
    }

    @Override
    public String toString() {
        return "Priority{" +
                "mPriority=" + mPriority +
                ", mId=" + mId +
                '}';
    }
}
