
package com.basic.code.utils.task;

import android.app.Application;

import androidx.annotation.NonNull;

import com.mikepenz.iconics.Iconics;
import com.basic.job.api.step.SimpleTaskStep;
import com.basic.job.core.ThreadType;
import com.basic.face.FACE;
import com.basic.code.MyApp;
import com.basic.code.utils.SettingSPUtils;
import com.basic.code.utils.sdkinit.XBasicLibInit;
import com.basic.code.utils.sdkinit.UpdateInit;
import com.basic.code.widget.iconfont.FACEIconFont;

/**
 * 主要初始化任务，放在第一位执行
 */
public class MainInitTask extends SimpleTaskStep {

    private Application mApplication;

    /**
     * 构造方法
     *
     * @param application 应用上下文
     */
    public MainInitTask(Application application) {
        mApplication = application;
    }

    @Override
    public void doTask() throws Exception {
        // 初始化基础库
        XBasicLibInit.init(mApplication);
        // 初始化UI框架
        initUi();
        // Update版本更新
        UpdateInit.init(mApplication);
    }

    /**
     * 初始化UI框架
     */
    private void initUi() {
        FACE.debug(MyApp.isDebug());
        if (SettingSPUtils.getInstance().isUseCustomFont()) {
            //设置默认字体为华文行楷
            FACE.initFontStyle("fonts/hwxk.ttf");
        }
        //字体图标库
        Iconics.init(mApplication);
        //2022.07.25
        //这是自己定义的图标库
        //Iconics.registerFont(new FACEIconFont());
    }

    @Override
    public String getName() {
        return "MainInitTask";
    }

    @NonNull
    @Override
    public ThreadType getThreadType() {
        return ThreadType.SYNC;
    }
}
