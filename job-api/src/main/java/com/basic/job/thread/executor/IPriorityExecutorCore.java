
package com.basic.job.thread.executor;

import com.basic.job.thread.pool.cancel.ICancelable;

/**
 * 拥有优先级控制的执行者内核实现接口
 *

 * @since 2021/11/10 1:04 AM
 */
public interface IPriorityExecutorCore extends IExecutorCore {

    /**
     * 执行异步任务
     *
     * @param task     任务
     * @param priority 优先级
     * @return 取消接口
     */
    ICancelable submit(Runnable task, int priority);

    /**
     * 按组执行异步任务
     *
     * @param groupName 任务组名
     * @param task      任务
     * @param priority  优先级
     * @return 取消接口
     */
    ICancelable submit(String groupName, Runnable task, int priority);

}
