
package com.basic.job.core.step;

import androidx.annotation.NonNull;

/**
 * 任务处理者
 */
public interface ITaskStepHandler {

    /**
     * 任务执行之前的处理
     *
     * @param step 任务
     */
    void beforeTask(@NonNull ITaskStep step);

    /**
     * 任务执行完毕的处理
     *
     * @param step 任务
     */
    void afterTask(@NonNull ITaskStep step);

    /**
     * 任务执行发生异常
     *
     * @param step      任务
     * @param exception 异常
     */
    void onTaskException(@NonNull ITaskStep step, Exception exception);

    /**
     * 是否接收执行任务
     *
     * @param step 任务
     * @return true：执行；false：不执行
     */
    boolean accept(@NonNull ITaskStep step);

    /**
     * 任务执行完成的处理
     *
     * @param step 任务
     */
    void handleTaskSucceed(@NonNull ITaskStep step);

    /**
     * 任务执行失败的处理
     *
     * @param step 任务
     */
    void handleTaskFailed(@NonNull ITaskStep step);

    /**
     * 任务执行被取消的处理
     *
     * @param step 任务
     */
    void handleTaskCancelled(@NonNull ITaskStep step);

}
