
package com.basic.job.core.step.impl;

import androidx.annotation.NonNull;

import com.basic.job.core.param.ITaskParam;
import com.basic.job.core.param.ITaskResult;
import com.basic.job.core.param.impl.TaskParam;
import com.basic.job.core.param.impl.TaskResult;
import com.basic.job.core.step.ITaskStepController;

/**
 * 任务命令，执行简单任务
 */
public abstract class TaskCommand implements ITaskStepController {

    /**
     * 任务步骤执行控制器
     */
    private ITaskStepController mController;

    /**
     * 设置任务步骤执行控制器
     *
     * @param controller 控制器
     * @return this
     */
    public TaskCommand setTaskStepResultController(ITaskStepController controller) {
        mController = controller;
        return this;
    }

    /**
     * 通知任务链任务步骤执行完毕
     */
    public void notifyTaskSucceed() {
        notifyTaskSucceed(TaskResult.succeed());
    }

    /**
     * 通知任务链任务步骤执行失败
     */
    public void notifyTaskFailed() {
        notifyTaskFailed(TaskResult.failed());
    }

    /**
     * 通知任务链任务步骤执行失败
     *
     * @param code 失败的错误码
     */
    public void notifyTaskFailed(int code) {
        notifyTaskFailed(TaskResult.failed(code));
    }

    /**
     * 通知任务链任务步骤执行失败
     *
     * @param code    失败的错误码
     * @param message 错误信息
     */
    public void notifyTaskFailed(int code, String message) {
        notifyTaskFailed(TaskResult.failed(code, message));
    }

    @Override
    public void notifyTaskSucceed(@NonNull ITaskResult result) {
        if (mController != null) {
            mController.notifyTaskSucceed(result);
        }
    }

    @Override
    public void notifyTaskFailed(@NonNull ITaskResult result) {
        if (mController != null) {
            mController.notifyTaskFailed(result);
        }
    }

    @Override
    public String getName() {
        return mController != null ? mController.getName() : "unknown";
    }

    @NonNull
    @Override
    public ITaskParam getTaskParam() {
        return mController != null ? mController.getTaskParam() : TaskParam.get();
    }

    /**
     * 执行任务
     *
     * @throws Exception 异常
     */
    public abstract void run() throws Exception;

    @Override
    public void recycle() {
        if (mController != null) {
            mController.recycle();
        }
    }
}
