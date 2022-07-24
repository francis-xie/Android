
package com.basic.util.rxjava.task;

import com.basic.util.rxjava.impl.IRxUITask;

/**
 * UI线程中操作的任务
 *

 * @since 2018/6/10 下午9:29
 */
public abstract class RxUITask<T> implements IRxUITask<T> {
    /**
     * UI执行任务的入参
     */
    private T InData;

    public RxUITask(T inData) {
        InData = inData;
    }

    public T getInData() {
        return InData;
    }

    public RxUITask setInData(T inData) {
        InData = inData;
        return this;
    }
}
