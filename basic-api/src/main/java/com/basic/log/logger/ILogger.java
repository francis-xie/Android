package com.basic.log.logger;

import com.basic.log.annotation.LogLevel;

/**
 * <pre>
 *     desc   : 日志打印者实现接口
 * </pre>
 */
public interface ILogger {

    /**
     * 日志打印者名称
     *
     * @return
     */
    String getName();

    /**
     * 设置tag标签
     *
     * @param tag
     * @return
     */
    ILogger tag(String tag);

    /**
     * 设置调试模式
     *
     * @param isDebug
     * @return
     */
    ILogger debug(boolean isDebug);

    /**
     * 打印调试日志
     *
     * @param message string模版
     * @param args    模版参数
     */
    void d(String message, Object... args);

    /**
     * 打印调试日志
     *
     * @param object 对象
     */
    void d(Object object);

    /**
     * 打印错误日志
     *
     * @param message
     * @param args
     */
    void e(String message, Object... args);

    /**
     * 打印错误日志
     *
     * @param throwable 错误
     * @param args
     */
    void e(Throwable throwable, Object... args);

    /**
     * 打印错误日志
     *
     * @param throwable 错误
     * @param message   string模版
     * @param args      模版参数
     */
    void e(Throwable throwable, String message, Object... args);

    /**
     * 打印警告信息
     *
     * @param message
     * @param args
     */
    void w(String message, Object... args);

    /**
     * 打印普通信息日志
     *
     * @param message
     * @param args
     */
    void i(String message, Object... args);

    /**
     * 打印普通日志，和d类似
     *
     * @param message
     * @param args
     */
    void v(String message, Object... args);

    /**
     * 打印严重bug的日志
     *
     * @param message
     * @param args
     */
    void wtf(String message, Object... args);

    /**
     * 格式化json内容并打印
     */
    void json(String json);

    /**
     * 格式化xml内容并打印
     */
    void xml(String xml);

    /**
     * 日志打印【提供具体日志打印的功能】
     *
     * @param level     日志打印等级
     * @param tag       日志标签
     * @param message   日志的信息
     * @param throwable 错误信息
     */
    void log(@LogLevel String level, String tag, String message, Throwable throwable);
}
