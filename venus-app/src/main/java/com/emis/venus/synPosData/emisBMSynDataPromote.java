package com.emis.venus.synPosData;

import android.database.sqlite.SQLiteStatement;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisProp;
import com.emis.venus.db.emisSQLiteWrapper;
import com.emis.venus.util.emisUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class emisBMSynDataPromote {
//  private ServletContext context_;
//  protected Logger oLogger_ = null;
//
//  public emisBMSynDataPromote(ServletContext context) {
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

  public boolean synPromote() {
    boolean bReturn = false;
    try {
      String SME_URL = emisProp.getInstance().getProp("SME_URL");
      String S_NO = emisProp.getInstance().getProp("S_NO");

      String data = emisBMSynDataUtils.sendGet(SME_URL + "/ws/wechatV3/bm/getPromote?sNo=" + S_NO);
      //oLogger_.info(data);

      JSONObject _oJsonObject = emisUtil.parseJSON(data);
      if (_oJsonObject != null && !_oJsonObject.isEmpty() && "0".equals(_oJsonObject.getString("code"))) {
        JSONArray promoteH = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("promoteH"));
        JSONArray promoteD = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("promoteD"));

        synPromoteH(promoteH);
        synPromoteD(promoteD);
        bReturn = true;
      } else {
        //oLogger_.warn("synPromote 无返回资料or异常数据");
        bReturn = false;
      }
    } catch (Exception ex) {
      bReturn = false;
      //oLogger_.error(ex, ex);
    }

    return bReturn;
  }

  public boolean synSaleTime() {
    boolean bReturn = false;
    try {
      String SME_URL = emisProp.getInstance().getProp("SME_URL");
      String S_NO = emisProp.getInstance().getProp("S_NO");

      String data = emisBMSynDataUtils.sendGet(SME_URL + "/ws/wechatV3/bm/getSaleTime?sNo=" + S_NO);
      //oLogger_.info(data);

      JSONObject _oJsonObject = emisUtil.parseJSON(data);
      if (_oJsonObject != null && !_oJsonObject.isEmpty() && "0".equals(_oJsonObject.getString("code"))) {
        JSONArray saleTimeH = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("saleTimeH"));
        JSONArray saleTimeD = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("saleTimeD"));

        synSaleTimeH(saleTimeH);
        synSaleTimeD(saleTimeD);
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

  private boolean synPromoteH(JSONArray jsonArry) {
    //oLogger_.info("-- synPromoteH --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "truncate table Promote_h ";
      String insData = "insert into Promote_h(PM_NO,PM_ENABLE,FLS_NO,PM_CUST_LEVEL,PM_PRIORITY,PM_THEME,PM_DATE_KIND" +
        ",PM_INTERVAL,PM_COMBINE,PM_CALC,PM_ACCU,PM_AREA,PM_DATE_S,PM_DATE_E,PM_HOUR_S,PM_HOUR_E,PM_DAY_WEEK" +
        ",PM_DAY_MONTH,PM_S_NO,PM_S_KIND,PM_SG_NO,PM_FULL_AMT,PM_FULL_AMT2,PM_FULL_AMT3,PM_TTL_QTY,PM_TTL_QTY2" +
        ",PM_TTL_QTY3,PM_PRICE,PM_PRICE2,PM_PRICE3,CRE_USER,CRE_DATE,UPD_USER,UPD_DATE,REMARK,PM_SL_TYPE," +
        "PM_GROUP_S_NO,PM_BIRTH_KIND,PM_BIRTH_B,PM_BIRTH_A,PM_CARD_NO,IS_SAP,DIS_RATE) " +
        " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmEnable"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "flsNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmCustLevel"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmPriOrity"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmTheme"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmDateKind"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmInterval"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmCombine"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmCalc"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmAccu"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmArea"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmDateS"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmDateE"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmHourS"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmHourE"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmDayWeek"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmDayMonth"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmSNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmSKind"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmSgNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmFullAmt"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmFullAmt2"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmFullAmt3"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmTtlQty"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmTtlQty2"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmTtlQty3"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmPrice"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmPrice2"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmPrice3"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "creUser"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "creDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "updUser"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "updDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "remark"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmSlType"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmGroupSNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmBirthKind"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmBirthB"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmBirthA"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmCardNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "isSap"));
          insDataStmt.bindString(iParam, parseJsonData(item, "disRate"));
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
        oDataSrc_.close();
        oDataSrc_ = null;
      }
    }
  }

  private boolean synPromoteD(JSONArray jsonArry) {
    //oLogger_.info("-- synPromoteD --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "truncate table Promote_d ";
      String insData = "insert into Promote_d(PM_NO,RECNO,IS_GIFT,PM_D_KIND,P_NO,P_NO_S,D_NO,PM_QTY,PM_RG" +
        ",PM_PRICE,PM_PRICE2,PM_PRICE3,PM_PRICE4,PM_PRICE5,PM_PRICE6,GIFT_PRICE) " +
        " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "recno"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "isGift"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmDKind"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pNoS"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "dNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmQty"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmRg"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmPrice"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmPrice2"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmPrice3"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmPrice4"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmPrice5"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pmPrice6"));
          insDataStmt.bindString(iParam, parseJsonData(item, "giftPrice"));
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
        oDataSrc_.close();
        oDataSrc_ = null;
      }
    }
  }

  private boolean synSaleTimeH(JSONArray jsonArry) {
    //oLogger_.info("-- synSaleTimeH --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "truncate table sale_time_h ";
      String insData = "insert into sale_time_h(ST_NO,ST_ENABLE,FLS_NO,ST_CUST_LEVEL,ST_THEME,ST_DATE_KIND,ST_INTERVAL" +
        ",ST_AREA,ST_DATE_S,ST_DATE_E,ST_HOUR_S,ST_HOUR_E,ST_DAY_WEEK,ST_DAY_MONTH,ST_S_NO,ST_S_KIND,ST_SG_NO" +
        ",CRE_USER,CRE_DATE,UPD_USER,UPD_DATE,REMARK,SL_TYPE) " +
        " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "stNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stEnable"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "flsNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stCustLevel"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stTheme"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stDateKind"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stInterval"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stArea"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stDateS"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stDateE"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stHourS"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stHourE"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stDayWeek"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stDayMonth"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stSNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stSKind"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stSgNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "creUser"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "creDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "updUser"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "updDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "remark"));
          insDataStmt.bindString(iParam, parseJsonData(item, "slType"));
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
        oDataSrc_.close();
        oDataSrc_ = null;
      }
    }
  }

  private boolean synSaleTimeD(JSONArray jsonArry) {
    //oLogger_.info("-- synSaleTimeD --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "truncate table sale_time_d ";
      String insData = "insert into sale_time_d(ST_NO,RECNO,ST_D_KIND,P_NO,P_NO_S,D_NO) " +
        " values(?,?,?,?,?,?) ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "stNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "recno"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "stDKind"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pNoS"));
          insDataStmt.bindString(iParam, parseJsonData(item, "dNo"));
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