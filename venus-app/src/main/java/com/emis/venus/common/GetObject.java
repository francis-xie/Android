package com.emis.venus.common;

import android.util.Log;
import com.emis.venus.activity.MainActivity;
import com.pax.dal.IDAL;

public class GetObject {
  private static IDAL dal;
  public static String logStr = "";

  // 获取IDal dal对象
  public static IDAL getDal() {
    dal = MainActivity.idal;
    if (dal == null) {
      Log.e("NeptuneLiteDemo", "dal is null");
    }
    return dal;
  }
}
