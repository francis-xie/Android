package com.emis.venus.logic;

import com.emis.venus.entity.Entity;
import com.emis.venus.util.emisUtil;
import com.emis.venus.util.log4j.LogKit;

import java.util.HashMap;

/**
 * 使用者 查询<br>
 * 检核是否非收银员
 */
public class Employ extends Entity {
  public static final String SUPPORTONLY = "__SUPPORT_ONLY__";
  public static final String SUPPORT = "emis";
  public static final String ADMIN = "admin";
  public static final String ADMIN_NAME = "POS管理员"; //POS管理员
  public static final String DEF_PWD = "emisvenus";
  /**
   * 是否签到退登录检查
   */
  private boolean isDuty = false;
  // 错误信息
  private String errorMessage = null;

  public Employ() throws Exception {
    super("CASHIER");
  }

  /**
   * 用帐号和密码登陆
   *
   * @param uid
   * @param pwd
   * @return
   */
  public boolean checkLogin(String uid, String pwd) {
    return checkLogin(uid, pwd, false);
  }

  /**
   * 用帐号和密码登陆
   *
   * @param uid
   * @param pwd
   * @return
   */
  public boolean checkLogin(String uid, String pwd, boolean isAutoLogin) {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("OP_NO", uid);
    errorMessage = null; // 重置错误信息
    try {
      boolean isOK;
      // 2016/06/16 Joe Add 增加代理管理员可用数字账号
      // ADMIN 系统管理员的代理账号，可由后台设置参数下传，亦可在前台透过FixDb来设定，不开发界面设定
      // 当代理账号不为空时，原内置的ADMIN失效，必需要有代理账号才可以登录为 系统管理员
      String sProxyId = EmisPropValue.getUniqueValueStr("POS_PROXY_ADMIN_ID");
      // 若设置了代理账号的密码则不是依默认的日期YYYYMMDD为密码
      String sProxyPwd = EmisPropValue.getUniqueValueStr("POS_PROXY_ADMIN_PWD");

      if (Employ.SUPPORT.equalsIgnoreCase(uid)) {
        this.load(map);
        this.cleanData();
        this.createNewRow();
        this.setString("OP_NO", Employ.SUPPORT);
        this.setString("OP_NAME", Employ.SUPPORT);
        this.first();
        //星期+日期+月（十六进制）+小时（十六进制）
        String curDate = emisUtil.getSYSWEEK() + emisUtil.getSYSDATE()
          + Integer.toHexString(emisUtil.parseInt(emisUtil
          .getDATE("MM")))
          + Integer.toHexString(emisUtil.parseInt(emisUtil
          .getDATE("hh")));
        isOK = (curDate.equalsIgnoreCase(pwd)) || isAutoLogin;
      } else if (("".equals(sProxyId.trim()) && Employ.ADMIN.equalsIgnoreCase(uid))
        || (!"".equals(sProxyId.trim()) && isAdminUser(uid) && sProxyId.equals(uid))) {
        this.load(map);
        this.cleanData();
        this.createNewRow();
        this.setString("OP_NO", Employ.ADMIN);
        this.setString("OP_NAME", Employ.ADMIN_NAME); // POS管理员
        this.first();
        String curDate = "".equals(sProxyPwd) ? emisUtil.getSYSDATE() : sProxyPwd;
        isOK = (curDate.equals(pwd)) || isAutoLogin;
      } else {
        //自动登陆不需要传密码
        if (!isAutoLogin) {
          map.put("OP_PASSWORD", pwd);
        }
        // boolean IS_ACC = emisKeeper.getInstance().IS_ACC();
        // if (IS_ACC) {
        // map.put("IS_ACC", "Y");
        // }

        isOK = this.load(map);
        if (isOK) {
          isOK = checkOP_DWN();
        }
      }
      return isOK;
    } catch (Exception e) {
      LogKit.error(e, e);
      return false;
    }
  }

