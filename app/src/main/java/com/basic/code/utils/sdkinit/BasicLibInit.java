
package com.basic.code.utils.sdkinit;

import android.app.Application;

import com.basic.aop.AOP;
import com.basic.dbms.FACEDataBaseRepository;
import com.basic.dbms.logs.DBLog;
import com.basic.log.Log;
import com.basic.log.annotation.LogLevel;
import com.basic.log.logger.LoggerFactory;
import com.basic.log.strategy.log.DiskLogStrategy;
import com.basic.page.PageConfig;
import com.basic.router.launcher.Router;
import com.basic.code.MyApp;
import com.basic.code.base.BaseActivity;
import com.basic.code.base.db.InternalDataBase;
import com.basic.code.utils.TokenUtils;
import com.basic.code.utils.XToastUtils;
import com.basic.tools.Util;
import com.basic.tools.common.StringUtils;

/**
 * 系列基础库的初始化
 */
public final class BasicLibInit {

    private BasicLibInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化基础库
     */
    public static void init(Application application) {
        initLog(application);
        initUtils(application);
        initPage(application);
        initAOP(application);
        initRouter(application);
        initDB(application);
    }

    /**
     * 初始化工具类
     *
     * @param application 应用上下文
     */
    private static void initLog(Application application) {
        Log.init(application);

        DiskLogStrategy diskLogStrategy = LoggerFactory.getDiskLogStrategy(
          "logs", //日志存储的目录名（相对路径）
          "log",  //生成日志的前缀名
          LogLevel.ERROR, LogLevel.DEBUG //日志记录的等级
        );

        //构建磁盘打印
        LoggerFactory.getSimpleDiskLogger(
          "DiskLogger", //Log的标示名
          diskLogStrategy, //磁盘打印的策略
          0  //方法的偏移（默认是0，可根据自己的需要设定）
        );
    }

    /**
     * 初始化工具类
     *
     * @param application 应用上下文
     */
    private static void initUtils(Application application) {
        Util.init(application);
        Util.debug(MyApp.isDebug());
        TokenUtils.init(application);
    }


    /**
     * 初始化Page页面框架
     *
     * @param application
     */
    private static void initPage(Application application) {
        //自动注册页面
        PageConfig.getInstance()
                .debug(MyApp.isDebug() ? "PageLog" : null)
                .setContainActivityClazz(BaseActivity.class)
                .init(application);
    }

    /**
     * 初始化AOP切片框架
     *
     * @param application
     */
    private static void initAOP(Application application) {
        //初始化插件
        AOP.init(application);
        //日志打印切片开启
        AOP.debug(MyApp.isDebug());
        //设置动态申请权限切片 申请权限被拒绝的事件响应监听
        AOP.setOnPermissionDeniedListener(permissionsDenied -> XToastUtils.error("权限申请被拒绝:" + StringUtils.listToString(permissionsDenied, ",")));
    }

    /**
     * 初始化Router路由
     *
     * @param application
     */
    private static void initRouter(Application application) {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (MyApp.isDebug()) {
            Router.openLog();     // 打印日志
            Router.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        Router.init(application);
    }

    /**
     * 初始化数据库框架
     *
     * @param application
     */
    private static void initDB(Application application) {
        FACEDataBaseRepository.getInstance()
                //设置内部存储的数据库实现接口
                .setIDatabase(new InternalDataBase())
                .init(application);
        DBLog.debug(MyApp.isDebug());
    }

//    /**
//     * 初始化video的存放路径[video项目太大，去除]
//     */
//    public static void initVideo() {
//        Video.setVideoCachePath(PathUtils.getExtDcimPath() + "/video/");
//        // 初始化拍摄
//        Video.initialize(false, null);
//    }


}
