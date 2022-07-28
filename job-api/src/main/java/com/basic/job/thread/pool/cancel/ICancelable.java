
package com.basic.job.thread.pool.cancel;

/**
 * 可取消执行的实现接口
 */
public interface ICancelable {

    /**
     * 取消
     */
    void cancel();

    /**
     * 获取是否已取消
     *
     * @return 是否已取消
     */
    boolean isCancelled();
}
