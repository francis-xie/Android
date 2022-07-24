
package com.basic.job.core.step;

import androidx.annotation.NonNull;

import com.basic.job.core.param.ITaskParam;
import com.basic.job.core.param.ITaskResult;

/**
 * 任务步骤执行控制器
 *

 * @since 1/30/22 6:33 PM
 */
public interface ITaskStepController {

    /**
     * 获取任务步骤名称
     *
     * @return 任务步骤的名称
     */
    String getName();

    /**
     * 获取任务的参数
     *
     * @return 任务参数
     */
    @NonNull
    ITaskParam getTaskParam();

    /**
     * 通知任务链任务步骤执行完毕
     *
     * @param result 任务执行结果
     */
    void notifyTaskSucceed(@NonNull ITaskResult result);

    /**
     * 通知任务链执行发生异常
     *
     * @param result 任务执行结果
     */
    void notifyTaskFailed(@NonNull ITaskResult result);

    /**
     * 资源释放
     */
    void recycle();
}
