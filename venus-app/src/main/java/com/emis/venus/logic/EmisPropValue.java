package com.emis.venus.logic;

import com.emis.venus.common.emisKeeper;
import com.emis.venus.util.emisUtil;

/**
 * 系统参数取值方法<br>
 * 唯一的参数，全部机台一样,emisprop表中：EmisPropValue.getUniqueValueBol("IS_USE_PRT");<br>
 **/
public class EmisPropValue {
  //------------------------统一(唯一)系统参数 --emisprop表--------------

  /**
   * 取统一(唯一)系统参数值（对象）
   *
   * @param varName
   * @return
   */
  public static Object getUniqueValue(String varName, String def) {
    if (emisKeeper.getInstance().getEmisPropMap() != null) {
      if (!emisKeeper.getInstance().getEmisPropMap().containsKey(varName)) {
        return def;
      }
      return emisKeeper.getInstance().getEmisPropMap().get(varName);
    }
    return def;
  }

  /**
   * 取统一(唯一)系统参数值（对象）
   *
   * @param varName
   * @return
   */
  public static void setUniqueValue(String varName, String val) {
    if (emisKeeper.getInstance().getEmisPropMap() != null) {
      emisKeeper.getInstance().getEmisPropMap().put(varName, val);
    }
  }

  /**
   * 取统一(唯一)系统参数值（对象）
   *
   * @param varName
   * @return
   */
  public static Object getUniqueValue(String varName) {
    return getUniqueValue(varName, "");
  }

  /**
   * 取统一(唯一)系统参数值（字符串）
   *
   * @param varName
   * @return
   */
  public static String getUniqueValueStr(String varName) {
    return emisUtil.parseString(getUniqueValue(varName));
  }

  /**
   * 取统一(唯一)系统参数值（字符串）
   *
   * @param varName
   * @return
   */
  public static String getUniqueValueStr(String varName, String def) {
    return emisUtil.parseString(getUniqueValue(varName, def));
  }

  /**
   * 取统一(唯一)系统参数值（int）
   *
   * @param varName
   * @return
   */
  public static int getUniqueValueInt(String varName) {
    return emisUtil.parseInt(getUniqueValue(varName));
  }

  /**
   * 取统一(唯一)系统参数值（int）
   *
   * @param varName
   * @return
   */
  public static int getUniqueValueInt(String varName, int def) {
    return emisUtil.parseInt(getUniqueValue(varName, def + ""));
  }

  /**
   * 取统一(唯一)系统参数值(布尔值)
   *
   * @param varName
   * @return
   */
  public static boolean getUniqueValueBol(String varName) {
    return emisUtil.parseBoolean(getUniqueValue(varName));
  }

  /**
   * 取统一(唯一)系统参数值(布尔值)
   *
   * @param varName
   * @return
   */
  public static boolean getUniqueValueBol(String varName, boolean def) {
    return emisUtil.parseBoolean(getUniqueValue(varName, def + ""));
  }

  /**
   * 取统一(唯一)系统参数值（double）
   *
   * @param varName
   * @return
   */
  public static double getUniqueValueDouble(String varName) {
    return emisUtil.parseDefaultDouble(getUniqueValue(varName), 0);
  }

  /**
   * 取统一(唯一)系统参数值（double）
   *
   * @param varName
   * @return
   */
  public static double getUniqueValueDouble(String varName, double def) {
    return emisUtil.parseDefaultDouble(getUniqueValue(varName, def + ""), def);
  }
}
