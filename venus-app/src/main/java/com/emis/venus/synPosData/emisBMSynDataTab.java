package com.emis.venus.synPosData;

import android.database.sqlite.SQLiteStatement;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisProp;
import com.emis.venus.db.emisSQLiteWrapper;
import com.emis.venus.util.emisUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class emisBMSynDataTab {
//  private ServletContext context_;
//  protected Logger oLogger_ = null;
//
//  public emisBMSynDataTab(ServletContext context) {
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

  public boolean synTab() {
    boolean bReturn = false;
    try {
      String SME_URL = emisProp.getInstance().getProp("SME_URL");
      String S_NO = emisProp.getInstance().getProp("S_NO");

      String data = emisBMSynDataUtils.sendGet(SME_URL + "/ws/wechatV3/bm/getTab?sNo=" + S_NO);
      //oLogger_.info(data);

      JSONObject _oJsonObject = emisUtil.parseJSON(data);
      if (_oJsonObject != null && !_oJsonObject.isEmpty() && "0".equals(_oJsonObject.getString("code"))) {
        JSONArray tabs = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("tabs"));
        for (Object tabsArry : tabs) {
          try {
            JSONObject item = (JSONObject) tabsArry;
            String T_NO = parseJsonData(item, "tabH");
            JSONArray tabD = emisUtil.parseJSONArray(item.getString("tabD"));
            synTab(T_NO, tabD);
          } catch (Exception ex) {
            //oLogger_.error(ex, ex);
          }
        }
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

  private boolean synTab(String T_NO, JSONArray jsonArry) {
    //oLogger_.info("-- synTab --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement delDataStmt;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "delete from tab_d where T_NO = ? ";
      String insData = "insert into tab_d(T_NO,TD_NO,TD_NAME,TD_SEQ) values(?,?,?,?) ";
      if (jsonArry != null) {
        delDataStmt = oDataSrc_.compileStmt(delData);
        delDataStmt.clearBindings();
        delDataStmt.bindString(1, T_NO);
        delDataStmt.executeUpdateDelete();

        insDataStmt = oDataSrc_.prepareStmt(insData);
        oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, T_NO);
          insDataStmt.bindString(iParam++, parseJsonData(item, "tdNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "tdName"));
          insDataStmt.bindString(iParam, parseJsonData(item, "tdSeq"));
          insDataStmt.executeInsert();
        }
        oDataSrc_.setTransactionSuccessful();
      }
      return true;
    } catch (Exception ex) {
      //oLogger_.error(ex);
      return false;
    } finally {
      if (oDataSrc_ != null) {
        oDataSrc_.endTransaction();
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