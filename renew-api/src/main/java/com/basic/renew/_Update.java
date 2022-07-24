
package com.basic.renew;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.LruCache;

import androidx.annotation.NonNull;

import com.basic.renew.entity.DownloadEntity;
import com.basic.renew.entity.UpdateError;
import com.basic.renew.listener.OnInstallListener;
import com.basic.renew.listener.OnUpdateFailureListener;
import com.basic.renew.listener.impl.DefaultInstallListener;
import com.basic.renew.listener.impl.DefaultUpdateFailureListener;
import com.basic.renew.logs.UpdateLog;
import com.basic.renew.proxy.IUpdateChecker;
import com.basic.renew.proxy.IUpdateDownloader;
import com.basic.renew.proxy.IUpdateHttpService;
import com.basic.renew.proxy.IUpdateParser;
import com.basic.renew.proxy.IUpdatePrompter;
import com.basic.renew.proxy.impl.DefaultFileEncryptor;
import com.basic.renew.utils.ApkInstallUtils;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.basic.renew.entity.UpdateError.ERROR.INSTALL_FAILED;

/**
 * 内部版本更新参数的获取
 *

 * @since 2018/7/10 下午4:27
 */
public final class _Update {

    /**
     * 存储正在进行检查版本的状态，key为url，value为是否正在检查
     */
    private static final Map<String, Boolean> sCheckMap = new ConcurrentHashMap<>();
    /**
     * 存储是否正在显示版本更新，key为url，value为是否正在显示版本更新
     */
    private static final Map<String, Boolean> sPrompterMap = new ConcurrentHashMap<>();
    /**
     * Runnable等待队列
     */
    private static final Map<String, Runnable> sWaitRunnableMap = new ConcurrentHashMap<>();

    /**
     * 存储顶部图片资源
     */
    private static final LruCache<String, Drawable> sTopDrawableCache = new LruCache<>(4);

    private static final Handler sMainHandler = new Handler(Looper.getMainLooper());

    /**
     * 10秒的检查延迟
     */
    private static final long CHECK_TIMEOUT = 10 * 1000L;

