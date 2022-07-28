package com.basic.code.utils.sdkinit;

import android.app.Application;
import android.content.Context;

import com.basic.code.MyApp;
import com.basic.code.utils.update.CustomUpdateDownloader;
import com.basic.code.utils.update.CustomUpdateFailureListener;
import com.basic.code.utils.update.HttpUpdateHttpServiceImpl;
import com.basic.renew.Update;
import com.basic.renew.utils.UpdateUtils;
import com.basic.tools.common.StringUtils;

/**
 * Update 版本更新 SDK 初始化
 */
public final class UpdateInit {

    private UpdateInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 应用版本更新的检查地址
     */
    // TODO: 2021/5/26 需要开启版本更新功能的话，就需要配置一下版本更新的地址
    private static final String KEY_UPDATE_URL = "";

    public static void init(Application application) {
        Update.get()
                .debug(MyApp.isDebug())
                //默认设置只在wifi下检查版本更新
                .isWifiOnly(false)
                //默认设置使用get请求检查版本
                .isGet(true)
                //默认设置非自动模式，可根据具体使用配置
                .isAutoMode(false)
                //设置默认公共请求参数
                .param("versionCode", UpdateUtils.getVersionCode(application))
                .param("appKey", application.getPackageName())
                //这个必须设置！实现网络请求功能。
                .setIUpdateHttpService(new HttpUpdateHttpServiceImpl())
                .setIUpdateDownLoader(new CustomUpdateDownloader())
                //这个必须初始化
                .init(application);
    }

    /**
     * 进行版本更新检查
     */
    public static void checkUpdate(Context context, boolean needErrorTip) {
        checkUpdate(context, KEY_UPDATE_URL, needErrorTip);
    }

    /**
     * 进行版本更新检查
     *
     * @param context      上下文
     * @param url          版本更新检查的地址
     * @param needErrorTip 是否需要错误的提示
     */
    private static void checkUpdate(Context context, String url, boolean needErrorTip) {
        if (StringUtils.isEmpty(url)) {
            return;
        }
        Update.newBuild(context).updateUrl(url).update();
        Update.get().setOnUpdateFailureListener(new CustomUpdateFailureListener(needErrorTip));
    }
}