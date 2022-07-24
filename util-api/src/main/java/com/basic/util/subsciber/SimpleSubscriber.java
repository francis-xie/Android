
package com.basic.util.subsciber;

import com.basic.util.exception.RxException;
import com.basic.util.logs.RxLog;

/**
 * 简单的订阅者
 *

 * @since 2018/6/10 下午9:27
 */
public abstract class SimpleSubscriber<T> extends BaseSubscriber<T> {
    /**
     * 出错
     *
     * @param e
     */
    @Override
    public void onError(RxException e) {
        RxLog.e(e);
    }
}
