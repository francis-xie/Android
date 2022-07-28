
package com.basic.router.launcher;

import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.basic.router.exception.InitException;
import com.basic.router.facade.Postcard;
import com.basic.router.facade.callback.NavigationCallback;
import com.basic.router.logs.ILogger;
import com.basic.router.logs.RLog;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Router对外统一的API
 *

 * @since 2018/5/17 上午1:10
 */
public final class Router {
    // Key of raw uri
    public static final String RAW_URI = "NTeRQWvye18AkPd6G";
    public static final String AUTO_INJECT = "wmHzgD4lOj5o4241";

    private volatile static Router instance = null;
    private volatile static boolean hasInit = false;

    private Router() {
    }

    /**
     * 初始化Router，必须先初始化
     *
     * @param application
     */
    public static void init(Application application) {
        if (!hasInit) {
            RLog.i("Router init start.");
            hasInit = _Router.init(application);

            if (hasInit) {
                _Router.afterInit();
            }

            RLog.i("Router init over.");
        }
    }

    /**
     * 获取Router的实例
     */
    public static Router getInstance() {
        if (!hasInit) {
            throw new InitException("Router::Init::Invoke init(context) first!");
        } else {
            if (instance == null) {
                synchronized (Router.class) {
                    if (instance == null) {
                        instance = new Router();
                    }
                }
            }
            return instance;
        }
    }

    /**
     * 打开调试模式
     */
    public static synchronized void openDebug() {
        _Router.openDebug();
    }

    /**
     * 是否是调试模式
     *
     * @return
     */
    public static boolean debuggable() {
        return _Router.debuggable();
    }

    /**
     * 打开日志
     */
    public static synchronized void openLog() {
        _Router.openLog();
    }

    /**
     * 设置拦截器执行的线程
     *
     * @param tpe
     */
    public static synchronized void setExecutor(ThreadPoolExecutor tpe) {
        _Router.setExecutor(tpe);
    }

    /**
     * 销毁路由
     */
    public synchronized void destroy() {
        _Router.destroy();
        hasInit = false;
    }

    public static synchronized void monitorMode() {
        _Router.monitorMode();
    }

    public static boolean isMonitorMode() {
        return _Router.isMonitorMode();
    }

    /**
     * 设置日志接口
     *
     * @param userLogger
     */
    public static void setLogger(ILogger userLogger) {
        _Router.setLogger(userLogger);
    }

    /**
     * 注入参数和服务
     */
    public void inject(@NonNull Object target) {
        _Router.inject(target);
    }

    /**
     * 构建一个路由表, draw a postcard.
     *
     * @param path Where you go.
     */
    public Postcard build(@NonNull String path) {
        return _Router.getInstance().build(path);
    }

    /**
     * 构建一个路由表, draw a postcard.
     *
     * @param path  Where you go.
     * @param group The group of path.
     */
    @Deprecated
    public Postcard build(String path, String group) {
        return _Router.getInstance().build(path, group);
    }

    /**
     * 构建一个路由表, draw a postcard.
     *
     * @param url the path
     */
    public Postcard build(@NonNull Uri url) {
        return _Router.getInstance().build(url);
    }

    /**
     * 获取服务/服务发现
     *
     * @param service interface of service
     * @param <T>     return type
     * @return instance of service
     */
    public <T> T navigation(@NonNull Class<? extends T> service) {
        return _Router.getInstance().navigation(service);
    }

    /**
     * 启动路由导航
     *
     * @param context
     * @param postcard
     * @param requestCode Set for startActivityForResult
     * @param callback    路由导航回调
     */
    public Object navigation(@NonNull Context context, @NonNull Postcard postcard, int requestCode, NavigationCallback callback) {
        return _Router.getInstance().navigation(context, postcard, requestCode, callback);
    }

    /**
     * 启动路由导航
     *
     * @param fragment
     * @param postcard
     * @param requestCode Set for startActivityForResult
     * @param callback    路由导航回调
     */
    public Object navigation(@NonNull Fragment fragment, @NonNull Postcard postcard, int requestCode, NavigationCallback callback) {
        return _Router.getInstance().navigation(fragment, postcard, requestCode, callback);
    }

    /**
     * 启动路由导航
     *
     * @param fragment
     * @param postcard
     * @param requestCode Set for startActivityForResult
     * @param callback    路由导航回调
     */
    public Object navigation(@NonNull androidx.fragment.app.Fragment fragment, @NonNull Postcard postcard, int requestCode, NavigationCallback callback) {
        return _Router.getInstance().navigation(fragment, postcard, requestCode, callback);
    }
}
