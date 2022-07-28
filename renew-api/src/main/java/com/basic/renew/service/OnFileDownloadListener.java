
package com.basic.renew.service;

import java.io.File;

/**
 * 下载服务下载监听
 */
public interface OnFileDownloadListener {

    /**
     * 下载之前
     */
    void onStart();

    /**
     * 更新进度
     *
     * @param progress 进度0.00 - 0.50  - 1.00
     * @param total    文件总大小 单位字节
     */
    void onProgress(float progress, long total);

    /**
     * 下载完毕
     *
     * @param file 下载好的文件
     * @return 下载完毕后是否打开文件进行安装<br>{@code true} ：安装<br>{@code false} ：不安装
     */
    boolean onCompleted(File file);

    /**
     * 错误回调
     *
     * @param throwable 错误提示
     */
    void onError(Throwable throwable);


}
