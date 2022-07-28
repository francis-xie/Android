
package com.basic.scan.decoding;

import android.app.Activity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 定时关闭界面（默认5分钟）
 *

 * @since 2019/1/17 上午12:06
 */
public final class InactivityTimer {

    private static final int INACTIVITY_DELAY_SECONDS = 5 * 60;

    private final ScheduledExecutorService inactivityTimer = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
    private final Activity mActivity;
    private ScheduledFuture<?> mInactivityFuture = null;

    public InactivityTimer(Activity activity) {
        mActivity = activity;
        onActivity();
    }

    public void onActivity() {
        cancel();
        mInactivityFuture = inactivityTimer.schedule(new FinishListener(mActivity),
                INACTIVITY_DELAY_SECONDS,
                TimeUnit.SECONDS);
    }

    private void cancel() {
        if (mInactivityFuture != null) {
            mInactivityFuture.cancel(true);
            mInactivityFuture = null;
        }
    }

    public void shutdown() {
        cancel();
        inactivityTimer.shutdown();
    }

    private static final class DaemonThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    }

}
