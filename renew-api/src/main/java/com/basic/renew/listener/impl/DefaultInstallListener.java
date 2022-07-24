
package com.basic.renew.listener.impl;

import android.content.Context;

import androidx.annotation.NonNull;

import com.basic.renew._Update;
import com.basic.renew.entity.DownloadEntity;
import com.basic.renew.listener.OnInstallListener;
import com.basic.renew.utils.ApkInstallUtils;

import java.io.File;
import java.io.IOException;

import static com.basic.renew.entity.UpdateError.ERROR.INSTALL_FAILED;

/**
 * 默认的apk安装监听【自定义安装监听可继承该类，并重写相应的方法】
 *

 * @since 2018/7/1 下午11:58
 */
public class DefaultInstallListener implements OnInstallListener {

    @Override
    public boolean onInstallApk(@NonNull Context context, @NonNull File apkFile, @NonNull DownloadEntity downloadEntity) {
        if (checkApkFile(downloadEntity, apkFile)) {
            return installApkFile(context, apkFile);
        } else {
            _Update.onUpdateError(INSTALL_FAILED, "Apk file verify failed, please check whether the MD5 value you set is correct！");
            return false;
        }
    }

    /**
     * 检验apk文件的有效性（默认是使用MD5进行校验,可重写该方法）
     *
     * @param downloadEntity 下载信息实体
     * @param apkFile        apk文件
     * @return apk文件是否有效
     */
    protected boolean checkApkFile(DownloadEntity downloadEntity, @NonNull File apkFile) {
        return downloadEntity != null && downloadEntity.isApkFileValid(apkFile);
    }

    /**
     * 安装apk文件【此处可自定义apk的安装方法,可重写该方法】
     *
     * @param context 上下文
     * @param apkFile apk文件
     * @return 是否安装成功
     */
    protected boolean installApkFile(Context context, File apkFile) {
        try {
            return ApkInstallUtils.install(context, apkFile);
        } catch (IOException e) {
            _Update.onUpdateError(INSTALL_FAILED, "An error occurred while install apk:" + e.getMessage());
        }
        return false;
    }


    @Override
    public void onInstallApkSuccess() {

    }
}
