package com.basic.code;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.basic.code.utils.sdkinit.ANRWatchDogInit;
import com.basic.code.utils.sdkinit.UMengInit;
import com.basic.code.utils.sdkinit.BasicLibInit;
import com.basic.code.utils.sdkinit.UpdateInit;

public class MyApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //解决4.x运行崩溃的问题
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLibs();
    }

    /**
     * 初始化基础库
     */
    private void initLibs() {
        // X系列基础库初始化
        BasicLibInit.init(this);
        // 版本更新初始化
        UpdateInit.init(this);
        // 运营统计数据
        UMengInit.init(this);
        // ANR监控
        ANRWatchDogInit.init();
    }


    /**
     * @return 当前app是否是调试开发模式
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }


}
