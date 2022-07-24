
package com.basic.router.thread;

import java.util.concurrent.CountDownLatch;

/**
 * 可取消的计数线程锁【多线程并发锁】
 *

 * @since 2018/5/18 上午12:18
 */
public class CancelableCountDownLatch extends CountDownLatch {
    /**
     * Constructs a {@code CountDownLatch} initialized with the given count.
     *
     * @param count the number of times {@link #countDown} must be invoked
     *              before threads can pass through {@link #await}
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public CancelableCountDownLatch(int count) {
        super(count);
    }

    /**
     * 取消计数锁
     */
    public void cancel() {
        while (getCount() > 0) {
            countDown();
        }
    }
}
