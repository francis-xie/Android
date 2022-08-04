package com.basic.log.crash;

import android.content.Context;

/**
 * <pre>
 *     desc   : 崩溃监听
 * </pre>
 */
public interface OnCrashListener {


    /**
     * 发生崩溃
     * @param context
     * @param crashHandler
     * @param throwable
     */
    void onCrash(Context context, ICrashHandler crashHandler, Throwable throwable);

}
