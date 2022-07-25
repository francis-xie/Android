package com.emis.venus.synPosData;

import android.database.sqlite.SQLiteStatement;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisProp;
import com.emis.venus.db.emisSQLiteWrapper;
import com.emis.venus.util.emisDate;
import com.emis.venus.util.emisUtil;
import net.sf.json.JSONObject;

/**
 * 大屏点餐-同步后台数据接口
 */
public class emisBMSynDataImpl {

  private final static String ACT_synPartData = "synPartData";  // 1.4 同步数据
  private final static String ACT_checkLogin = "checkLogin";  // 1.5 登录身份检查
  private String defaultAct;

  /**
   * 设置默认act
   *
   * @param defaultAct
   */
  public void setDefaultAct(String defaultAct) {
    this.defaultAct = defaultAct;
  }

  /**
   * @param sAct 获取请求Act
   * @return
   */
  public String postAction(String sAct) {
    // 当没有传act时取默认的defaultAct
    if (sAct == null || "".equals(sAct.trim())) {
      sAct = this.defaultAct;
    }
    // 选择响应业务
    if (ACT_synPartData.equalsIgnoreCase(sAct)) {
      return doSynPartData();
    } else if (ACT_checkLogin.equalsIgnoreCase(sAct)) {
      return doCheckLogin();
    }
    return null;
  }


  /**
   * 分类列表
   *
   * @return 分类列表
   */
  private String doSynPartData() {
    String code = "";
    String msg = "";

    try {
      emisBMSynDataStore syn0 = new emisBMSynDataStore();
      syn0.synStore();

      emisBMSynDataProp syn4 = new emisBMSynDataProp();
      syn4.synProp();
      emisProp.getInstance().init();

      emisBMSynDataPart syn = new emisBMSynDataPart();
      syn.synAllPart();

//      emisBMSynDataImages syn2 = new emisBMSynDataImages();
//      syn2.synSettingImg();

//      emisBMSynDataPromote syn3 = new emisBMSynDataPromote();
//      syn3.synPromote();
//      syn3.synSaleTime();

      emisBMSynDataTab synTab = new emisBMSynDataTab();
      synTab.synTab();

      code = "0";
      msg = "成功";
    } catch (Exception ex) {
      code = "900";
      msg = "处理异常,请重试";
      //oLogger_.error(ex, ex);
    }

    return "{\"code\":\"" + code + "\", \"msg\":\"" + msg + "\"}";
  }

