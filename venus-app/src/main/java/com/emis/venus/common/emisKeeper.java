package com.emis.venus.common;

import android.content.Context;
import com.emis.venus.entity.BillDEntity;
import com.emis.venus.entity.Entity;
import com.emis.venus.entity.SettingEntity;
import com.emis.venus.util.emisPropUtil;
import com.emis.venus.util.log4j.LogKit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

/**
 * 单例模式。用来保存<br>
 * 添加的一些对象，记得在close()方法里销毁
 */
public class emisKeeper extends Observable {
  private volatile static emisKeeper emisKeeper = null;

  public static emisKeeper getInstance() {
    if (emisKeeper == null) {
      synchronized (emisKeeper.class) {
        if (emisKeeper == null) {
          LogKit.info("程序启动===emisKeeper is null===");
          emisKeeper = new emisKeeper();
        }
      }
    }
    return emisKeeper;
  }

  private emisKeeper() {
    init();
  }

  protected void init() {
    emisPropMap = new HashMap();
  }

  /**
   * 系统参数(放最开始)
   */
  private HashMap emisPropMap = null;
  /**
   * 登录的使用者
   */
  private Entity employ = null;

  private boolean isClear, isLogin;
  private SettingEntity oSettingEntity;
  private OkHttpClient okClient = null;


  private ArrayList<BillDEntity> aBillDEntity;


  /**
   * @param emisPropMap the emisPropMap to set
   */
  public void setEmisPropMap(HashMap emisPropMap) {
    if (this.emisPropMap != null) {
      this.emisPropMap.clear();
      //this.emisPropMap = null;//不能给null,会导致其他地方值丢失，指针地址改变了。
      this.emisPropMap.putAll(emisPropMap);
    } else {
      this.emisPropMap = emisPropMap;
    }
  }

  /**
   * @return the emisPropMap
   */
  public HashMap getEmisPropMap() {
    if (emisPropMap == null || emisPropMap.isEmpty()) {
      emisPropMap = emisPropUtil.getPropMapfromEntity();
    }
    return emisPropMap;
  }

  public Entity getEmploy() {
    return employ;
  }

  public void setEmploy(Entity employ) {
    this.employ = employ;
  }

  public OkHttpClient getOkClient() {
    if (okClient == null) {
      okClient = new OkHttpClient().newBuilder().sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
        .hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();
    }
    return okClient;
  }

  public String getsInfo(String key, Context context) {

    if (oSettingEntity == null) oSettingEntity = new SettingEntity(context);

    return oSettingEntity.getStrValue(key);
  }

  public Boolean getbInfo(String key, Context context) {

    if (oSettingEntity == null) oSettingEntity = new SettingEntity(context);

    return oSettingEntity.getBooleanValue(key);
  }

  public boolean ifLogin() {
    return isLogin;
  }

  public boolean ifClear() {
    return isClear;
  }

  public void refreshInfo(Context context) {
    oSettingEntity = new SettingEntity(context);
  }

  public boolean setsInfo(String key, String value, Context context) {

    return oSettingEntity.setStrValue(key, value, context);
  }

  public boolean setbInfo(String key, boolean value, Context context) {

    return oSettingEntity.setBooleanValue(key, value, context);
  }

  public void setLogin(boolean ifLogin) {
    isLogin = ifLogin;
  }

  public void setClear(boolean ifClear) {
    isClear = ifClear;
  }

  public ArrayList<BillDEntity> getBillD() {
    if (aBillDEntity == null) {
      aBillDEntity = new ArrayList<BillDEntity>();
    }
    return aBillDEntity;
  }

  public void setBillD(ArrayList<BillDEntity> saleD) {
    aBillDEntity = saleD;
  }

}