    /**
     * 设置版本检查的状态【防止重复检查】
     *
     * @param url        请求地址
     * @param isChecking 是否正在检查
     */
    public static void setCheckUrlStatus(final String url, boolean isChecking) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        sCheckMap.put(url, isChecking);
        Runnable waitRunnable = sWaitRunnableMap.get(url);
        if (waitRunnable != null) {
            sMainHandler.removeCallbacks(waitRunnable);
            sWaitRunnableMap.remove(url);
        }
        if (isChecking) {
            Runnable newRunnable = new Runnable() {
                @Override
                public void run() {
                    // 处理超时情况
                    sWaitRunnableMap.remove(url);
                    sCheckMap.put(url, false);
                }
            };
            sMainHandler.postDelayed(newRunnable, CHECK_TIMEOUT);
            sWaitRunnableMap.put(url, newRunnable);
        }
    }

    /**
     * 获取版本检查的状态
     *
     * @param url 请求地址
     * @return 是否正在检查
     */
    public static boolean getCheckUrlStatus(String url) {
        Boolean checkStatus = sCheckMap.get(url);
        return checkStatus != null && checkStatus;
    }

    /**
     * 设置版本更新弹窗是否已经显示
     *
     * @param url    请求地址
     * @param isShow 是否已经显示
     */
    public static void setIsPrompterShow(String url, boolean isShow) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        sPrompterMap.put(url, isShow);
    }

    /**
     * 获取版本更新弹窗是否已经显示
     *
     * @param url 请求地址
     * @return 是否正在显示
     */
    public static boolean isPrompterShow(String url) {
        Boolean isShow = sPrompterMap.get(url);
        return isShow != null && isShow;
    }

    /**
     * 保存顶部背景图片
     *
     * @param drawable 图片
     * @return 图片标识
     */
    public static String saveTopDrawable(Drawable drawable) {
        String tag = UUID.randomUUID().toString();
        sTopDrawableCache.put(tag, drawable);
        return tag;
    }

    /**
     * 获取顶部背景图片
     *
     * @param drawableTag 图片标识
     * @return 顶部背景图片
     */
    public static Drawable getTopDrawable(String drawableTag) {
        if (TextUtils.isEmpty(drawableTag)) {
            return null;
        }
        return sTopDrawableCache.get(drawableTag);
    }

    //===========================属性设置===================================//

    public static Map<String, Object> getParams() {
        return Update.get().mParams;
    }

    public static IUpdateHttpService getIUpdateHttpService() {
        return Update.get().mUpdateHttpService;
    }

    public static IUpdateChecker getIUpdateChecker() {
        return Update.get().mUpdateChecker;
    }

    public static IUpdateParser getIUpdateParser() {
        return Update.get().mUpdateParser;
    }

    public static IUpdatePrompter getIUpdatePrompter() {
        return Update.get().mUpdatePrompter;
    }

    public static IUpdateDownloader getIUpdateDownLoader() {
        return Update.get().mUpdateDownloader;
    }

    public static boolean isGet() {
        return Update.get().mIsGet;
    }

    public static boolean isWifiOnly() {
        return Update.get().mIsWifiOnly;
    }

    public static boolean isAutoMode() {
        return Update.get().mIsAutoMode;
    }

    public static String getApkCacheDir() {
        return Update.get().mApkCacheDir;
    }

    //===========================文件加密===================================//

    /**
     * 加密文件
     *
     * @param file 需要加密的文件
     */
    public static String encryptFile(File file) {
        if (Update.get().mFileEncryptor == null) {
            Update.get().mFileEncryptor = new DefaultFileEncryptor();
        }
        return Update.get().mFileEncryptor.encryptFile(file);
    }

    /**
     * 验证文件是否有效（加密是否一致）
     *
     * @param encrypt 加密值，不能为空
     * @param file    需要校验的文件
     * @return 文件是否有效
     */
    public static boolean isFileValid(String encrypt, File file) {
        if (Update.get().mFileEncryptor == null) {
            Update.get().mFileEncryptor = new DefaultFileEncryptor();
        }
        return Update.get().mFileEncryptor.isFileValid(encrypt, file);
    }

    //===========================apk安装监听===================================//

    public static OnInstallListener getOnInstallListener() {
        return Update.get().mOnInstallListener;
    }

    /**
     * 开始安装apk文件
     *
     * @param context 传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile apk文件
     */
    public static void startInstallApk(@NonNull Context context, @NonNull File apkFile) {
        startInstallApk(context, apkFile, new DownloadEntity());
    }

    /**
     * 开始安装apk文件
     *
     * @param context        传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile        apk文件
     * @param downloadEntity 文件下载信息
     */
    public static void startInstallApk(@NonNull Context context, @NonNull File apkFile, @NonNull DownloadEntity downloadEntity) {
        UpdateLog.d("开始安装apk文件, 文件路径:" + apkFile.getAbsolutePath() + ", 下载信息:" + downloadEntity);
        if (onInstallApk(context, apkFile, downloadEntity)) {
            onApkInstallSuccess(); //静默安装的话，不会回调到这里
        } else {
            onUpdateError(INSTALL_FAILED);
        }
    }

    /**
     * 安装apk
     *
     * @param context        传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile        apk文件
     * @param downloadEntity 文件下载信息
     */
    private static boolean onInstallApk(Context context, File apkFile, DownloadEntity downloadEntity) {
        if (Update.get().mOnInstallListener == null) {
            Update.get().mOnInstallListener = new DefaultInstallListener();
        }
        return Update.get().mOnInstallListener.onInstallApk(context, apkFile, downloadEntity);
    }

    /**
     * apk安装完毕
     */
    private static void onApkInstallSuccess() {
        if (Update.get().mOnInstallListener == null) {
            Update.get().mOnInstallListener = new DefaultInstallListener();
        }
        Update.get().mOnInstallListener.onInstallApkSuccess();
    }

    //===========================更新出错===================================//

    public static OnUpdateFailureListener getOnUpdateFailureListener() {
        return Update.get().mOnUpdateFailureListener;
    }

    /**
     * 更新出现错误
     *
     * @param errorCode
     */
    public static void onUpdateError(int errorCode) {
        onUpdateError(new UpdateError(errorCode));
    }

    /**
     * 更新出现错误
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static void onUpdateError(int errorCode, String message) {
        onUpdateError(new UpdateError(errorCode, message));
    }

    /**
     * 更新出现错误
     *
     * @param updateError
     */
    public static void onUpdateError(@NonNull UpdateError updateError) {
        if (Update.get().mOnUpdateFailureListener == null) {
            Update.get().mOnUpdateFailureListener = new DefaultUpdateFailureListener();
        }
        Update.get().mOnUpdateFailureListener.onFailure(updateError);
    }

}
