
package com.basic.renew.proxy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.basic.renew.entity.UpdateEntity;
import com.basic.renew.service.OnFileDownloadListener;

/**
 * 版本更新下载器
 *

 * @since 2018/6/29 下午8:31
 */
public interface IUpdateDownloader {

    /**
     * 开始下载更新
     *
     * @param updateEntity     更新信息
     * @param downloadListener 文件下载监听
     */
    void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener);

    /**
     * 取消下载
     */
    void cancelDownload();

    /**
     * 后台下载更新
     */
    void backgroundDownload();
}