  /**
   * 登录检查
   *
   * @return
   */
  private String doCheckLogin() {
    String code = "";
    String msg = "";

    /*String sPosUrl = emisUtil.parseString(req.getFirst("sPosUrl"));
    String sNo = emisUtil.parseString(req.getFirst("sNo"));
    String idNo = emisUtil.parseString(req.getFirst("idNo"));
    String uId = emisUtil.parseString(req.getFirst("uId"));
    String uPwd = emisUtil.parseString(req.getFirst("uPwd"));

    try {
      emisProp prop = emisProp.getInstance(context_);
      String SME_URL = prop.get("SME_URL");
      String S_NO = prop.get("S_NO");
      String ID_NO = prop.get("ID_NO");
      String POS_USERPWD = prop.get("POS_USERPWD");
      String POS_USERID = prop.get("POS_USERID");
      String BM_VERSION = prop.get("BM_VERSION");

      HttpClient _oClient = null;
      int _iStatus = 0;
      Response resp = null;
      String respBody = "";
      try {
        _oClient = new HttpClient();
        _oClient.setConnectionTimeout(180000);
        _oClient.setTimeout(180000);
        PostMethod method = new PostMethod(sPosUrl + "/ws/wechatV3/bm/checkLogin");
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        method.addParameter("sNo", sNo);
        method.addParameter("idNo", idNo);
        method.addParameter("uId", uId);
        method.addParameter("uPwd", uPwd);
        method.addParameter("bmVersion", BM_VERSION);

        for (MultivaluedMap.Entry<String, List<String>> entry : req.entrySet()) {
          List ls = entry.getValue();
          method.addParameter(entry.getKey(), ls.get(0).toString());
        }
        _iStatus = _oClient.executeMethod(method);
        resp = Response.ok(method.getResponseBodyAsString(), MediaType.APPLICATION_JSON).build();
        respBody = method.getResponseBodyAsString();
      } catch (Exception e) {
        oLogger_.error(e, e);
      }

      if (resp != null) {
        oLogger_.warn("resp has data");
      } else {
        oLogger_.warn("resp error");
      }

      oLogger_.warn(respBody);
      JSONObject posResp = null;
      if (respBody == null || "".equals(respBody)) {
        return "{\"code\":\"800\",\"msg\":\"后台连线异常，请重试。\"}";
      } else {
        if (!emisUtil.isJSON(respBody.trim())) {
          return "{\"code\":\"801\",\"msg\":\"后台连线异常，请重试。\"}";
        } else {
          posResp = JSONObject.fromObject(respBody);
          if (posResp == null || posResp.isEmpty()) {
            return "{\"code\":\"802\",\"msg\":\"后台连线异常，请重试。\"}";
          } else {
            String posResp_code = getJsonString(posResp, "code");
            if (!"0".equals(posResp_code) && !"00".equals(posResp_code)) {
              return respBody;
            }
          }
        }
      }

      boolean firstLogin = false;
      if ("".equals(SME_URL) || "".equals(S_NO) || "".equals(ID_NO) || "".equals(POS_USERPWD) || "".equals(POS_USERID)) {
        // 系统参数不完整，不执行后续动作。
        firstLogin = true;
      }

      updEmisprop(sPosUrl, sNo, idNo, uId, uPwd);

      emisProp.reload(context_);

      if (firstLogin) {
        try {
          // 首次登录成功，立即调用下载排程
          if (emisScheduleMgr.getInstance(context_).isExists("emisBMDownload")) {
            ClassLoader oClassLoader = Thread.currentThread().getContextClassLoader();
            Class obj = oClassLoader.loadClass("com.emis.schedule.epos.bm.emisBMDownload");
            emisTask task = (emisTask) (obj).newInstance();
            task.setName("emisBMDownload");
            task.setContext(context_);
            Thread t = new Thread(task);
            t.start();
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      } else {
        cleanData();
      }

      code = "0";
      msg = "成功";
    } catch (Exception ex) {
      code = "900";
      msg = "处理异常,请重试";
      oLogger_.error(ex, ex);
    }*/

    return "{\"code\":\"" + code + "\", \"msg\":\"" + msg + "\"}";
  }

  private String getJsonString(JSONObject jsonObj, String data) {
    String sReturn = "";
    try {
      if (jsonObj.has(data)) {
        sReturn = jsonObj.getString(data);
      }
    } catch (Exception ex) {
      sReturn = "";
      ex.printStackTrace();
    }
    return sReturn;
  }

