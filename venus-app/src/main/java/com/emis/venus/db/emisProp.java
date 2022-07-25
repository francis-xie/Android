package com.emis.venus.db;

import android.database.sqlite.SQLiteCursor;
import android.util.Log;
import com.emis.venus.util.helper;

import java.util.HashMap;

public class emisProp {

  private emisProp() {
    init();
  }

  static emisProp m_Prop = null;

  public static emisProp getInstance() {
    if (m_Prop == null) {
      m_Prop = new emisProp();
    }
    return m_Prop;
  }

  HashMap m_Map = new HashMap();

  public void init() {
    m_Map.clear();
    emisSQLiteWrapper db = emisDb.getInstance();
    try {
      SQLiteCursor c = null;
      try {
        //this.dropTable(db);
        db.executeQuery("select NAME,VALUE from emisprop");
        db.first();
        while (db.next()) {
          String sName = db.getString("NAME");
          String sValue = db.getString("VALUE");
          Log.d("init", "sName:" + sName + "sValue:" + sValue);
          if (sName != null) {
            sName = sName.toUpperCase();
            m_Map.put(sName, sValue);
          }
          //c.moveToNext();
        }
      } catch (Throwable e) {
        Log.e("app", "emisProp.init", e);
        //helper.alert(e);
      } finally {
        //if (c != null) c.close();
      }
    } finally {
      if (db != null) db.close();
    }
  }

  public String getProp(String sName) {
    sName = sName.toUpperCase();

    return (String) m_Map.get(sName);
  }

  public void setProp(String sName, String sValue) {
    sName = sName.toUpperCase();
    emisSQLiteWrapper db = emisDb.getInstance();
    try {
      SQLiteCursor c = null;
      try {
        c = (SQLiteCursor) helper.Query(db, "select VALUE from emisprop where NAME='" + sName + "'");
        c.moveToFirst();
        if (c.isAfterLast() == false) { // exists
          c.close();

          db.execSQL("update emisprop set VALUE='" + sValue + "' where NAME='" + sName + "'");
        } else {
          c.close();
          db.execSQL("insert into emisprop (NAME,VALUE) values ('" + sName + "','" + sValue + "')");
        }
        m_Map.put(sName, sValue);
      } finally {
        if (c != null) c.close();
      }
    } finally {
      db.close();
    }
  }

  public String getUpdateURL() {
    String s = getProp("S_UPDATEURL");
    Log.d("update1", "update1" + s);
    if (s == null || "".equals(s.trim()))
      s = "http://10.9.1.68:8081/venus_tw/";
    Log.d("update2", "update2" + s);
    return s;
  }

  public String getServerURL() {
    String s = getProp("S_URL");
    if (s == null || "".equals(s.trim())) {
      //s="http://10.9.1.70:8080/VenusRt/";
      s = "http://10.2.6.60:8080/venusrt/";
    }
    return s;
  }

  public String getStoreNo() {
    String s = getProp("S_NO");
    if (s == null || "".equals(s.trim())) {
      //s="802601";
      s = "A03";
    }
    return s;
  }

  public String getID_NO() {
    String s = getProp("ID_NO");
    if (s == null || "".equals(s.trim())) {
      //s="80260101";
      s = "A0302";
    }
    return s;
  }

  public void setUpdateURL(String sURL) {
    setProp("S_UPDATEURL", sURL);
  }

  public void setServerURL(String sURL) {
    setProp("S_URL", sURL);
  }

  public void setStoreNo(String sSNo) {
    setProp("S_NO", sSNo);
  }

  public void setID_NO(String sID_NO) {
    setProp("ID_NO", sID_NO);
  }

  String sCurrentUserId;

  public String getUserId() {
    return sCurrentUserId;
  }

  // OP_NO
  public void setUserId(String sUser) {
    sCurrentUserId = sUser;
  }

  // 是否勾選主畫面上[強制更新]
  boolean bIsForced;

  public boolean getIsForced() {
    return bIsForced;
  }

  public void setIsForced(boolean isForced) {
    bIsForced = isForced;
  }

  String sCurrentPassword;

  public String getUersPassword() {
    return sCurrentPassword;
  }

  // user's Password
  public void setUersPassword(String sPassword) {
    sCurrentPassword = sPassword;
  }

  // SL_NO , 在啟動時,會 sale_h_temp 找一次最大值
  int m_SL_NO = 0;

  public void setSL_NO(int SL_NO) {
    m_SL_NO = SL_NO;
  }

  public synchronized String allocateSL_NO() {
    m_SL_NO = m_SL_NO + 1;
    return helper.lpad("" + m_SL_NO, '0', 4);
  }

  public void setMoneyFormat(String s) {
    setProp("MFMT", s);
  }

  public String getMoneyFormat() {
    String s = getProp("MFMT");
    if (s == null || "".equals(s.trim()))
      s = "INT";
    return s;
  }

  String m_SL_DATE = null;

  public String getSL_DATE() {
    if (m_SL_DATE == null) {
      m_SL_DATE = helper.getToday();
    } else {
      String sToday = helper.getToday();
      if (!sToday.equals(m_SL_DATE)) { // 日期已經不同了,必須把 SL_NO reset
        m_SL_NO = 0;
        m_SL_DATE = sToday;
      }
    }
    return m_SL_DATE;
  }

  //用於記錄當重新送單時，最後一次的狀態(是否為第一次點餐 or 加點)
  boolean bLastEditMode;

  public boolean isbLastEditMode() {
    return bLastEditMode;
  }

  public void setbLastEditMode(boolean bLastEditMode) {
    this.bLastEditMode = bLastEditMode;
  }
}