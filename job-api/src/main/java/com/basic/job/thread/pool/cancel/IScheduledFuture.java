
package com.basic.job.thread.pool.cancel;

import java.util.concurrent.RunnableScheduledFuture;

/**
 * 可直接取消任务的RunnableScheduledFuture接口
 */
public interface IScheduledFuture<T> extends RunnableScheduledFuture<T>, ICancelable {

}
