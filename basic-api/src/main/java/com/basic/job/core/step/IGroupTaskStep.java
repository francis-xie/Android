
package com.basic.job.core.step;

import java.util.List;

/**
 * 组任务步骤
 */
public interface IGroupTaskStep {

    /**
     * 增加执行任务
     *
     * @param taskStep 执行任务
     * @return 任务链执行引擎
     */
    IGroupTaskStep addTask(ITaskStep taskStep);

    /**
     * 增加执行任务集合
     *
     * @param taskStepList 执行任务集合
     * @return 任务链执行引擎
     */
    IGroupTaskStep addTasks(List<ITaskStep> taskStepList);

    /**
     * 清理任务
     */
    void clearTask();
}
