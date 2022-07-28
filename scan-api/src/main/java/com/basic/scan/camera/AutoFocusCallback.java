
package com.basic.scan.camera;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

import com.basic.scan.Scan;
import com.basic.scan.logs.QCLog;

/**
 * 自动聚焦的回掉
 *

 * @since 2019-05-18 17:23
 */
public final class AutoFocusCallback implements Camera.AutoFocusCallback {

    private static final String TAG = AutoFocusCallback.class.getSimpleName();

    public static final long AUTO_FOCUS_INTERVAL_MS = 1500L;

    private Handler autoFocusHandler;
    private int autoFocusMessage;

    public void setHandler(Handler autoFocusHandler, int autoFocusMessage) {
        this.autoFocusHandler = autoFocusHandler;
        this.autoFocusMessage = autoFocusMessage;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (autoFocusHandler != null) {
            Message message = autoFocusHandler.obtainMessage(autoFocusMessage, success);
            autoFocusHandler.sendMessageDelayed(message, Scan.getAutoFocusInterval());
            autoFocusHandler = null;
        } else {
            QCLog.dTag(TAG, "Got auto-focus callback, but no handler for it");
        }
    }

}
