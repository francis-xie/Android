
package com.basic.job.thread.priority;

import com.basic.job.thread.pool.cancel.IFuture;

/**
 * 具有优先级排序的Future接口
 *

 * @since 2021/10/9 2:31 AM
 */
public interface IPriorityFuture<V> extends IPriorityComparable<IPriority>, IFuture<V> {

}
