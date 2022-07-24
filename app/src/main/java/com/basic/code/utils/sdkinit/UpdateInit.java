
package com.basic.code.utils.sdkinit;

import android.app.Application;

import com.basic.code.MyApp;
import com.basic.code.utils.update.CustomUpdateDownloader;
import com.basic.code.utils.update.OkHttpUpdateHttpServiceImpl;
import com.basic.renew.Update;
import com.basic.renew.utils.UpdateUtils;

/**
 * Update 版本更新 SDK 初始化

 * @since 2019-06-18 15:51
 */
public final class UpdateInit {

    private UpdateInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

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
                .setIUpdateHttpService(new OkHttpUpdateHttpServiceImpl())
                .setIUpdateDownLoader(new CustomUpdateDownloader())
                //这个必须初始化
                .init(application);
    }
}
