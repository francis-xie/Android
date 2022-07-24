
package com.basic.util.rxjava.impl;

/**
 * 在IO线程中执行的任务
 *

 * @since 2018/6/10 下午9:29
 */
public interface IRxIOTask<T, R> {

    /**
     * 在IO线程中执行
     * @param t 任务执行的入参
     * @return  R 任务执行的出参
     */
    R doInIOThread(T t);
}
