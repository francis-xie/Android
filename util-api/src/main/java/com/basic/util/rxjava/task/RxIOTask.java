
package com.basic.util.rxjava.task;

import com.basic.util.rxjava.impl.IRxIOTask;

/**
 * IO线程中操作的任务
 */
public abstract class RxIOTask<T> implements IRxIOTask<T, Void> {
    /**
     * IO执行任务的入参
     */
    private T InData;

    public RxIOTask(T inData) {
        InData = inData;
    }

    public T getInData() {
        return InData;
    }

    public RxIOTask setInData(T inData) {
        InData = inData;
        return this;
    }

}
