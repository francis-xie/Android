
package com.basic.job.core;

import androidx.annotation.NonNull;

import com.basic.job.core.param.ITaskResult;

/**
 * 任务链执行回调
 *

 * @since 2021/10/18 10:32 PM
 */
public interface ITaskChainCallback {

    /**
     * 回调是否返回主线程
     *
     * @return 是否返回主线程
     */
    boolean isCallBackOnMainThread();

    /**
     * 任务步骤开始执行
     *
     * @param engine 任务链
     */
    void onTaskChainStart(@NonNull ITaskChainEngine engine);

    /**
     * 任务步骤执行完毕
     *
     * @param engine 任务链
     * @param result 任务执行结果
     */
    void onTaskChainCompleted(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result);

    /**
     * 任务步骤执行发生异常
     *
     * @param engine 任务链
     * @param result 任务执行结果
     */
    void onTaskChainError(@NonNull ITaskChainEngine engine, @NonNull ITaskResult result);

    /**
     * 任务步骤执行被取消
     *
     * @param engine 任务链
     */
    void onTaskChainCancelled(@NonNull ITaskChainEngine engine);

}
