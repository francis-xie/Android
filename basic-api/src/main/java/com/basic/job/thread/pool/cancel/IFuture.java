
package com.basic.job.thread.pool.cancel;

import java.util.concurrent.Future;

/**
 * 可直接取消任务的Future接口
 */
public interface IFuture<T> extends Future<T>, ICancelable {

}
