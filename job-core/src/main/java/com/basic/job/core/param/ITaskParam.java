
package com.basic.job.core.param;

/**
 * 任务参数信息实现接口
 */
public interface ITaskParam extends IDataStore {

    /**
     * 增加任务路径
     *
     * @param path 任务路径
     */
    void addPath(String path);

    /**
     * 增加组任务路径
     *
     * @param path  任务路径
     * @param index 任务索引
     * @param total 总任务数
     */
    void addGroupPath(String path, int index, int total);

    /**
     * 获取当前任务执行的全路径
     *
     * @return 当前任务执行的全路径
     */
    String getPath();

    /**
     * 更新路径
     *
     * @param path 任务全路径
     */
    void updatePath(String path);

    /**
     * 获取数据存储仓库
     *
     * @return 数据存储仓库
     */
    IDataStore getDataStore();

    /**
     * 更新数据
     *
     * @param iDataStore 数据存储仓库
     */
    void updateData(IDataStore iDataStore);

    /**
     * 更新参数
     *
     * @param path       任务全路径
     * @param iDataStore 数据存储仓库
     */
    void updateParam(String path, IDataStore iDataStore);

    /**
     * 更新参数
     *
     * @param taskParam 任务参数
     */
    void updateParam(ITaskParam taskParam);

}
