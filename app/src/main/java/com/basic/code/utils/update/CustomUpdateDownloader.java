
package com.basic.code.utils.update;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.basic.renew.entity.UpdateEntity;
import com.basic.renew.proxy.impl.DefaultUpdateDownloader;
import com.basic.renew.service.OnFileDownloadListener;

/**
 * 重写DefaultUpdateDownloader，在取消下载时，弹出提示
 *

 * @since 2019-06-14 23:47
 */
public class CustomUpdateDownloader extends DefaultUpdateDownloader {

    private boolean mIsStartDownload;

    @Override
    public void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener) {
        super.startDownload(updateEntity, downloadListener);
        mIsStartDownload = true;

    }

    @Override
    public void cancelDownload() {
        super.cancelDownload();
        if (mIsStartDownload) {
            mIsStartDownload = false;
            UpdateTipDialog.show("");
        }
    }

}
