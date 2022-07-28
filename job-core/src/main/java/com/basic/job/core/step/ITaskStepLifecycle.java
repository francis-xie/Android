
package com.basic.job.core.step;

import androidx.annotation.NonNull;

import com.basic.job.core.param.ITaskResult;

/**
 * 任务步骤的生命周期管理
 */
public interface ITaskStepLifecycle {

    /**
     * 任务步骤执行完毕
     *
     * @param step   任务步骤
     * @param result 任务执行结果
     */
    void onTaskStepCompleted(@NonNull ITaskStep step, @NonNull ITaskResult result);

    /**
     * 任务步骤执行发生异常
     *
     * @param step   任务步骤
     * @param result 任务执行结果
     */
    void onTaskStepError(@NonNull ITaskStep step, @NonNull ITaskResult result);
}
