
package com.basic.renew.widget;

import java.io.File;

/**
 * 下载事件处理者
 */
public interface IDownloadEventHandler {

    /**
     * 处理开始下载
     */
    void handleStart();

    /**
     * 处理下载中的进度更新
     *
     * @param progress 下载进度
     */
    void handleProgress(float progress);

    /**
     * 处理下载完毕
     *
     * @param file 下载文件
     * @return 下载完毕后是否打开文件进行安装<br>{@code true} ：安装<br>{@code false} ：不安装
     */
    boolean handleCompleted(File file);

    /**
     * 处理下载失败
     *
     * @param throwable 失败原因
     */
    void handleError(Throwable throwable);
}
