package com.basic.code.fragment.log;

import android.os.Environment;
import com.basic.aop.annotation.Permission;
import com.basic.aop.consts.PermissionConsts;
import com.basic.code.entity.UserInfo;
import com.basic.log.Log;
import com.basic.log.annotation.LogLevel;
import com.basic.log.logger.LoggerFactory;
import com.basic.page.annotation.Page;
import com.basic.page.base.PageSimpleListFragment;
import com.basic.tools.common.logger.ILogger;
import com.basic.tools.common.logger.Logger;
import com.basic.tools.resource.ResourceUtils;
import com.google.gson.Gson;

import java.util.List;

@Page(name = "日志记录")
public class LogFragment extends PageSimpleListFragment {
  private final static String LOG_PATH = Environment.getExternalStorageDirectory() + "/log/logs/debug_logs";

  String json;

  @Override
  protected void initArgs() {
    super.initArgs();

    UserInfo userInfo = new UserInfo()
      .setLoginName("zhiqiang")
      .setPassword("12345678");
    json = new Gson().toJson(userInfo);

    //为适配第三方日志打印接口创建的日志打印
    LoggerFactory.getPrettyLogger("LogUtils", 2);

    //适配第三方日志打印接口
    Logger.setLogger(new ILogger() {
      @Override
      public void log(int priority, String tag, String message, Throwable t) {
        Log.get().getLogger("LogUtils").log(LoggerFactory.logPriority2LogLevel(priority), tag, message, t);
      }
    });
  }

  /**
   * 初始化例子
   *
   * @param lists
   * @return
   */
  @Override
  protected List<String> initSimpleData(List<String> lists) {
    lists.add("打印普通DEBUG日志");
    lists.add("打印JSON数据");
    lists.add("打印XML数据");
    lists.add("打印错误信息");
    lists.add("适配第三方打印日志接口");
    lists.add("设置日志输出根目录为绝对路径：" + LOG_PATH);
    return lists;
  }

  /**
   * 条目点击
   *
   * @param position
   */
  @Override
  protected void onItemClick(int position) {
    switch (position) {
      case 0: //打印普通DEBUG日志
        Log.get().d(json);
        break;
      case 1: //打印JSON数据
        Log.get().json(json);
        break;
      case 2: //打印XML数据
        Log.get().xml(ResourceUtils.readStringFromAssert("AndroidManifest.xml"));
        break;
      case 3: //打印错误信息
        try {
          throw new NullPointerException("出错啦！");
        } catch (Exception e) {
          e.printStackTrace();
          Log.get().e(e);
        }
        break;
      case 4: //适配第三方打印日志接口
        Logger.i("适配第三方打印日志接口");
        Logger.iTag("zhiqiang", "适配第三方打印日志接口");

        Logger.d(json);
        try {
          throw new NullPointerException("出错啦！");
        } catch (Exception e) {
          e.printStackTrace();
          Logger.e(e);
        }
        break;
      case 5:
        setDebugLogAbsolutePath();
        break;
      default:
        break;
    }
  }

  @Permission(PermissionConsts.STORAGE)
  public void setDebugLogAbsolutePath() {
    LoggerFactory.getDiskLogger("DEBUG_LOGGER", LOG_PATH, true, "debug_log_", 0, LogLevel.DEBUG);
  }
}
