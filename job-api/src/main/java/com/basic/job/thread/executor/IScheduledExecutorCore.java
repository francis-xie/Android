
package com.basic.job.thread.executor;

import com.basic.job.thread.pool.cancel.ICancelable;

import java.util.concurrent.TimeUnit;

/**
 * 拥有周期执行能力的内核实现接口
 */
public interface IScheduledExecutorCore extends IExecutorCore {

    /**
     * 执行延期任务
     *
     * @param task  任务
     * @param delay 延迟时长
     * @param unit  时间单位
     * @return 取消接口
     */
    ICancelable schedule(Runnable task, long delay, TimeUnit unit);

    /**
     * 执行周期任务（以固定频率执行的任务）
     *
     * @param task         任务
     * @param initialDelay 初始延迟时长
     * @param period       间隔时长
     * @param unit         时间单位
     * @return 取消接口
     */
    ICancelable scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit);

    /**
     * 执行周期任务（以固定延时执行的任务，延时是相对当前任务结束为起点计算开始时间）
     *
     * @param task         任务
     * @param initialDelay 初始延迟时长
     * @param period       间隔时长
     * @param unit         时间单位
     * @return 取消接口
     */
    ICancelable scheduleWithFixedDelay(Runnable task, long initialDelay, long period, TimeUnit unit);

}
