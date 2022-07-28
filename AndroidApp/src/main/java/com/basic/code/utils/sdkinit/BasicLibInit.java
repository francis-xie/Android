package com.basic.code.utils.sdkinit;

import android.app.Application;

import com.basic.code.MyApp;
import com.basic.code.core.BaseActivity;
import com.basic.code.utils.TokenUtils;
import com.basic.code.utils.ToastUtils;
import com.basic.aop.AOP;
import com.basic.http2.HttpSDK;
import com.basic.page.PageConfig;
import com.basic.router.launcher.Router;
import com.basic.face.FACE;
import com.basic.tools.Util;
import com.basic.tools.common.StringUtils;

/**
 * 系列基础库初始化
 */
public final class BasicLibInit {

    private BasicLibInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化基础库SDK
     */
    public static void init(Application application) {
        //工具类
        initUtil(application);

        //网络请求框架
        initHttp(application);

        //页面框架
        initPage(application);

        //切片框架
        initAOP(application);

        //UI框架
        initFACE(application);

        //路由框架
        initRouter(application);
    }

    /**
     * 初始化Util工具类
     */
    private static void initUtil(Application application) {
        Util.init(application);
        Util.debug(MyApp.isDebug());
        TokenUtils.init(application);
    }

    /**
     * 初始化Http
     */
    private static void initHttp(Application application) {
        //初始化网络请求框架，必须首先执行
        HttpSDK.init(application);
        //需要调试的时候执行
        if (MyApp.isDebug()) {
            HttpSDK.debug();
        }
//        XHttpSDK.debug(new CustomLoggingInterceptor()); //设置自定义的日志打印拦截器
        //设置网络请求的全局基础地址
        HttpSDK.setBaseUrl("https://gitee.com/");
//        //设置动态参数添加拦截器
//        XHttpSDK.addInterceptor(new CustomDynamicInterceptor());
//        //请求失效校验拦截器
//        XHttpSDK.addInterceptor(new CustomExpiredInterceptor());
    }

    /**
     * 初始化Page页面框架
     */
    private static void initPage(Application application) {
        PageConfig.getInstance()
                .debug(MyApp.isDebug())
                .setContainActivityClazz(BaseActivity.class)
                .init(application);
    }

    /**
     * 初始化AOP
     */
    private static void initAOP(Application application) {
        AOP.init(application);
        AOP.debug(MyApp.isDebug());
        //设置动态申请权限切片 申请权限被拒绝的事件响应监听
        AOP.setOnPermissionDeniedListener(permissionsDenied -> ToastUtils.error("权限申请被拒绝:" + StringUtils.listToString(permissionsDenied, ",")));
    }

    /**
     * 初始化FACE框架
     */
    private static void initFACE(Application application) {
        FACE.init(application);
        FACE.debug(MyApp.isDebug());
    }

    /**
     * 初始化路由框架
     */
    private static void initRouter(Application application) {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (MyApp.isDebug()) {
            Router.openLog();     // 打印日志
            Router.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        Router.init(application);
    }

}
