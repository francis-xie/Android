
package com.basic.router.launcher;

import static com.basic.router.utils.Consts.ROUTE_ROOT_SEIVICE;
import static com.basic.router.utils.Consts.ROUTE_SERVICE_AUTOWIRED;
import static com.basic.router.utils.Consts.ROUTE_SERVICE_INTERCEPTORS;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.basic.router.core.LogisticsCenter;
import com.basic.router.exception.HandlerException;
import com.basic.router.exception.InitException;
import com.basic.router.exception.NoRouteFoundException;
import com.basic.router.facade.Postcard;
import com.basic.router.facade.callback.InterceptorCallback;
import com.basic.router.facade.callback.NavigationCallback;
import com.basic.router.facade.service.AutoWiredService;
import com.basic.router.facade.service.DegradeService;
import com.basic.router.facade.service.InterceptorService;
import com.basic.router.facade.service.PathReplaceService;
import com.basic.router.facade.template.IProvider;
import com.basic.router.logs.ILogger;
import com.basic.router.logs.XRLog;
import com.basic.router.thread.DefaultPoolExecutor;
import com.basic.router.utils.Consts;
import com.basic.router.utils.TextUtils;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Router 核心功能
 *

 * @since 2018/5/18 上午12:32
 */
final class _XRouter {
    private volatile static boolean monitorMode = false;
    private volatile static boolean debuggable = false;
    private volatile static _XRouter sInstance = null;
    private volatile static boolean hasInit = false;
    private volatile static ThreadPoolExecutor executor = DefaultPoolExecutor.getInstance();
    private static Handler mMainHandler = new Handler(Looper.getMainLooper());
    private static Context mContext;

    private static InterceptorService interceptorService;

    private _XRouter() {
    }

    protected static synchronized boolean init(Application application) {
        mContext = application;
        LogisticsCenter.init(mContext, executor);
        XRLog.i("Router init success!");
        hasInit = true;

        // It's not a good idea.
        // if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        //     application.registerActivityLifecycleCallbacks(new AutowiredLifecycleCallback());
        // }
        return true;
    }

    /**
     * Destroy Router, it can be used only in debug mode.
     */
    static synchronized void destroy() {
        if (debuggable()) {
            hasInit = false;
            LogisticsCenter.suspend();
            XRLog.i("Router destroy success!");
        } else {
            XRLog.e("Destroy can be used in debug mode only!");
        }
    }

    protected static _XRouter getInstance() {
        if (!hasInit) {
            throw new InitException("XRouterCore::Init::Invoke init(context) first!");
        } else {
            if (sInstance == null) {
                synchronized (_XRouter.class) {
                    if (sInstance == null) {
                        sInstance = new _XRouter();
                    }
                }
            }
            return sInstance;
        }
    }

    static synchronized void openDebug() {
        debuggable = true;
        XRLog.i("Router openDebug");
    }

    static synchronized void openLog() {
        XRLog.debug(true);
        XRLog.i("Router openLog");
    }

    static synchronized void setExecutor(ThreadPoolExecutor tpe) {
        executor = tpe;
    }

    static synchronized void monitorMode() {
        monitorMode = true;
        XRLog.i("Router monitorMode on");
    }

    static boolean isMonitorMode() {
        return monitorMode;
    }

    static boolean debuggable() {
        return debuggable;
    }

    static void setLogger(ILogger logger) {
        XRLog.setLogger(logger);
    }

    static void inject(Object target) {
        AutoWiredService autoWiredService = ((AutoWiredService) Router.getInstance().build(ROUTE_SERVICE_AUTOWIRED).navigation());
        if (autoWiredService != null) {
            autoWiredService.autoWire(target);
        }
    }

