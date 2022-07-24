
package com.basic.job.core.param;

/**
 * 任务执行结果
 *

 * @since 2021/10/27 2:10 AM
 */
public interface ITaskResult {

    /**
     * 任务成功
     */
    int SUCCESS = 0;

    /**
     * 任务失败
     */
    int ERROR = -1;

    /**
     * 任务执行过程中发生异常
     */
    int PROCESS_TASK_THROW_EXCEPTION = -2;

    /**
     * 获取任务结果码
     *
     * @return 任务结果码
     */
    int getCode();

    /**
     * 获取任务执行信息
     *
     * @return 任务执行信息
     */
    String getMessage();

    /**
     * 设置结果信息
     *
     * @param code    任务结果码
     * @param message 任务执行信息
     */
    void setResult(int code, String message);

    /**
     * 保存执行结果
     *
     * @param taskResult 执行结果
     */
    void saveResult(ITaskResult taskResult);

    /**
     * 保存执行结果，但是不保存执行路径
     *
     * @param taskResult 执行结果
     */
    void saveResultNotPath(ITaskResult taskResult);

    /**
     * 更新参数
     *
     * @param taskParam 任务参数
     */
    void updateParam(ITaskParam taskParam);

    /**
     * 获取数据存储仓库
     *
     * @return 数据存储仓库
     */
    IDataStore getDataStore();

    /**
     * 获取当前任务执行的全路径
     *
     * @return 当前任务执行的全路径
     */
    String getPath();

    /**
     * 获取详细信息
     *
     * @return 详细信息
     */
    String getDetailMessage();

}
