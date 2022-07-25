package com.emis.venus.config;

import android.app.Application;

/**
 * APP打开就会自动执行的类，放一些全局变量啥的或者需要初始化的
 */
public class GlobalApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    initLogger();
  }

  /**
   * 初始化与配置Logger
   */
  private void initLogger() {
    /*LogConfigurator logConfigurator = new LogConfigurator();
    logConfigurator.setFileName(Environment.getExternalStorageDirectory().getAbsolutePath()
      + File.separator + "VenusApp" + File.separator + "logs"
      + File.separator + "emis.log"); // 日志存储路径，如果该路径没有会自动创建文件
    logConfigurator.setRootLevel(Level.DEBUG);
    logConfigurator.setLevel("log", Level.ERROR);
    logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
    logConfigurator.setMaxFileSize(1024 * 1024 * 5);
    logConfigurator.setImmediateFlush(true);
    logConfigurator.configure(); // 设置提交*/
  }
}