
package com.basic.job.core;

/**
 * 线程执行类型
 */
public enum ThreadType {

    /**
     * 主线程（UI线程）
     */
    MAIN,

    /**
     * 异步线程（开子线程，普通线程池）
     */
    ASYNC,

    /**
     * 异步线程（开子线程，io线程池）
     */
    ASYNC_IO,

    /**
     * 异步线程（开子线程，紧急线程池）
     */
    ASYNC_EMERGENT,

    /**
     * 异步线程（开子线程，优先级较低线程池）
     */
    ASYNC_BACKGROUND,

    /**
     * 同步线程（直接执行）
     */
    SYNC

}
