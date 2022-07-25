package com.emis.venus.synPosData;

import android.database.sqlite.SQLiteStatement;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisProp;
import com.emis.venus.db.emisSQLiteWrapper;
import com.emis.venus.util.emisUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class emisBMSynDataProp {
//  private ServletContext context_;
//  protected Logger oLogger_ = null;
//
//  public emisBMSynDataProp(ServletContext context) {
//    this.context_ = context;
//    try {
//      oLogger_ = emisLogger.getlog4j(context_, this.getClass().getName());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  public void setServletContext(ServletContext context) {
//    this.context_ = context;
//    try {
//      oLogger_ = emisLogger.getlog4j(context_, this.getClass().getName());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }

  public boolean synProp() {
    boolean bReturn = false;
    try {
      String SME_URL = emisProp.getInstance().getProp("SME_URL");
      String S_NO = emisProp.getInstance().getProp("S_NO");

      String data = emisBMSynDataUtils.sendGet(SME_URL + "/ws/wechatV3/bm/getProp?sNo=" + S_NO);
      //oLogger_.info(data);

      JSONObject _oJsonObject = emisUtil.parseJSON(data);
      if (_oJsonObject != null && !_oJsonObject.isEmpty() && "0".equals(_oJsonObject.getString("code"))) {
        JSONArray prop = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("prop"));

        synProp(prop);
        bReturn = true;
      } else {
        //oLogger_.warn("无返回资料or异常数据");
        bReturn = false;
      }
    } catch (Exception ex) {
      bReturn = false;
      //oLogger_.error(ex, ex);
    }

    return bReturn;
  }

  private boolean synProp(JSONArray jsonArry) {
    //oLogger_.info("-- synProp --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement updDataStmt = null;
    SQLiteStatement insDataStmt = null;
    try {
      String today = emisUtil.todayDateAD();
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();
      if (jsonArry != null) {
        updDataStmt = oDataSrc_.prepareStmt("update Emisprop set VALUE = ?, KIND = ?, REMARK = ?, REMARK2 = ?, MENU = ?, UPD_DATE = ?, ISROOT = ? where NAME = ? ");
        insDataStmt = oDataSrc_.prepareStmt("insert into Emisprop(NAME, VALUE, KIND, REMARK, REMARK2, MENU, UPD_USER, UPD_DATE, ISROOT) values(?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        int iParam = 1;
        for (Object aJsonArry : jsonArry) {
          try {
            JSONObject item = (JSONObject) aJsonArry;
            iParam = 1;
            updDataStmt.clearBindings();
            updDataStmt.bindString(iParam++, parseJsonData(item, "value"));
            updDataStmt.bindString(iParam++, parseJsonData(item, "kind"));
            updDataStmt.bindString(iParam++, parseJsonData(item, "remark"));
            updDataStmt.bindString(iParam++, parseJsonData(item, "remark2"));
            updDataStmt.bindString(iParam++, parseJsonData(item, "menu"));
            updDataStmt.bindString(iParam++, today);
            updDataStmt.bindString(iParam++, parseJsonData(item, "isRoot"));
            updDataStmt.bindString(iParam++, parseJsonData(item, "name"));
            if (updDataStmt.executeUpdateDelete() <= 0) {
              iParam = 1;
              insDataStmt.clearBindings();
              insDataStmt.bindString(iParam++, parseJsonData(item, "name"));
              insDataStmt.bindString(iParam++, parseJsonData(item, "value"));
              insDataStmt.bindString(iParam++, parseJsonData(item, "kind"));
              insDataStmt.bindString(iParam++, parseJsonData(item, "remark"));
              insDataStmt.bindString(iParam++, parseJsonData(item, "remark2"));
              insDataStmt.bindString(iParam++, parseJsonData(item, "menu"));
              insDataStmt.bindString(iParam++, "");
              insDataStmt.bindString(iParam++, today);
              insDataStmt.bindString(iParam++, parseJsonData(item, "isRoot"));
              insDataStmt.executeInsert();
            }
            oDataSrc_.setTransactionSuccessful();
          } catch (Exception ex) {
            //oLogger_.error(ex.getMessage());
          }
        }
        emisProp.getInstance().init();
      }
      return true;
    } catch (Exception ex) {
      //oLogger_.error(ex);
      return false;
    } finally {
      if (updDataStmt != null) updDataStmt.close();
      if (insDataStmt != null) insDataStmt.close();
      if (oDataSrc_ != null) {
        oDataSrc_.endTransaction();
        oDataSrc_.close();
        oDataSrc_ = null;
      }
    }
  }

  private String parseJsonData(JSONObject item, String name) {
    try {
      return item.getString(name);
    } catch (Exception ex) {
      return "";
    }
  }
}