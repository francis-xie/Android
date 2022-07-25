package com.emis.venus.synPosData;

import android.database.sqlite.SQLiteStatement;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisProp;
import com.emis.venus.db.emisSQLiteWrapper;
import com.emis.venus.util.emisUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class emisBMSynDataPart {
//  private ServletContext context_;
//  protected Logger oLogger_ = null;
//
//  public emisBMSynDataPart(ServletContext context) {
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

  public boolean synAllPart() {
    boolean bReturn = false;
    try {
      emisProp prop = emisProp.getInstance();
      String SME_URL = prop.getProp("SME_URL");
      String S_NO = prop.getProp("S_NO");
      String ID_NO = prop.getProp("ID_NO");

      String data = emisBMSynDataUtils.sendGet(SME_URL + "/ws/wechatV3/bm/getPartAllData?sNo=" + S_NO + "&idNo=" + ID_NO);
      //oLogger_.info(data);

      JSONObject _oJsonObject = emisUtil.parseJSON(data);
      if (_oJsonObject != null && !_oJsonObject.isEmpty() && "0".equals(_oJsonObject.getString("code"))) {
        JSONArray subdep = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("subdep"));
        JSONArray depart = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("depart"));
        JSONArray departTab = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("departTab"));
        JSONArray ddepart = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("ddepart"));
        JSONArray part = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("part"));
        JSONArray smenuH = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("smenuH"));
        JSONArray smenuD = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("smenuD"));
        JSONArray seasoningH = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("seasoningH"));
        JSONArray seasoningD = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("seasoningD"));

        synSubdep(subdep);
        synDepart(depart);
        //synDepartTab(departTab);
        synDdepart(ddepart);
        synPart(part);
        synSmenuH(smenuH);
        synSmenuD(smenuD);
        synSeasoningH(seasoningH);
        synSeasoningD(seasoningD);
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

  public boolean synPartSaleOut() {
    boolean bReturn = false;
    try {
      emisProp prop = emisProp.getInstance();
      String SME_URL = prop.getProp("SME_URL");
      String S_NO = prop.getProp("S_NO");
      String ID_NO = prop.getProp("ID_NO");
      String BM_VERSION = prop.getProp("BM_VERSION");

      String data = emisBMSynDataUtils.sendGet(SME_URL + "/ws/wechatV3/bm/getPartSaleOut?sNo=" + S_NO + "&idNo=" + ID_NO + "&bmVersion=" + BM_VERSION);
      //oLogger_.info(data);

      JSONObject _oJsonObject = emisUtil.parseJSON(data);
      if (_oJsonObject != null && !_oJsonObject.isEmpty() && "0".equals(_oJsonObject.getString("code"))) {
        JSONArray partSaleOut = emisUtil.parseJSONArray(_oJsonObject.getJSONObject("result").getString("partSaleOut"));

        emisSQLiteWrapper oDataSrc_ = null;
        SQLiteStatement updSaleOutStmt = null;
        SQLiteStatement updSaleOut4SMStmt = null;
        try {
          oDataSrc_ = emisDb.getInstance();
          oDataSrc_.beginTransaction();

          if (partSaleOut != null) {
            String cleanFlag = "update Part set WM_SALE_OUT_FLAG = '' ";
            String updSaleOut = "update Part set WM_SALE_OUT = 'Y', WM_SALE_OUT_FLAG = 'Y' where P_NO = ? ";
            String updNonSaleOut = "update Part set WM_SALE_OUT = '' where WM_SALE_OUT_FLAG = '' ";
            String cleanFlag4SM = "update Smenu_h set WM_SALE_OUT_FLAG = '' ";
            String updSaleOut4SM = "update Smenu_h set WM_SALE_OUT = 'Y', WM_SALE_OUT_FLAG = 'Y' where SM_NO = ? ";
            String updNonSaleOut4SM = "update Smenu_h set WM_SALE_OUT = '' where WM_SALE_OUT_FLAG = '' ";

            oDataSrc_.execSQL(cleanFlag);
            oDataSrc_.execSQL(cleanFlag4SM);
            oDataSrc_.setTransactionSuccessful();

            updSaleOutStmt = oDataSrc_.prepareStmt(updSaleOut);
            updSaleOut4SMStmt = oDataSrc_.prepareStmt(updSaleOut4SM);
            for (Object aJsonArry : partSaleOut) {
              JSONObject item = (JSONObject) aJsonArry;
              oDataSrc_.clearBindings();
              if ("1".equals(parseJsonData(item, "pType"))) {
                updSaleOut4SMStmt.clearBindings();
                updSaleOut4SMStmt.bindString(1, parseJsonData(item, "pNo"));
                updSaleOut4SMStmt.executeUpdateDelete();
              } else {
                updSaleOutStmt.clearBindings();
                updSaleOutStmt.bindString(1, parseJsonData(item, "pNo"));
                updSaleOutStmt.executeUpdateDelete();
              }
            }
            oDataSrc_.setTransactionSuccessful();

            oDataSrc_.execSQL(updNonSaleOut);
            oDataSrc_.execSQL(updNonSaleOut4SM);
            oDataSrc_.setTransactionSuccessful();
          }
          bReturn = true;
        } catch (Exception ex) {
          //oLogger_.error(ex);
          bReturn = false;
        } finally {
          if (updSaleOut4SMStmt != null) {
            updSaleOut4SMStmt.close();
          }
          if (updSaleOutStmt != null) {
            updSaleOutStmt.close();
          }
          if (oDataSrc_ != null) {
            oDataSrc_.endTransaction();
            oDataSrc_.close();
            oDataSrc_ = null;
          }
        }
        bReturn = true;
      } else {
        //oLogger_.warn("synPartSaleOut 无返回资料or异常数据");
        bReturn = false;
      }
    } catch (Exception ex) {
      bReturn = false;
      //oLogger_.error(ex, ex);
    }

    return bReturn;
  }

  private boolean synSubdep(JSONArray jsonArry) {
    //oLogger_.info("-- synSubdep --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "truncate table subdep ";
      String insData = "insert into subdep(SUBDEP,SUB_NAME,CRE_DATE,UPD_DATE) values(?,?,?,?) ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "sub"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "subName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "creDate"));
          insDataStmt.bindString(iParam, parseJsonData(item, "updDate"));
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

  private boolean synDepart(JSONArray jsonArry) {
    //oLogger_.info("-- synDepart --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "truncate table depart ";
      String insData = "insert into depart(D_NO, SUBDEP, D_CNAME, D_ENAME, D_TYPE, CRE_DATE, UPD_DATE, D_DOWN, SEA_NO" +
        ", UN_NO, SALE_OPT, USED, DP_TYPE, D_NO_OD, WM_MIN_ORDER_NUM, WM_PACKAGE_BOX_NUM, WM_PACKAGE_BOX_PRICE" +
        ", WM_USED, WM_D_NO, D_OVERTIME, SEQ_NO) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "dNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "sub"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "dName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "dEName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "dType"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "creDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "updDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "dDown"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "seaNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "unNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "saleOpt"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "used"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "dpType"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "dNoOd"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmMinOrderNum"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmPackageBoxNum"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmPackageBoxPrice"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmUsed"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmDNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "dOverTime"));
          insDataStmt.bindString(iParam, parseJsonData(item, "seqNo"));
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

  /*private boolean synDepartTab(JSONArray jsonArry) {
    //oLogger_.info("-- synDepartTab --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt = null;
    SQLiteStatement insData2Stmt = null;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "delete from tab_d where T_NO = 'WM_D_NO' ";
      String delData2 = "truncate table bm_depart_img ";
      String insData = "insert into tab_d(T_NO,TD_NO,TD_NAME,TD_SEQ) values(?,?,?,?) ";
      String insData2 = "insert into bm_depart_img(D_NO,F_FILE,CRE_DATE) values(?,?,?) ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);
        oDataSrc_.execSQL(delData2);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        insData2Stmt = oDataSrc_.prepareStmt(insData2);

        oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "tNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "tdNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "tdName"));
          insDataStmt.bindString(iParam, parseJsonData(item, "tdSeq"));
          insDataStmt.executeInsert();

          insData2Stmt.clearBindings();
          iParam = 1;
          insData2Stmt.bindString(iParam++, parseJsonData(item, "tdNo"));
          insData2Stmt.bindString(iParam++, parseJsonData(item, "fFile"));
          insData2Stmt.bindString(iParam, parseJsonData(item, "creDate"));
          insData2Stmt.executeInsert();
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
  }*/

  private boolean synDdepart(JSONArray jsonArry) {
    //oLogger_.info("-- synDdepart --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "truncate table ddepart ";
      String insData = "insert into ddepart(DD_NO, D_NO, DD_CNAME, DD_ENAME, CRE_DATE, UPD_DATE, SEA_NO, D_DOWN, PRINT_LABLE, DISP_FLAG, ERP_NO, SEQ_NO) values(?,?,?,?,?,?,?,?,?,?,?,?) ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "ddNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "dNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "ddName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "ddEName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "creDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "updDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "seaNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "dDown"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "printLable"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "dispFlag"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "erpNo"));
          insDataStmt.bindString(iParam, parseJsonData(item, "seqno"));
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

  private boolean synPart(JSONArray jsonArry) {
    //oLogger_.info("-- synPart --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "truncate table part ";
      String insData = "insert into part(P_NO, P_EAN, P_NAME, P_NAME_S, P_TAX, P_PU, D_NO, P_PHOTO, P_PRICE, P_PRICE2" +
        ", P_PRICE3, P_PRICE4, P_PRICE5, P_PRICE6, UN_NO, P_IN_UN, P_RATE, P_DEF1, P_DEF2, P_DEF4, P_DEF3, P_STATUS" +
        ", P_PS_QTY, CRE_DATE, UPD_DATE, P_DEFA, P_DEFB, P_DEFC, P_DEFD, P_PRICE_ORI, P_TAX_RATE, DD_NO, P_PY, LBL_NAME" +
        ", IS_POR, IS_WEIGH, P_DC, STORAGE_LIFE, P_NO_OD, REM_CODE, WM_SETTING_TYPE, WM_MIN_ORDER_NUM" +
        ", WM_PACKAGE_BOX_NUM, WM_PACKAGE_BOX_PRICE, WM_USED, WM_DESC, WM_ATTR, P_NEW_DATE, DISABLE_SEA_ITEM_NO, WM_SALE_OUT, WM_SALE_OUT_FLAG) " +
        " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'') ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "pNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pEan"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pNameS"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pTax"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pPu"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "dNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pPhone"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pPrice"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pPrice2"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pPrice3"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pPrice4"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pPrice5"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pPrice6"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "unNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pInNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pRate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pDef1"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pDef2"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pDef3"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pDef4"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pStatus"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pPsQty"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "creDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "updDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pDefA"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pDefB"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pDefC"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pDefD"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pPriceOri"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pTaxRate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "ddNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pPy"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "lblName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "isPor"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "isWeigh"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pDc"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "storageLife"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pNoOd"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "remCode"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmSettingType"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmMinOrderNum"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmPackageBoxNum"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmPackageBoxPrice"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmUsed"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmDesc"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmAttr"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pNewDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "disableSeaTimeNo"));
          insDataStmt.bindString(iParam, parseJsonData(item, "wmSaleOut"));
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

  private boolean synSmenuH(JSONArray jsonArry) {
    //oLogger_.info("-- synSmenuH --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "truncate table smenu_h ";
      String insData = "insert into smenu_h(SM_NO, SM_NAME, SM_UN_NO, SM_TAX, SM_DP_NO, SM_PRICE, SM_PRICE2, SM_PRICE3" +
        ", SM_PRICE4, SM_PRICE5, SM_PRICE6, FLS_NO, CRE_DATE, UPD_DATE, P_PU, SM_TAX_RATE, SM_PY, GROUP_NAME_A" +
        ", MAX_NUM_A, GROUP_NAME_B, MAX_NUM_B, GROUP_NAME_C, MAX_NUM_C, GROUP_NAME_D, MAX_NUM_D, GROUP_NAME_E" +
        ", MAX_NUM_E, P_NO_S_OD, WM_SETTING_TYPE, WM_MIN_ORDER_NUM, WM_PACKAGE_BOX_NUM, WM_PACKAGE_BOX_PRICE" +
        ", WM_USED, WM_DESC, SM_PHOTO, WM_ATTR, SEQ_NO, WM_SALE_OUT, WM_SALE_OUT_FLAG " +
        ", MIN_NUM_A, MIN_NUM_B, MIN_NUM_C, MIN_NUM_D, MIN_NUM_E) " +
        " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'',?,?,?,?,?) ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "smNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smUnNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smTax"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smDpNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smPrice"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smPrice2"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smPrice3"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smPrice4"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smPrice5"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smPrice6"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "flsNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "creDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "updDate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pPu"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smTaxRate"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smPy"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "groupNameA"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "maxNumA"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "groupNameB"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "maxNumB"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "groupNameC"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "maxNumC"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "groupNameD"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "maxNumD"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "groupNameE"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "maxNumE"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pNoSOd"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmSettingType"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmMinOrderNum"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmPackageBoxNum"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmPackageBoxPrice"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmUsed"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmDesc"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smPhoto"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmAttr"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "seqno"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "wmSaleOut"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "minNumA", "0"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "minNumB", "0"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "minNumC", "0"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "minNumD", "0"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "minNumE", "0"));
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

  private boolean synSmenuD(JSONArray jsonArry) {
    //oLogger_.info("-- synSmenuD --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "truncate table smenu_d ";
      String insData = "insert into smenu_d(SM_NO,RECNO,P_NO,SM_QTY,SM_PRICE,GROUP_TYPE,ADD_PRICE,P_NO_S_OD,WM_GET_PLAN_ATTR) values(?,?,?,?,?,?,?,?,?) ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          oDataSrc_.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "smNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "recno"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smQty"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "smPrice"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "groupType"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "addPrice"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "pNoSOd"));
          insDataStmt.bindString(iParam, parseJsonData(item, "wmGetPlanAttr"));
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

  private boolean synSeasoningH(JSONArray jsonArry) {
    //oLogger_.info("-- synSeasoningH --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "truncate table seasoning_h ";
      String insData = "insert into seasoning_h(SEA_NO,SEA_NAME,SEA_ENAME,SEA_TYPE,IS_SINGLE,USED,SEQ_NO,WM_CANUSE) values(?,?,?,?,?,?,?,?) ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "seaNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "seaName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "seaEName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "seaType"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "isSingle"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "used"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "seqno"));
          insDataStmt.bindString(iParam, parseJsonData(item, "wmCanUse"));
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

  private boolean synSeasoningD(JSONArray jsonArry) {
    //oLogger_.info("-- synSeasoningD --");
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement insDataStmt;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delData = "truncate table seasoning_d ";
      String insData = "insert into seasoning_d(SEA_ITEM_NO,SEA_NO,SEA_ITEM_NAME,SEA_ITEM_ENAME,DEF_CHOOSE,PRICE,PRICE2,PRICE3,USED,SEQ_NO,SEA_ITEM_NO_OD,SEA_REM_CODE) " +
        " values(?,?,?,?,?,?,?,?,?,?,?,?) ";
      if (jsonArry != null) {
        oDataSrc_.execSQL(delData);

        insDataStmt = oDataSrc_.prepareStmt(insData);
        for (Object aJsonArry : jsonArry) {
          JSONObject item = (JSONObject) aJsonArry;
          insDataStmt.clearBindings();
          int iParam = 1;
          insDataStmt.bindString(iParam++, parseJsonData(item, "seaItemNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "seaNo"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "seaItemName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "seaItemEName"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "defChoose"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "price"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "price2"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "price3"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "used"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "seqno"));
          insDataStmt.bindString(iParam++, parseJsonData(item, "seaItemNoOd"));
          insDataStmt.bindString(iParam, parseJsonData(item, "wmCanUse"));
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

  private String parseJsonData(JSONObject item, String name, String def) {
    try {
      return item.getString(name);
    } catch (Exception ex) {
      return def;
    }
  }
}