
package com.basic.renew.widget;

import androidx.annotation.NonNull;

import com.basic.renew.service.OnFileDownloadListener;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 弱引用文件下载监听, 解决内存泄漏问题
 *

 * @since 2020/11/15 10:58 PM
 */
public class WeakFileDownloadListener implements OnFileDownloadListener {

    private WeakReference<IDownloadEventHandler> mDownloadHandlerRef;

    public WeakFileDownloadListener(@NonNull IDownloadEventHandler handler) {
        mDownloadHandlerRef = new WeakReference<>(handler);
    }

    @Override
    public void onStart() {
        if (getEventHandler() != null) {
            getEventHandler().handleStart();
        }
    }

    @Override
    public void onProgress(float progress, long total) {
        if (getEventHandler() != null) {
            getEventHandler().handleProgress(progress);
        }
    }

    @Override
    public boolean onCompleted(File file) {
        if (getEventHandler() != null) {
            return getEventHandler().handleCompleted(file);
        } else {
            // 下载好了，返回true，自动进行apk安装
            return true;
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (getEventHandler() != null) {
            getEventHandler().handleError(throwable);
        }
    }

    private IDownloadEventHandler getEventHandler() {
        return mDownloadHandlerRef != null ? mDownloadHandlerRef.get() : null;
    }
}
