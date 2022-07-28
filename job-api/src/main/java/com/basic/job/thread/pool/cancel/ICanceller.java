
package com.basic.job.thread.pool.cancel;

/**
 * 取消者
 */
public interface ICanceller extends ICancelable {

    /**
     * 获取取消的标识
     *
     * @return 取消的标识
     */
    String getName();
}