  private boolean updEmisprop(String sPosUrl, String sNo, String idNo, String uId, String uPwd) {
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement updEmispropStmt = null;
    SQLiteStatement insEmispropStmt = null;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String today = emisUtil.todayDateAD();
      updEmispropStmt = oDataSrc_.prepareStmt("update emisprop set VALUE = ?, UPD_DATE = ? where NAME = ?");
      insEmispropStmt = oDataSrc_.prepareStmt("insert into emisprop (NAME, VALUE, UPD_DATE) values (?, ?, ?)");

      // SME_URL
      try {
        updEmispropStmt.clearBindings();
        updEmispropStmt.bindString(1, sPosUrl);
        updEmispropStmt.bindString(2, today);
        updEmispropStmt.bindString(3, "SME_URL");
        if (updEmispropStmt.executeUpdateDelete() <= 0) {
          insEmispropStmt.clearBindings();
          insEmispropStmt.bindString(1, "SME_URL");
          insEmispropStmt.bindString(2, sPosUrl);
          insEmispropStmt.bindString(3, today);
          insEmispropStmt.executeInsert();
        }
        oDataSrc_.setTransactionSuccessful();
      } catch (Exception ex) {
        //oLogger_.error("update SME_URL error");
      }

      // S_NO
      try {
        updEmispropStmt.clearBindings();
        updEmispropStmt.bindString(1, sNo);
        updEmispropStmt.bindString(2, today);
        updEmispropStmt.bindString(3, "S_NO");
        if (updEmispropStmt.executeUpdateDelete() <= 0) {
          insEmispropStmt.clearBindings();
          insEmispropStmt.bindString(1, "S_NO");
          insEmispropStmt.bindString(2, sNo);
          insEmispropStmt.bindString(3, today);
          insEmispropStmt.executeInsert();
        }
        oDataSrc_.setTransactionSuccessful();
      } catch (Exception ex) {
        //oLogger_.error("update SME_URL error");
      }

      // ID_NO
      try {
        updEmispropStmt.clearBindings();
        updEmispropStmt.bindString(1, idNo);
        updEmispropStmt.bindString(2, today);
        updEmispropStmt.bindString(3, "ID_NO");
        if (updEmispropStmt.executeUpdateDelete() <= 0) {
          insEmispropStmt.clearBindings();
          insEmispropStmt.bindString(1, "ID_NO");
          insEmispropStmt.bindString(2, idNo);
          insEmispropStmt.bindString(3, today);
          insEmispropStmt.executeInsert();
        }
        oDataSrc_.setTransactionSuccessful();
      } catch (Exception ex) {
        //oLogger_.error("update SME_URL error");
      }

      // POS_USERID
      try {
        updEmispropStmt.clearBindings();
        updEmispropStmt.bindString(1, uId);
        updEmispropStmt.bindString(2, today);
        updEmispropStmt.bindString(3, "POS_USERID");
        if (updEmispropStmt.executeUpdateDelete() <= 0) {
          insEmispropStmt.clearBindings();
          insEmispropStmt.bindString(1, "POS_USERID");
          insEmispropStmt.bindString(2, uId);
          insEmispropStmt.bindString(3, today);
          insEmispropStmt.executeInsert();
        }
        oDataSrc_.setTransactionSuccessful();
      } catch (Exception ex) {
        //oLogger_.error("update SME_URL error");
      }

      // POS_USERPWD
      try {
        updEmispropStmt.clearBindings();
        updEmispropStmt.bindString(1, uPwd);
        updEmispropStmt.bindString(2, today);
        updEmispropStmt.bindString(3, "POS_USERPWD");
        if (updEmispropStmt.executeUpdateDelete() <= 0) {
          insEmispropStmt.clearBindings();
          insEmispropStmt.bindString(1, "POS_USERPWD");
          insEmispropStmt.bindString(2, uPwd);
          insEmispropStmt.bindString(3, today);
          insEmispropStmt.executeInsert();
        }
        oDataSrc_.setTransactionSuccessful();
      } catch (Exception ex) {
        //oLogger_.error("update SME_URL error");
      }
    } catch (Exception ex) {
      //oLogger_.error(ex, ex);
    } finally {
      if (updEmispropStmt != null) {
        updEmispropStmt.close();
        updEmispropStmt = null;
      }
      if (insEmispropStmt != null) {
        insEmispropStmt.close();
        insEmispropStmt = null;
      }
      if (oDataSrc_ != null) {
        oDataSrc_.endTransaction();
        oDataSrc_.close();
        oDataSrc_ = null;
      }
    }