  /***
   *
   * @return
   */
  private boolean checkOP_DWN() {
    this.first();
    // 退登录检查 ,直接返回
    if (isDuty) {
      return true;
    }
    if (Employ.SUPPORT.equalsIgnoreCase(this.getString("OP_NO"))
      || Employ.ADMIN.equalsIgnoreCase(this.getString("OP_NO"))) {
      return true;
    }
    String S_NO = null;
    String sysS_NO = EmisPropValue.getUniqueValueStr("S_NO");

    // 兼容集集同账号多门店情况,即同一个账号可登录多个门店又有限制可登录门店，如有门店为空即不限制门店
    do {
      S_NO = this.getString("S_NO");
      if (sysS_NO.equals(S_NO) || "".equals(S_NO)) {
        break;
      }
    } while (this.next());
    this.first();

    if ("".equals(S_NO)) {// 所属门店为空，可以登录
      return true;
    }

    if ("N".equals(this.getString("OP_DWN"))) { //$NON-NLS-2$
      errorMessage = "非收银员不能登入!";// 非收银员不能登入!
      return false;
    }
    if (!sysS_NO.equals(S_NO)) {
      if (emisUtil.isCardFinacialMode()) {
        errorMessage = "非本财务部人员不能登录!";// 非本财务部人员不能登录!
      } else if (emisUtil.isCardServiceMode()) {
        errorMessage = "非本服务台人员不能登录!";// 非本服务台人员不能登录!
      } else if (emisUtil.isFoodCourtMode()) {
        errorMessage = "非本档口人员不能登录!";// 非本档口人员不能登录!
      } else {
        errorMessage = "非本门店人员不能登录!";// 非本门店人员不能登录!
      }
      return false;
    }
    return true;
  }

  /**
   * 默认账户登录：密码为：emisvenus即可
   *
   * @param uid
   * @param pwd
   * @return
   */
  public boolean checkDefaultLogin(String uid, String pwd) {
    try {
      if (Employ.DEF_PWD.equalsIgnoreCase(pwd)) {
        this.createNewRow();
        this.setString("OP_NO", "DEFAULT"); //$NON-NLS-2$
        this.setString("OP_NAME", "DEFAULT"); //$NON-NLS-2$
        return true;
      }
      return false;
    } catch (Exception e) {
      LogKit.error(e, e);
      return false;
    }
  }

  /**
   * 用帐号和密码登陆签到退
   *
   * @param uid
   * @param pwd
   * @param isDuty
   * @return
   */
  public boolean checkLoginDuty(String uid, String pwd, boolean isDuty) {
    boolean isOk = checkLogin(uid, pwd, false);
    if (isOk && isDuty) {
      // 用账号登陆时，将把卡号移除：
      this.setString("CSHER_CARD", ""); //$NON-NLS-2$
    }
    return isOk;
  }

  /**
   * 取得姓名
   *
   * @param uid
   * @return
   */
  public String getOP_NAME(String uid) {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("OP_NO", uid);
    try {
      this.load(map);
      this.first();
      if (emisUtil.isEmpty(this)) {
        if (ADMIN.equalsIgnoreCase(uid)) {
          this.createNewRow();
          this.setString("OP_NO", ADMIN);
          this.setString("OP_NAME", ADMIN_NAME);
          this.first();
        } else if (SUPPORT.equalsIgnoreCase(uid)) {
          this.createNewRow();
          this.setString("OP_NO", SUPPORT);
          this.setString("OP_NAME", SUPPORT);
          this.first();
        }
      }
      return this.getString("OP_NAME");
    } catch (Exception e) {
      LogKit.error(e, e);
    }
    return "";
  }

  public void setDuty(boolean isDuty) {
    this.isDuty = isDuty;
  }

  public boolean isDuty() {
    return isDuty;
  }

  /**
   * 是否为POS系统管理员（即非emis和普通账号），用于checkLogin时检查
   *
   * @param id
   * @return
   */
  private boolean isAdminUser(String id) {
    if (SUPPORT.equalsIgnoreCase(id))
      return false;
    boolean ret = false;
    Entity oTmp = new Entity(this.getM_sTableName());
    try {
      oTmp.loadBySql("select OP_NO from CASHIER where OP_NO=? FETCH FIRST 1 ROWS ONLY",
        new String[]{id});
      ret = (oTmp != null && oTmp.getM_rowCount() == 0);
    } catch (Exception e) {
      LogKit.error("check login user weather super user faild", e);
    } finally {
      oTmp.cleanData();
      oTmp = null;
    }
    return ret;
  }

  // 获取错误信息
  public String getErrorMessage() {
    return errorMessage;
  }
}
