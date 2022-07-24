
package com.basic.job.thread.pool.cancel;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 可以取消的供Handler使用的Runnable
 *

 * @since 3/21/22 1:34 AM
 */
public class CancelHandlerRunnable implements Runnable, ICancelable {

    /**
     * 获取可以取消的Runnable
     *
     * @param handler  handler
     * @param runnable 执行任务
     * @return 可以取消的Runnable
     */
    public static CancelHandlerRunnable get(@NonNull Handler handler, @NonNull Runnable runnable) {
        return new CancelHandlerRunnable(handler, runnable);
    }

    private WeakReference<Handler> mHandler;

    private Runnable mRunnable;

    private AtomicBoolean mIsCancelled = new AtomicBoolean(false);

    /**
     * 构造方法
     *
     * @param handler  handler
     * @param runnable 执行任务
     */
    private CancelHandlerRunnable(@NonNull Handler handler, @NonNull Runnable runnable) {
        mHandler = new WeakReference<>(handler);
        mRunnable = runnable;
    }

    /**
     * 开始延期执行
     *
     * @param delayMillis 延期时间
     * @return 取消接口
     */
    public ICancelable startDelayed(long delayMillis) {
        final Handler handler = mHandler.get();
        if (handler != null) {
            handler.postDelayed(this, delayMillis);
        }
        return this;
    }

    @Override
    public void cancel() {
        if (isCancelled()) {
            return;
        }
        final Handler handler = mHandler.get();
        if (handler == null) {
            return;
        }
        handler.removeCallbacks(this);
        mIsCancelled.set(true);
    }

    @Override
    public boolean isCancelled() {
        return mIsCancelled.get();
    }

    @Override
    public void run() {
        if (mRunnable != null) {
            mRunnable.run();
        }
    }
}