    return true;
  }

  /**
   * 删除旧交易资料 (30天前)
   */
  private void cleanData() {
    emisSQLiteWrapper oDataSrc_ = null;
    SQLiteStatement delSaleOrderPkWxStmt = null;
    SQLiteStatement delSaleOrderDisStmt = null;
    SQLiteStatement delSaleOrderWmStmt = null;
    SQLiteStatement delSaleOrderDStmt = null;
    SQLiteStatement delSaleOrderHStmt = null;
    try {
      oDataSrc_ = emisDb.getInstance();
      oDataSrc_.beginTransaction();

      String delDay = (new emisDate()).addDay(-31).toString(true);
      delSaleOrderPkWxStmt = oDataSrc_.prepareStmt("delete from sale_order_pk_wx where SL_DATE <= ?");
      delSaleOrderDisStmt = oDataSrc_.prepareStmt("delete from sale_order_dis where SL_DATE <= ?");
      delSaleOrderWmStmt = oDataSrc_.prepareStmt("delete from sale_order_wm where SL_DATE <= ?");
      delSaleOrderDStmt = oDataSrc_.prepareStmt("delete from sale_order_d where SL_DATE <= ?");
      delSaleOrderHStmt = oDataSrc_.prepareStmt("delete from sale_order_h where SL_DATE <= ?");

      try {
        delSaleOrderPkWxStmt.clearBindings();
        delSaleOrderPkWxStmt.bindString(1, delDay);
        delSaleOrderPkWxStmt.executeUpdateDelete();
        oDataSrc_.setTransactionSuccessful();
      } catch (Exception ex) {
        //oLogger_.error("update sale_order_pk_wx error");
        //oLogger_.error(ex.getMessage());
      }

      try {
        delSaleOrderDisStmt.clearBindings();
        delSaleOrderDisStmt.bindString(1, delDay);
        delSaleOrderDisStmt.executeUpdateDelete();
        oDataSrc_.setTransactionSuccessful();
      } catch (Exception ex) {
        //oLogger_.error("update sale_order_dis error");
        //oLogger_.error(ex.getMessage());
      }

      try {
        delSaleOrderWmStmt.clearBindings();
        delSaleOrderWmStmt.bindString(1, delDay);
        delSaleOrderWmStmt.executeUpdateDelete();
        oDataSrc_.setTransactionSuccessful();
      } catch (Exception ex) {
        //oLogger_.error("update sale_order_wm error");
        //oLogger_.error(ex.getMessage());
      }

      try {
        delSaleOrderDStmt.clearBindings();
        delSaleOrderDStmt.bindString(1, delDay);
        delSaleOrderDStmt.executeUpdateDelete();
        oDataSrc_.setTransactionSuccessful();
      } catch (Exception ex) {
        //oLogger_.error("update sale_order_d error");
        //oLogger_.error(ex.getMessage());
      }

      try {
        delSaleOrderHStmt.clearBindings();
        delSaleOrderHStmt.bindString(1, delDay);
        delSaleOrderHStmt.executeUpdateDelete();
        oDataSrc_.setTransactionSuccessful();
      } catch (Exception ex) {
        //oLogger_.error("update sale_order_h error");
        //oLogger_.error(ex.getMessage());
      }

    } catch (Exception ex) {
      //oLogger_.error(ex, ex);
    } finally {
      if (delSaleOrderPkWxStmt != null) {
        delSaleOrderPkWxStmt.close();
        delSaleOrderPkWxStmt = null;
      }
      if (delSaleOrderDisStmt != null) {
        delSaleOrderDisStmt.close();
        delSaleOrderDisStmt = null;
      }
      if (delSaleOrderWmStmt != null) {
        delSaleOrderWmStmt.close();
        delSaleOrderWmStmt = null;
      }
      if (delSaleOrderDStmt != null) {
        delSaleOrderDStmt.close();
        delSaleOrderDStmt = null;
      }
      if (delSaleOrderHStmt != null) {
        delSaleOrderHStmt.close();
        delSaleOrderHStmt = null;
      }
      if (oDataSrc_ != null) {
        oDataSrc_.endTransaction();
        oDataSrc_.close();
        oDataSrc_ = null;
      }
    }
  }
}