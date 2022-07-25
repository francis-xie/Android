package com.emis.venus.synPosData;

import android.database.sqlite.SQLiteStatement;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisProp;
import com.emis.venus.db.emisSQLiteWrapper;
import com.emis.venus.util.emisUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class emisBMSynDataStore {
//  private ServletContext context_;
//  protected Logger oLogger_ = null;
//
//  public emisBMSynDataStore(ServletContext context) {
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

  public boolean synStore() {
    boolean bReturn = false;
    try {
      String SME_URL = emisProp.getInstance().getProp("SME_URL");
      String S_NO = emisProp.getInstance().getProp("S_NO");

      String data = emisBMSynDataUtils.sendGet(SME_URL + "/ws/wechatV3/bm/getStore?sNo=" + S_NO);
      //oLogger_.info(data);

      JSONObject _oJsonObject = emisUtil.parseJSON(data);
      if (_oJsonObject != null && !_oJsonObject.isEmpty() && "0".equals(_oJsonObject.getString("code"))) {
        JSONArray store = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("store"));

        synStore(store, S_NO);
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

  private boolean synStore(JSONArray jsonArry, String S_NO) {
    //oLogger_.info("-- synStore --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement delDataStmt;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "delete from Store where S_NO = ? ";
      String insData = "insert into Store(S_NO,S_NAME,S_NAME_S,R_NO,S_KIND,S_LEVEL,S_ADDR,S_TEL,S_FAX,S_EMAIL" +
        ",CRE_DATE,CRE_USER,UPD_DATE,UPD_USER,REMARK,S_STATUS,MU_NO,S_TYPE,S_REGION_B,S_PROVINCE,S_CITY,S_REGION_S" +
        ",S_CLOSE_D,SEND_LINE,WC_STORE,MAP_LNG,MAP_LAT,WM_MU_NO,WECHAT_SEND_RANGE) " +
        " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
      if (jsonArry != null) {
        delDataStmt = oDataSrc_.prepareStmt(delData);
        delDataStmt.clearBindings();
        delDataStmt.bindString(1, S_NO);
        delDataStmt.executeUpdateDelete();

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "sNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sNameS"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "rNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sKind"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sLevel"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sAddr"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sTel"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sFax"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sEmail"));

          insDataStmt.bindString(iParam++, parseJsonData(item, "creDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "creUser"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "updDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "updUser"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "remark"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sStatus"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "muNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sType"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sRegionB"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sProvince"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sCityS"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sRegionS"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sCloseD"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sendLine"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wcStore"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "mapLng"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "mapLat"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmMuNo"));
          insDataStmt.bindString(iParam, parseJsonData(item, "wechatSendRange"));
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