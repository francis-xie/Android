
package com.basic.job.thread.priority;

import java.util.concurrent.Callable;

/**
 * 具有优先级排序的Callable接口
 *

 * @since 2021/10/9 2:36 AM
 */
public interface IPriorityCallable<V> extends IPriorityComparable<IPriority>, Callable<V> {

}
