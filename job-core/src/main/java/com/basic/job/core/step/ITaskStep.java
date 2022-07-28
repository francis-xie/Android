
package com.basic.job.core.step;

import androidx.annotation.NonNull;

import com.basic.job.core.ThreadType;
import com.basic.job.core.param.ITaskParam;
import com.basic.job.core.param.impl.TaskParam;
import com.basic.job.thread.pool.cancel.ICancelable;

/**
 * 任务步骤
 */
public interface ITaskStep extends Runnable, ICancelable, ITaskStepController {

    /**
     * 设置任务步骤的生命周期
     *
     * @param taskStepLifecycle 任务步骤的生命周期
     * @return 任务步骤
     */
    ITaskStep setTaskStepLifecycle(@NonNull ITaskStepLifecycle taskStepLifecycle);

    /**
     * 设置任务处理者
     *
     * @param taskStepHandler 任务处理者
     * @return 任务步骤
     */
    ITaskStep setTaskStepHandler(@NonNull ITaskStepHandler taskStepHandler);

    /**
     * 设置执行的线程类型
     *
     * @param threadType 线程类型
     * @return 任务步骤
     */
    ITaskStep setThreadType(@NonNull ThreadType threadType);

    /**
     * 设置执行的任务参数
     *
     * @param taskParam 任务参数
     * @return 任务步骤
     */
    ITaskStep setTaskParam(@NonNull ITaskParam taskParam);

    /**
     * 获取执行的线程类型
     *
     * @return 线程类型
     */
    @NonNull
    ThreadType getThreadType();

    /**
     * 是否接收执行
     *
     * @return 是否执行，默认是true
     */
    boolean accept();

    /**
     * 任务准备工作
     *
     * @param taskParam 任务参数
     */
    void prepareTask(TaskParam taskParam);

    /**
     * 设置任务取消接口
     *
     * @param cancelable 取消接口
     */
    void setCancelable(ICancelable cancelable);

    /**
     * 执行任务
     *
     * @throws Exception 异常
     */
    void doTask() throws Exception;

    /**
     * 设置是否正在运行
     *
     * @param isRunning 是否正在运行
     */
    void setIsRunning(boolean isRunning);

    /**
     * 是否正在运行
     *
     * @return 是否正在运行
     */
    boolean isRunning();

    /**
     * 是否正在等待
     *
     * @return 是否正在等待
     */
    boolean isPending();

}
