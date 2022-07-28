
package com.basic.job.api.step;

import androidx.annotation.NonNull;

import com.basic.job.core.ThreadType;
import com.basic.job.core.param.ITaskParam;
import com.basic.job.core.param.ITaskResult;
import com.basic.job.core.step.ITaskStep;
import com.basic.job.core.step.impl.AbstractGroupTaskStep;
import com.basic.job.thread.pool.cancel.ICancelable;
import com.basic.job.utils.TaskUtils;

/**
 * 串行任务组(不进行具体的任务）
 */
public class SerialGroupTaskStep extends AbstractGroupTaskStep {

    /**
     * 获取串行任务组
     *
     * @return 串行任务组
     */
    public static SerialGroupTaskStep get() {
        return new SerialGroupTaskStep();
    }

    /**
     * 获取串行任务组
     *
     * @param name 任务组名称
     * @return 串行任务组
     */
    public static SerialGroupTaskStep get(@NonNull String name) {
        return new SerialGroupTaskStep(name);
    }

    /**
     * 获取串行任务组
     *
     * @param threadType 线程类型
     * @return 串行任务组
     */
    public static SerialGroupTaskStep get(@NonNull ThreadType threadType) {
        return new SerialGroupTaskStep(threadType);
    }

    public SerialGroupTaskStep() {
        super();
    }

    public SerialGroupTaskStep(@NonNull String name) {
        super(name);
    }

    public SerialGroupTaskStep(@NonNull ThreadType threadType) {
        super(threadType);
    }

    public SerialGroupTaskStep(@NonNull String name, @NonNull ITaskParam taskParam) {
        super(name, taskParam);
    }

    public SerialGroupTaskStep(@NonNull String name, @NonNull ThreadType threadType) {
        super(name, threadType);
    }

    @Override
    public void doTask() throws Exception {
        ITaskStep firstTaskStep = TaskUtils.findNextTaskStep(getTasks(), null);
        if (firstTaskStep != null) {
            initGroupTask();
            firstTaskStep.prepareTask(getResult());
            ICancelable cancelable = TaskUtils.executeTask(firstTaskStep);
            firstTaskStep.setCancelable(cancelable);
        } else {
            notifyTaskSucceed(getResult());
        }
    }

    @Override
    public void onTaskStepCompleted(@NonNull ITaskStep step, @NonNull ITaskResult result) {
        getResult().saveResultNotPath(result);
        getResult().addGroupPath(step.getName(), mTaskIndex.getAndIncrement(), mTaskTotalSize);
        ITaskStep nextTaskStep = TaskUtils.findNextTaskStep(getTasks(), step);
        if (nextTaskStep != null) {
            // 更新数据，将上一个task的结果更新到下一个task
            nextTaskStep.prepareTask(getResult());
            ICancelable cancelable = TaskUtils.executeTask(nextTaskStep);
            nextTaskStep.setCancelable(cancelable);
        } else {
            notifyTaskSucceed(result);
        }
    }

    @Override
    public void onTaskStepError(@NonNull ITaskStep step, @NonNull ITaskResult result) {
        notifyTaskFailed(result);
    }

}
