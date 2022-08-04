package com.basic.log;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.basic.log.crash.CrashHandler;
import com.basic.log.crash.OnCrashListener;
import com.basic.log.logger.ILogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Log全局日志打印
 */
public class Log implements ILogger {
    private static Context sContext;

    private static Log sInstance;
    /**
     * 日志容器.
     */
    private final Map<String, ILogger> mLoggers;

    private Log() {
        mLoggers = new ConcurrentHashMap<>();
    }

    /**
     * 初始化日志【包括崩溃处理】
     *
     * @param application
     */
    public static void init(@NonNull Application application) {
        sContext = application.getApplicationContext();
        CrashHandler.getInstance().init(sContext);
    }

    /**
     * 初始化日志【不包括崩溃处理】
     *
     * @param context
     */
    public static void init(@NonNull Context context) {
        sContext = context.getApplicationContext();
    }

    /**
     * 初始化崩溃处理
     *
     * @param context
     */
    public static void initCrashHandler(@NonNull Context context) {
        CrashHandler.getInstance().init(context);
    }

    /**
     * 设置崩溃的监听器
     *
     * @param listener
     */
    public static void setOnCrashListener(OnCrashListener listener) {
        CrashHandler.getInstance().setOnCrashListener(listener);
    }

    public static Context getContext() {
        testInitialize();
        return sContext;
    }

    private static void testInitialize() {
        if (sContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 Log.init() 初始化！");
        }
    }

    /**
     * 获取日志实例
     *
     * @return
     */
    public static Log get() {
        if (sInstance == null) {
            synchronized (Log.class) {
                if (sInstance == null) {
                    sInstance = new Log();
                }
            }
        }
        return sInstance;
    }

    public Map<String, ILogger> getLoggers() {
        return mLoggers;
    }

    /**
     * 获取日志
     *
     * @param logName
     * @return
     */
    @Nullable
    public ILogger getLogger(String logName) {
        return mLoggers.containsKey(logName) ? mLoggers.get(logName) : null;
    }

    /**
     * 增加日志
     *
     * @param logger
     */
    public void addLogger(@NonNull ILogger logger) {
        mLoggers.put(logger.getName(), logger);
    }

    /**
     * 去除日志
     *
     * @param logName
     */
    public void removeLogger(String logName) {
        mLoggers.remove(logName);
    }

    /**
     * 去除日志
     *
     * @param logger
     */
    public void removeLogger(@NonNull ILogger logger) {
        mLoggers.remove(logger.getName());
    }

    /**
     * 清除日志
     */
    public void clearLoggers() {
        mLoggers.clear();
    }

    /**
     * 日志打印者名称
     *
     * @return
     */
    @Override
    public String getName() {
        return "Log";
    }

    /**
     * 设置tag标签
     *
     * @param tag
     * @return
     */
    @Override
    public Log tag(String tag) {
        for (ILogger logger : mLoggers.values()) {
            logger.tag(tag);
        }
        return this;
    }

    /**
     * 设置tag标签
     *
     * @param logName
     * @param tag
     * @return
     */
    public Log tag(String logName, String tag) {
        ILogger logger = getLogger(logName);
        if (logger != null) {
            logger.tag(tag);
        }
        return this;
    }

    /**
     * 调试开关
     *
     * @param isDebug
     */
    @Override
    public Log debug(boolean isDebug) {
        for (ILogger logger : mLoggers.values()) {
            logger.debug(isDebug);
        }
        return this;
    }

    /**
     * 调试开关
     *
     * @param logName
     * @param isDebug
     * @return
     */
    public Log debug(String logName, boolean isDebug) {
        ILogger logger = getLogger(logName);
        if (logger != null) {
            logger.debug(isDebug);
        }
        return this;
    }

    /**
     * 打印调试日志
     *
     * @param message string模版
     * @param args    模版参数
     */
    @Override
    public void d(String message, Object... args) {
        for (ILogger logger : mLoggers.values()) {
            logger.d(message, args);
        }
    }

    /**
     * 打印调试日志
     *
     * @param object 对象
     */
    @Override
    public void d(Object object) {
        for (ILogger logger : mLoggers.values()) {
            logger.d(object);
        }
    }

    /**
     * 打印错误日志
     *
     * @param message
     * @param args
     */
    @Override
    public void e(String message, Object... args) {
        for (ILogger logger : mLoggers.values()) {
            logger.e(message, args);
        }
    }

    /**
     * 打印错误日志
     *
     * @param throwable 错误
     * @param args
     */
    @Override
    public void e(Throwable throwable, Object... args) {
        for (ILogger logger : mLoggers.values()) {
            logger.e(throwable, args);
        }
    }

    /**
     * 打印错误日志
     *
     * @param throwable 错误
     * @param message   string模版
     * @param args      模版参数
     */
    @Override
    public void e(Throwable throwable, String message, Object... args) {
        for (ILogger logger : mLoggers.values()) {
            logger.e(throwable, message, args);
        }
    }

    /**
     * 打印警告信息
     *
     * @param message
     * @param args
     */
    @Override
    public void w(String message, Object... args) {
        for (ILogger logger : mLoggers.values()) {
            logger.w(message, args);
        }
    }

    /**
     * 打印普通信息日志
     *
     * @param message
     * @param args
     */
    @Override
    public void i(String message, Object... args) {
        for (ILogger logger : mLoggers.values()) {
            logger.i(message, args);
        }
    }

    /**
     * 打印普通日志，和d类似
     *
     * @param message
     * @param args
     */
    @Override
    public void v(String message, Object... args) {
        for (ILogger logger : mLoggers.values()) {
            logger.v(message, args);
        }
    }

    /**
     * 打印严重bug的日志
     *
     * @param message
     * @param args
     */
    @Override
    public void wtf(String message, Object... args) {
        for (ILogger logger : mLoggers.values()) {
            logger.wtf(message, args);
        }
    }

    /**
     * 格式化json内容并打印
     *
     * @param json
     */
    @Override
    public void json(String json) {
        for (ILogger logger : mLoggers.values()) {
            logger.json(json);
        }
    }

    /**
     * 格式化xml内容并打印
     *
     * @param xml
     */
    @Override
    public void xml(String xml) {
        for (ILogger logger : mLoggers.values()) {
            logger.xml(xml);
        }
    }

    /**
     * 日志打印
     *
     * @param level
     * @param tag
     * @param message
     * @param throwable
     */
    @Override
    public void log(String level, String tag, String message, Throwable throwable) {
        for (ILogger logger : mLoggers.values()) {
            logger.log(level, tag, message, throwable);
        }
    }


}
