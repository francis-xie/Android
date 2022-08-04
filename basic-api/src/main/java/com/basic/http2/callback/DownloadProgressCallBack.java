package com.basic.http2.callback;

/**
 * 下载进度回调（主线程，可以直接操作UI）
 */
public abstract class DownloadProgressCallBack<T> extends CallBack<T> {

    public DownloadProgressCallBack() {
    }

    @Override
    public void onSuccess(T response) {

    }

    /**
     * 更新进度条
     * @param downLoadSize 已经下载的大小
     * @param totalSize 下载文件的总大小
     * @param done
     */
    public abstract void update(long downLoadSize, long totalSize, boolean done);

    public abstract void onComplete(String path);

    @Override
    public void onCompleted() {

    }
}
