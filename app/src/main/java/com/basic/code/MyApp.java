package com.basic.code;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.basic.dbms.annotation.DataBase;
import com.basic.dbms.enums.DataBaseType;
import com.basic.job.Task;
import com.basic.code.utils.task.AsyncInitTask;
import com.basic.code.utils.task.MainInitTask;


/**
 * 应用初始化
 *

 * @since 2018/11/7 下午1:12
 */
@DataBase(name = "FACE", type = DataBaseType.INTERNAL)
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
        Task.debug(MyApp.isDebug());
        Task.getTaskChain()
                .addTask(new MainInitTask(this))
                .addTask(new AsyncInitTask(this))
                .start();
    }

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

}
