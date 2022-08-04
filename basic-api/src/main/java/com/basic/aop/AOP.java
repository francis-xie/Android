
package com.basic.aop;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.basic.aop.cache.XCache;
import com.basic.aop.cache.XDiskCache;
import com.basic.aop.cache.XMemoryCache;
import com.basic.aop.cache.converter.IDiskConverter;
import com.basic.aop.cache.converter.SerializableDiskConverter;
import com.basic.aop.cache.key.DefaultCacheKeyCreator;
import com.basic.aop.cache.key.ICacheKeyCreator;
import com.basic.aop.checker.IThrowableHandler;
import com.basic.aop.checker.Interceptor;
import com.basic.aop.logger.ILogger;
import com.basic.aop.logger.XLogger;
import com.basic.aop.util.PermissionUtils.OnPermissionDeniedListener;
import com.basic.aop.util.Strings;

/**
 * AOP
 */
public final class AOP {

    private static Context sContext;

    /**
     * 权限申请被拒绝的监听
     */
    private static OnPermissionDeniedListener sOnPermissionDeniedListener;
    /**
     * 默认的磁盘序列化接口
     */
    private static IDiskConverter sIDiskConverter = new SerializableDiskConverter();
    /**
     * 缓存Key的生成器
     */
    private static ICacheKeyCreator sICacheKeyCreator = new DefaultCacheKeyCreator();
    /**
     * 自定义拦截切片的拦截器接口
     */
    private static Interceptor sInterceptor;

    /**
     * 自定义的异常处理者接口
     */
    private static IThrowableHandler sIThrowableHandler;

    /**
     * 初始化
     *
     * @param application
     */
    public static void init(Application application) {
        sContext = application.getApplicationContext();
    }

    /**
     * 获取全局上下文
     *
     * @return
     */
    public static Context getContext() {
        testInitialize();
        return sContext;
    }

    private static void testInitialize() {
        if (sContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 AOP.init() 初始化！");
        }
    }

    //============动态申请权限失败事件设置=============//

    /**
     * 设置权限申请被拒绝的监听
     *
     * @param listener 权限申请被拒绝的监听器
     */
    public static void setOnPermissionDeniedListener(@NonNull OnPermissionDeniedListener listener) {
        AOP.sOnPermissionDeniedListener = listener;
    }

    public static OnPermissionDeniedListener getOnPermissionDeniedListener() {
        return sOnPermissionDeniedListener;
    }

    //============磁盘缓存的序列化接口=============//

    /**
     * 设置默认的磁盘缓存的序列化接口
     *
     * @param sIDiskConverter
     */
    public static void setIDiskConverter(@NonNull IDiskConverter sIDiskConverter) {
        AOP.sIDiskConverter = sIDiskConverter;
    }

    public static IDiskConverter getIDiskConverter() {
        return sIDiskConverter;
    }

    //============缓存Key的生成器=============//

    /**
     * @param sICacheKeyCreator
     */
    public static void setICacheKeyCreator(@NonNull ICacheKeyCreator sICacheKeyCreator) {
        AOP.sICacheKeyCreator = sICacheKeyCreator;
    }

    public static ICacheKeyCreator getICacheKeyCreator() {
        return sICacheKeyCreator;
    }

    //============自定义拦截器设置=============//

    /**
     * 设置自定义拦截切片的拦截器接口
     *
     * @param sInterceptor 自定义拦截切片的拦截器接口
     */
    public static void setInterceptor(@NonNull Interceptor sInterceptor) {
        AOP.sInterceptor = sInterceptor;
    }

    public static Interceptor getInterceptor() {
        return sInterceptor;
    }

    //============自定义捕获异常处理=============//

    /**
     * 设置自定义捕获异常处理
     *
     * @param sIThrowableHandler 自定义捕获异常处理
     */
    public static void setIThrowableHandler(@NonNull IThrowableHandler sIThrowableHandler) {
        AOP.sIThrowableHandler = sIThrowableHandler;
    }

    public static IThrowableHandler getIThrowableHandler() {
        return sIThrowableHandler;
    }

    //============日志打印设置=============//

    /**
     * 设置是否打开调试
     *
     * @param isDebug 是否打开调试
     */
    public static void debug(boolean isDebug) {
        XLogger.debug(isDebug);
    }

    /**
     * 设置调试模式
     *
     * @param tag tag信息
     */
    public static void debug(String tag) {
        XLogger.debug(tag);
    }

    /**
     * 设置打印日志的等级（只打印改等级以上的日志）
     *
     * @param priority 日志的等级
     */
    public static void setPriority(int priority) {
        XLogger.setPriority(priority);
    }

    /**
     * 设置日志打印时参数序列化的接口方法
     *
     * @param sISerializer 日志打印时参数序列化的接口方法
     */
    public static void setISerializer(@NonNull Strings.ISerializer sISerializer) {
        XLogger.setISerializer(sISerializer);
    }

    /**
     * 设置日志记录者的接口
     *
     * @param logger 日志记录者的接口
     */
    public static void setLogger(@NonNull ILogger logger) {
        XLogger.setLogger(logger);
    }

    //============缓存设置=============//

    /**
     * 初始化内存缓存
     *
     * @param memoryMaxSize 内存缓存的最大数量
     */
    public static void initMemoryCache(int memoryMaxSize) {
        XMemoryCache.getInstance().init(memoryMaxSize);
    }

    /**
     * 初始化磁盘缓存
     *
     * @param builder 缓存构造器
     */
    public static void initDiskCache(XCache.Builder builder) {
        XDiskCache.getInstance().init(builder.isDiskCache(true));
    }


}