    /**
     * 通过path和default group构建路由表
     *
     * @param path 路由路径
     */
    protected Postcard build(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new HandlerException(Consts.TAG + "Parameter is invalid!");
        } else {
            PathReplaceService pService = Router.getInstance().navigation(PathReplaceService.class);
            if (pService != null) {
                path = pService.forString(path);
            }
            return build(path, extractGroup(path));
        }
    }

    /**
     * 通过path和group构建路由表
     *
     * @param path  路由路径
     * @param group 路由组
     */
    protected Postcard build(String path, String group) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(group)) {
            throw new HandlerException(Consts.TAG + "Parameter is invalid!");
        } else {
            PathReplaceService pService = Router.getInstance().navigation(PathReplaceService.class);
            if (pService != null) {
                path = pService.forString(path);
            }
            return new Postcard(path, group);
        }
    }

    /**
     * 通过uri构建路由表
     *
     * @param uri 资源路径
     */
    protected Postcard build(Uri uri) {
        if (uri == null || TextUtils.isEmpty(uri.toString())) {
            throw new HandlerException(Consts.TAG + "Parameter invalid!");
        } else {
            PathReplaceService pService = Router.getInstance().navigation(PathReplaceService.class);
            if (pService != null) {
                uri = pService.forUri(uri);
            }
            return new Postcard(uri.getPath(), extractGroup(uri.getPath()), uri, null);
        }
    }

    /**
     * 从路由路径中抽出路由组
     *
     * @param path 路由路径
     */
    private String extractGroup(String path) {
        if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
            throw new HandlerException(Consts.TAG + "Extract the default group failed, the path must be start with '/' and contain more than 2 '/'!");
        }
        try {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            if (TextUtils.isEmpty(defaultGroup)) {
                throw new HandlerException(Consts.TAG + "Extract the default group failed! There's nothing between 2 '/'!");
            } else {
                return defaultGroup;
            }
        } catch (Exception e) {
            XRLog.w("Failed to extract default group! " + e.getMessage());
            return null;
        }
    }

    static void afterInit() {
        // Trigger interceptor init, use byName.
        interceptorService = (InterceptorService) Router.getInstance().build(ROUTE_SERVICE_INTERCEPTORS).navigation();
    }

    /**
     * 服务发现（需要实现{@link IProvider}接口）
     *
     * @param service
     * @param <T>
     * @return
     */
    protected <T> T navigation(Class<? extends T> service) {
        try {
            Postcard postcard = LogisticsCenter.buildProvider(service.getName());
            // Compatible 1.0.5 compiler sdk.
            if (postcard == null) { // No service, or this service in old version.
                postcard = LogisticsCenter.buildProvider(service.getSimpleName());
            }
            LogisticsCenter.completion(postcard);
            return (T) postcard.getProvider();
        } catch (NoRouteFoundException ex) {
            XRLog.w(ex.getMessage());

            if (debuggable() && !service.getName().contains(ROUTE_ROOT_SEIVICE)) { // Show friendly tips for user.
                String tips = "There's no service matched!\n" +
                        " service name = [" + service.getName() + "]";
                Toast.makeText(mContext, tips, Toast.LENGTH_LONG).show();
                XRLog.i(tips);
            }
            return null;
        }
    }

    /**
     * 进行路由导航
     *
     * @param context     Activity or null.
     * @param postcard    路由信息容器
     * @param requestCode 请求码
     * @param callback    路由导航回调
     */
    protected Object navigation(final Context context, final Postcard postcard, final int requestCode, final NavigationCallback callback) {
        try {
            LogisticsCenter.completion(postcard);
        } catch (NoRouteFoundException ex) {
            XRLog.e(ex);

            handleNoRouteException(postcard, callback, context);

            return null;
        }

        if (callback != null) {
            callback.onFound(postcard);
        }

        if (!postcard.isGreenChannel()) {   // It must be run in async thread, maybe interceptor cost too mush time made ANR.
            interceptorService.doInterceptions(postcard, new InterceptorCallback() {
                /**
                 * 继续执行下一个拦截器
                 *
                 * @param postcard 路由信息
                 */
                @Override
                public void onContinue(Postcard postcard) {
                    _navigation(context, postcard, requestCode, callback);
                }

                /**
                 * 拦截中断, 当该方法执行后，通道将会被销毁
                 *
                 * @param exception 中断的原因.
                 */
                @Override
                public void onInterrupt(Throwable exception) {
                    if (callback != null) {
                        callback.onInterrupt(postcard);
                    }
                    XRLog.i("Navigation failed, termination by interceptor : " + exception.getMessage());
                }
            });
        } else {
            return _navigation(context, postcard, requestCode, callback);
        }

        return null;
    }

    /**
     * 真正执行导航的方法
     *
     * @param context
     * @param postcard    路由容器
     * @param requestCode 请求code
     * @param callback    导航回调
     * @return
     */
    private Object _navigation(final Context context, final Postcard postcard, final int requestCode, final NavigationCallback callback) {
        final Context currentContext = null == context ? mContext : context;
        switch (postcard.getType()) {
            case ACTIVITY:
                // Build intent
                final Intent intent = buildIntent(currentContext, postcard);

                // Navigation in main looper.
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (requestCode > 0) {  // Need start for result
                            ActivityCompat.startActivityForResult((Activity) currentContext, intent, requestCode, postcard.getOptionsBundle());
                        } else {
                            ActivityCompat.startActivity(currentContext, intent, postcard.getOptionsBundle());
                        }

                        if ((-1 != postcard.getEnterAnim() && -1 != postcard.getExitAnim()) && currentContext instanceof Activity) {    // Old version.
                            ((Activity) currentContext).overridePendingTransition(postcard.getEnterAnim(), postcard.getExitAnim());
                        }

                        if (callback != null) { // Navigation over.
                            callback.onArrival(postcard);
                        }
                    }
                });
                break;
            case PROVIDER:
                return postcard.getProvider();
            case BROADCAST:
            case CONTENT_PROVIDER:
            case FRAGMENT:
                Object instance = findFragment(postcard);
                if (instance != null) return instance;
            case METHOD:
            case SERVICE:
            default:
                return null;
        }

        return null;
    }

    /**
     * 进行路由导航 [Fragment]
     *
     * @param fragment    fragment
     * @param postcard    路由信息容器
     * @param requestCode 请求码
     * @param callback    路由导航回调
     */
    protected Object navigation(final Fragment fragment, final Postcard postcard, final int requestCode, final NavigationCallback callback) {
        try {
            LogisticsCenter.completion(postcard);
        } catch (NoRouteFoundException ex) {
            XRLog.e(ex);

            handleNoRouteException(postcard, callback, fragment.getActivity());

            return null;
        }

        if (callback != null) {
            callback.onFound(postcard);
        }

        if (!postcard.isGreenChannel()) {   // It must be run in async thread, maybe interceptor cost too mush time made ANR.
            interceptorService.doInterceptions(postcard, new InterceptorCallback() {
                /**
                 * 继续执行下一个拦截器
                 *
                 * @param postcard 路由信息
                 */
                @Override
                public void onContinue(Postcard postcard) {
                    _navigation(fragment, postcard, requestCode, callback);
                }

                /**
                 * 拦截中断, 当该方法执行后，通道将会被销毁
                 *
                 * @param exception 中断的原因.
                 */
                @Override
                public void onInterrupt(Throwable exception) {
                    if (callback != null) {
                        callback.onInterrupt(postcard);
                    }
                    XRLog.i("Navigation failed, termination by interceptor : " + exception.getMessage());
                }
            });
        } else {
            return _navigation(fragment, postcard, requestCode, callback);
        }
        return null;
    }


    /**
     * 真正执行导航的方法 [Fragment]
     *
     * @param fragment
     * @param postcard    路由容器
     * @param requestCode 请求code
     * @param callback    导航回调
     * @return
     */
    private Object _navigation(final Fragment fragment, final Postcard postcard, final int requestCode, final NavigationCallback callback) {
        switch (postcard.getType()) {
            case ACTIVITY:
                // Build intent
                final Intent intent = buildIntent(fragment.getActivity(), postcard);

                // Navigation in main looper.
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (requestCode > 0) {  // Need start for result
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                fragment.startActivityForResult(intent, requestCode);
                            } else {
                                fragment.startActivityForResult(intent, requestCode, postcard.getOptionsBundle());
                            }
                        } else {
                            ActivityCompat.startActivity(fragment.getActivity(), intent, postcard.getOptionsBundle());
                        }

                        if ((-1 != postcard.getEnterAnim() && -1 != postcard.getExitAnim())) {    // Old version.
                            (fragment.getActivity()).overridePendingTransition(postcard.getEnterAnim(), postcard.getExitAnim());
                        }

                        if (callback != null) { // Navigation over.
                            callback.onArrival(postcard);
                        }
                    }
                });
                break;
            case PROVIDER:
                return postcard.getProvider();
            case BROADCAST:
            case CONTENT_PROVIDER:
            case FRAGMENT:
                Object instance = findFragment(postcard);
                if (instance != null) return instance;
            case METHOD:
            case SERVICE:
            default:
                return null;
        }

        return null;
    }

    /**
     * 进行路由导航 [Fragment]
     *
     * @param fragment    fragment
     * @param postcard    路由信息容器
     * @param requestCode 请求码
     * @param callback    路由导航回调
     */
    protected Object navigation(final androidx.fragment.app.Fragment fragment, final Postcard postcard, final int requestCode, final NavigationCallback callback) {
        try {
            LogisticsCenter.completion(postcard);
        } catch (NoRouteFoundException ex) {
            XRLog.e(ex);

            handleNoRouteException(postcard, callback, fragment.getActivity());

            return null;
        }

        if (callback != null) {
            callback.onFound(postcard);
        }

        if (!postcard.isGreenChannel()) {   // It must be run in async thread, maybe interceptor cost too mush time made ANR.
            interceptorService.doInterceptions(postcard, new InterceptorCallback() {
                /**
                 * 继续执行下一个拦截器
                 *
                 * @param postcard 路由信息
                 */
                @Override
                public void onContinue(Postcard postcard) {
                    _navigation(fragment, postcard, requestCode, callback);
                }

                /**
                 * 拦截中断, 当该方法执行后，通道将会被销毁
                 *
                 * @param exception 中断的原因.
                 */
                @Override
                public void onInterrupt(Throwable exception) {
                    if (callback != null) {
                        callback.onInterrupt(postcard);
                    }
                    XRLog.i("Navigation failed, termination by interceptor : " + exception.getMessage());
                }
            });
        } else {
            return _navigation(fragment, postcard, requestCode, callback);
        }
        return null;
    }

    /**
     * 真正执行导航的方法 [Fragment]
     *
     * @param fragment
     * @param postcard    路由容器
     * @param requestCode 请求code
     * @param callback    导航回调
     * @return
     */
    private Object _navigation(final androidx.fragment.app.Fragment fragment, final Postcard postcard, final int requestCode, final NavigationCallback callback) {
        switch (postcard.getType()) {
            case ACTIVITY:
                // Build intent
                final Intent intent = buildIntent(fragment.getActivity(), postcard);

                // Navigation in main looper.
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (requestCode > 0) {  // Need start for result
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                fragment.startActivityForResult(intent, requestCode);
                            } else {
                                fragment.startActivityForResult(intent, requestCode, postcard.getOptionsBundle());
                            }
                        } else {
                            ActivityCompat.startActivity(fragment.getActivity(), intent, postcard.getOptionsBundle());
                        }

                        if ((-1 != postcard.getEnterAnim() && -1 != postcard.getExitAnim())) {    // Old version.
                            (fragment.getActivity()).overridePendingTransition(postcard.getEnterAnim(), postcard.getExitAnim());
                        }

                        if (callback != null) { // Navigation over.
                            callback.onArrival(postcard);
                        }
                    }
                });
                break;
            case PROVIDER:
                return postcard.getProvider();
            case BROADCAST:
            case CONTENT_PROVIDER:
            case FRAGMENT:
                Object instance = findFragment(postcard);
                if (instance != null) return instance;
            case METHOD:
            case SERVICE:
            default:
                return null;
        }

        return null;
    }

    /**
     * 构建intent
     *
     * @param context
     * @param postcard
     * @return
     */
    @NonNull
    private Intent buildIntent(Context context, Postcard postcard) {
        final Intent intent = new Intent(context, postcard.getDestination());
        intent.putExtras(postcard.getExtras());

        // Set flags.
        int flags = postcard.getFlags();
        if (flags != -1) {
            intent.setFlags(flags);
        } else if (!(context instanceof Activity)) {    // Non activity, need less one flag.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        // Set Actions
        String action = postcard.getAction();
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }
        return intent;
    }

    @Nullable
    private Object findFragment(Postcard postcard) {
        Class fragmentMeta = postcard.getDestination();
        try {
            Object instance = fragmentMeta.getConstructor().newInstance();
            if (instance instanceof Fragment) {
                ((Fragment) instance).setArguments(postcard.getExtras());
            } else if (instance instanceof androidx.fragment.app.Fragment) {
                ((androidx.fragment.app.Fragment) instance).setArguments(postcard.getExtras());
            }
            return instance;
        } catch (Exception ex) {
            XRLog.e("Fetch fragment instance error, " + TextUtils.formatStackTrace(ex.getStackTrace()));
        }
        return null;
    }

    /**
     * 处理路由丢失的错误
     *
     * @param postcard
     * @param callback
     * @param activity
     */
    private void handleNoRouteException(Postcard postcard, NavigationCallback callback, Context activity) {
        if (debuggable()) { // Show friendly tips for user.
            String tips = "There's no route matched!\n" +
                    " Path = [" + postcard.getPath() + "]\n" +
                    " Group = [" + postcard.getGroup() + "]";
            Toast.makeText(mContext, tips, Toast.LENGTH_LONG).show();
            XRLog.i(tips);
        }

        if (callback != null) {
            callback.onLost(postcard);
        } else {    // No callback for this invoke, then we use the global degrade service.
            DegradeService degradeService = Router.getInstance().navigation(DegradeService.class);
            if (degradeService != null) {
                degradeService.onLost(activity, postcard);
            }
        }
    }
}
