
package com.basic.util.rxjava.task;

import com.basic.util.rxjava.impl.IRxIOTask;

/**
 * IO线程中操作的任务
 *

 * @since 2018/6/10 下午9:29
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
