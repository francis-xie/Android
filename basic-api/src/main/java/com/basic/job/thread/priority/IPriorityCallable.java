
package com.basic.job.thread.priority;

import java.util.concurrent.Callable;

/**
 * 具有优先级排序的Callable接口
 */
public interface IPriorityCallable<V> extends IPriorityComparable<IPriority>, Callable<V> {

}
