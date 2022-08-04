package com.basic.log.crash.ui;

import android.content.Context;
import android.widget.Toast;

import com.basic.log.crash.ICrashHandler;
import com.basic.log.crash.OnCrashListener;

/**
 * 简单提示toast的崩溃处理
 */
public class ToastCrashListener implements OnCrashListener {
    /**
     * 发生崩溃
     *
     * @param context
     * @param crashHandler
     * @param throwable
     */
    @Override
    public void onCrash(final Context context, final ICrashHandler crashHandler, Throwable throwable) {
        Toast.makeText(context, "程序无响应，正在恢复...", Toast.LENGTH_LONG).show();
        crashHandler.setIsHandledCrash(true);
    }
}
