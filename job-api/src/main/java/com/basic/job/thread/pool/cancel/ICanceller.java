
package com.basic.job.thread.pool.cancel;

/**
 * 取消者
 *

 * @since 2/7/22 12:39 PM
 */
public interface ICanceller extends ICancelable {

    /**
     * 获取取消的标识
     *
     * @return 取消的标识
     */
    String getName();
}
