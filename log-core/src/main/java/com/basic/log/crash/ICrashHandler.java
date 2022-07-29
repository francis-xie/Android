package com.basic.log.crash;

import java.io.File;

/**
 * 崩溃处理者
 */
public interface ICrashHandler {

    /**
     * 是否处理完成崩溃 【设置了这个之后，将退出崩溃处理】
     * @param isHandled 是否已处理完毕
     * @return
     */
    ICrashHandler setIsHandledCrash(boolean isHandled);

    /**
     * 是否需要重启程序 【设置了这个之后，在退出崩溃处理之后将自动重启程序】
     * @param isNeedReopen
     * @return
     */
    ICrashHandler setIsNeedReopen(boolean isNeedReopen);

    /**
     * 获取崩溃报告
     * @param throwable
     * @return
     */
    String getCrashReport(Throwable throwable);


    /**
     * 获取记录崩溃日志的文件
     * @return
     */
    File getCrashLogFile();
}
