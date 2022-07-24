
package com.basic.util.rxjava.impl;

/**
 * 在UI线程中操作的任务
 *

 * @since 2018/6/10 下午9:29
 */
public interface IRxUITask<T> {

    /**
     * 在UI线程中执行
     * @param t 任务执行的入参
     */
    void doInUIThread(T t);
}
