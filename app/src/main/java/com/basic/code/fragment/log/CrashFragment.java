package com.basic.code.fragment.log;

import android.os.Environment;
import com.basic.aop.annotation.Permission;
import com.basic.aop.consts.PermissionConsts;
import com.basic.code.crash.ToastCrashListener;
import com.basic.log.crash.Crash;
import com.basic.log.crash.CrashHandler;
import com.basic.log.crash.mail.AutomaticEmailCrashListener;
import com.basic.log.crash.mail.SystemEmailCrashListener;
import com.basic.log.crash.ui.ShowActivityCrashListener;
import com.basic.page.annotation.Page;
import com.basic.page.base.PageSimpleListFragment;
import com.basic.tools.system.PermissionUtils;

import java.util.List;

@Page(name = "程序崩溃处理")
public class CrashFragment extends PageSimpleListFragment {

  private final static String CRASH_PATH = Environment.getExternalStorageDirectory() + "/log/crash_logs/";

  /**
   * 默认发件人地址
   */
  private final static String DEFAULT_SEND_EMAIL_ADDRESS = "zhiqiang@163.com";
  /**
   * 默认授权码
   */
  private static final String DEFAULT_SEND_PASSWORD = "zhiqiang";
  /**
   * 默认收件人地址
   */
  private final static String DEFAULT_TO_EMAIL_ADDRESS = "zhiqiang@163.com";
  /**
   * 默认抄收人地址
   */
  private final static String DEFAULT_CC_EMAIL_ADDRESS = "zhiqiang@gmail.com";

  @Override
  protected void initArgs() {
    super.initArgs();
    Crash.getInstance()
      .setSendEmail(DEFAULT_SEND_EMAIL_ADDRESS)
      .setAuthorizationCode(DEFAULT_SEND_PASSWORD)
      .setToEmails(DEFAULT_TO_EMAIL_ADDRESS)
      .setCcEmails(DEFAULT_CC_EMAIL_ADDRESS);
  }

  /**
   * 初始化例子
   *
   * @param lists
   * @return
   */
  @Override
  protected List<String> initSimpleData(List<String> lists) {
    lists.add("崩溃处理：简单的toast提示 + 程序自动启动。");
    lists.add("崩溃处理：系统方式发送崩溃日志邮件。");
    lists.add("崩溃处理：自动发送崩溃日志邮件。");
    lists.add("崩溃处理：弹出新的Activity浏览界面。");
    lists.add("设置崩溃日志输出根目录为绝对路径：" + CRASH_PATH);
    return lists;
  }

  @Override
  protected void initViews() {
    super.initViews();
    PermissionUtils.requestSystemAlertWindow(getActivity());
  }

  /**
   * 条目点击
   *
   * @param position
   */
  @Override
  protected void onItemClick(int position) {
    switch (position) {
      case 0:
        CrashHandler.getInstance().setOnCrashListener(new ToastCrashListener());
        crash();
        break;
      case 1:
        CrashHandler.getInstance().setOnCrashListener(new SystemEmailCrashListener());
        crash();
        break;
      case 2:
        CrashHandler.getInstance().setOnCrashListener(new AutomaticEmailCrashListener());
        crash();
        break;
      case 3:
        CrashHandler.getInstance().setOnCrashListener(new ShowActivityCrashListener());
        crash();
        break;
      case 4:
        setAbsolutePath();
        break;
      default:
        break;
    }
  }

  /**
   * 设置崩溃日志输出根目录为绝对路径，路径为外部存储需要申请权限
   */
  @Permission(PermissionConsts.STORAGE)
  public void setAbsolutePath() {
    CrashHandler.getInstance().setAbsolutePath(true).setCrashLogDir(CRASH_PATH);
  }

  private void crash() {
    throw new NullPointerException("崩溃啦！");
  }
}
