
package com.basic.util.rxjava;


import io.reactivex.FlowableOnSubscribe;

/**
 * 在订阅时执行的回调
 *

 * @since 2018/6/10 下午9:28
 */
public abstract class RxTaskOnSubscribe<T> implements FlowableOnSubscribe<T> {
    /**
     * 在订阅时执行的任务
     */
    private T mTask;

    public RxTaskOnSubscribe(T task) {
        mTask = task;
    }

    public T getTask() {
        return mTask;
    }

    public RxTaskOnSubscribe setTask(T task) {
        mTask = task;
        return this;
    }

}
