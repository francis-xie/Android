
package com.basic.job.core.step.impl;

import androidx.annotation.NonNull;

import com.basic.job.core.param.ITaskResult;
import com.basic.job.core.param.impl.TaskResult;
import com.basic.job.core.step.ITaskStep;
import com.basic.job.core.step.ITaskStepHandler;

/**
 * 自动通知任务执行结果处理者
 *

 * @since 2021/11/2 1:48 AM
 */
public class AutoNotifyTaskStepHandler implements ITaskStepHandler {
    @Override
    public void beforeTask(@NonNull ITaskStep step) {

    }

    @Override
    public void afterTask(@NonNull ITaskStep step) {
        step.notifyTaskSucceed(TaskResult.succeed());
    }

    @Override
    public void onTaskException(@NonNull ITaskStep step, Exception exception) {
        step.notifyTaskFailed(TaskResult.failed(ITaskResult.PROCESS_TASK_THROW_EXCEPTION, exception.getMessage()));
    }

    @Override
    public boolean accept(@NonNull ITaskStep step) {
        return true;
    }

    @Override
    public void handleTaskSucceed(@NonNull ITaskStep step) {

    }

    @Override
    public void handleTaskFailed(@NonNull ITaskStep step) {

    }

    @Override
    public void handleTaskCancelled(@NonNull ITaskStep step) {

    }
}
