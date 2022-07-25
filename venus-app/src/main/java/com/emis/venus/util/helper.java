package com.emis.venus.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.text.format.DateFormat;
import android.view.View;

import com.emis.venus.db.emisProp;
import com.emis.venus.db.emisSQLiteWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;

public class helper {

  static Gson g_Gson = null;

  public static Gson createCustomGson() {
    if (g_Gson == null) {
      g_Gson = new GsonBuilder()
        .enableComplexMapKeySerialization()
        .serializeNulls()
        .setPrettyPrinting()
        .setVersion(1.0)
        .create();
    }
    return g_Gson;
  }

  public static Cursor Query(emisSQLiteWrapper database, String sSQL) {
    return (SQLiteCursor) database.rawQuery(sSQL, null);
  }

  public static String getString(Cursor c, String name) throws Exception {
    int index = c.getColumnIndex(name);
    if (index == -1) throw new Exception("Unable to get column(s) index of:" + name);
    return c.getString(index);
  }

  public static double getDouble(Cursor c, String name) throws Exception {
    int index = c.getColumnIndex(name);
    if (index == -1) throw new Exception("Unable to get column(d) index of:" + name);
    String s = c.getString(index);
    try {
      if (s == null) return 0;
      return Double.parseDouble(s);
    } catch (Exception e) {
      return 0;
    }
  }

  public static int getInt(Cursor c, String name) throws Exception {
    int index = c.getColumnIndex(name);
    if (index == -1) throw new Exception("Unable to get column index of:" + name);
    String s = c.getString(index);
    try {
      if (s == null) return 0;
      return Integer.parseInt(s);
    } catch (Exception e) {
      return 0;
    }
  }

  public static String SL_TYPE2Str(String sSL_TYPE) {
    String SL_TYPE;
    if ("1".equals(sSL_TYPE)) {
      SL_TYPE = "內";
    } else {
      SL_TYPE = "外";
    }
    return SL_TYPE;
  }

  // This is a fix
  // http://stackoverflow.com/questions/2886140/does-changing-the-background-also-change-the-padding-of-a-linearlayout
  public static void setBackgroundResource(View theView, int ResourceId) {
		/*
		int bottom = theView.getPaddingBottom();
	    int top = theView.getPaddingTop();
	    int right = theView.getPaddingRight();
	    int left = theView.getPaddingLeft();
	    */
    theView.setBackgroundResource(ResourceId);
    //theView.setPadding(left, top, right, bottom);
  }

  //半角转换成全角
  public static String ToDBC(String input) {
    char[] c = input.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == 12288) {
        c[i] = (char) 32;
        continue;
      }
      if (c[i] > 65280 && c[i] < 65375)
        c[i] = (char) (c[i] - 65248);
    }
    return new String(c);
  }

  // 回傳 20130204 字串
  public static String getToday() {
    return (String) DateFormat.format("yyyyMMdd", Calendar.getInstance());
  }

  public static String getTodayDateTime() {
    return (String) DateFormat.format("yyyyMMddkkmmss", Calendar.getInstance());
  }

  public static String lpad(String s, char c, int len) {
    if (s == null) {
      s = "";
    }
    len = len - s.length();
    if (len < 0) return s;
    for (int i = 0; i < len; i++) {
      s = c + s;
    }
    return s;
  }

  // 這邊卡整數 issue
  public static String double2str(double d) {
    if ("INT".equalsIgnoreCase(emisProp.getInstance().getMoneyFormat())) {
      d = Math.floor(d);
      return "" + ((int) d);
    } else {
      return "" + d;
    }
  }

  public static boolean isFixedGroupType(String sGroupType) {
    if ("0".equals(sGroupType)) {
      return true;
    }
    return false;
  }

  public static String getStack(Throwable e) {
    StringBuffer sb = new StringBuffer();
    sb.append(e.getMessage()).append("\n");
    StackTraceElement[] st = e.getStackTrace();
    if (st != null) {
      for (int i = 0; i < st.length; i++) {
        StackTraceElement s = st[i];
        sb.append(s.getClassName() + "." + s.getMethodName() + ":" + s.getLineNumber()).append("\n");
      }
    }
    return sb.toString();
  }

  public static boolean isEmptyStr(String s) {
    if ((s == null) || "".equals(s.trim())) {
      return true;
    }
    return false;
  }

}



