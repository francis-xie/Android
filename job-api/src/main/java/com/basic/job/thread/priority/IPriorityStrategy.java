
package com.basic.job.thread.priority;

/**
 * 优先级策略实现接口
 */
public interface IPriorityStrategy {

    /**
     * 比较两个优先级
     *
     * @param priority 优先级
     * @param other    比较的优先级
     * @return 比较结果，如果priority比other的优先级高，那么结果<0;
     */
    int compare(IPriority priority, IPriority other);
}
