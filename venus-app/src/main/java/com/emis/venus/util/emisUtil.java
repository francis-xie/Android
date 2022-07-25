package com.emis.venus.util;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.ArrayMap;
import android.util.Log;
import com.emis.venus.R;
import com.emis.venus.entity.Entity;
import com.emis.venus.util.log4j.LogKit;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.nio.channels.FileLock;
import java.security.MessageDigest;
import java.text.*;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公用程式
 */
public class emisUtil {
  private static DecimalFormat sdf_pos0 = new DecimalFormat("#");
  private static DecimalFormat sdf_pos1 = new DecimalFormat("#0.0");
  private static DecimalFormat sdf_pos2 = new DecimalFormat("#0.00");
  private static DecimalFormat sdf_pos3 = new DecimalFormat("#0.000");
  private static DecimalFormat sdf_pos4 = new DecimalFormat("#0.0000");

  public static SimpleDateFormat df_yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
  public static SimpleDateFormat df_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
  public static SimpleDateFormat df_yyyyMMddHHmm = new SimpleDateFormat("yyyyMMddHHmm");
  public static SimpleDateFormat df_yyyyMMddHH = new SimpleDateFormat("yyyy/MM/dd HH:mm");
  public static SimpleDateFormat df_yyyy_MM_ddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  public static SimpleDateFormat df_yyyy_MM_ddHHmmssE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
  public static SimpleDateFormat df_HHmmss = new SimpleDateFormat("HHmmss");
  public static SimpleDateFormat df_HH_mm_ss = new SimpleDateFormat("HH:mm:ss");
  public static SimpleDateFormat df_yyMMddHHmmss = new SimpleDateFormat("yyMMddHHmmss");
  public static SimpleDateFormat df_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
  public static SimpleDateFormat df_yyyyMMddP = new SimpleDateFormat("yyyMMdd");
  public static SimpleDateFormat df_HHmmssSSS = new SimpleDateFormat("HHmmssSSS");
  public static SimpleDateFormat df_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
  public static SimpleDateFormat df_MM_dd = new SimpleDateFormat("MM/dd");

  public static String getSYSMMDD() {
    return df_MM_dd.format(new Date());
  }

  /**
   * 返回boolean
   *
   * @param obj
   * @return
   */
  public static boolean parseBoolean(Object obj) {
    return "true".equalsIgnoreCase(parseString(obj)) || "Y".equalsIgnoreCase(parseString(obj)); //$NON-NLS-2$
  }

  /**
   * 返回boolean
   *
   * @param obj
   * @return
   */
  public static boolean parseBoolean(Object obj, boolean defaultValue) {
    return "true".equalsIgnoreCase(parseString(obj, defaultValue + "")) || "Y".equalsIgnoreCase(parseString(obj, defaultValue + "")); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
  }

  /**
   * 把Object转换成String，如Object==null 返回默认值
   *
   * @param obj
   * @param defaultValue
   * @return
   */
  public static String parseString(Object obj, String defaultValue) {
    if (obj == null)
      return defaultValue;
    else
      return String.valueOf(obj);
  }

  /**
   * 把Object转换成String，如Object==null 返回""
   *
   * @param obj
   * @return
   */
  public static String parseString(Object obj) {
    return parseString(obj, "");
  }

  /**
   * 处理特殊的。把Object转换成String，如Object==null 返回"" 并且如果有小数，把多余小数位移除
   *
   * @param obj
   * @return
   */
  public static String parseSpeString(Object obj) {
    String str = parseString(obj, "");
    try {
      // 如果是整数，直接返回
      if (isDecimal(str)) {
        Double d = new Double(str);
        int iValue = d.intValue();
        if (iValue == d.doubleValue()) {
          d = null;
          return iValue + "";
        }
        d = null;
      }
    } catch (Exception e) {
    }
    return str;
  }

  public static boolean isNumeric(String str) {
    Pattern pattern = Pattern.compile("[0-9]*");
    return pattern.matcher(str).matches();
  }

  /**
   * 判断是否为浮点数
   *
   * @param str
   * @return
   */
  public static boolean isDecimal(String str) {
    Pattern pattern = Pattern.compile("^[-]?[0-9]+(\\.)[0-9]+$");
    Matcher isNum = pattern.matcher(str);
    return isNum.matches();
  }

  /**
   * 把Object转换成float，如Object==null 返回默认值
   *
   * @param obj
   * @param defaultValue
   * @return
   */
  public static float parseFloat(Object obj, float defaultValue) {
    // 如果是整数，直接返回
    try {
      Double d = new Double(parseString(obj, String.valueOf(0)));
      int iValue = d.intValue();
      if (iValue == d.doubleValue()) {
        d = null;
        return iValue;
      }
      d = null;
    } catch (Exception e) {
      // TODO: handle exception
    }
    float value = defaultValue;
    try {
      value = Float.parseFloat(parseString(obj, String
        .valueOf(defaultValue)));
    } catch (Exception ex) {
      value = defaultValue;
    }
    return value;
  }

  /**
   * 把Object转换成float，如Object==null 返回0
   *
   * @param obj
   * @return
   */
  public static float parseFloat(Object obj) {
    return parseFloat(obj, 0);
  }

  /**
   * 把Object转换成int，如Object==null 返回默认值
   *
   * @param obj
   * @param defaultValue
   * @return
   */
  public static int parseInt(Object obj, int defaultValue) {
    int value = defaultValue;
    try {
      // 转成Double，然后取整数
      Double d = new Double(
        parseString(obj, String.valueOf(defaultValue)));
      value = d.intValue();
      d = null;
    } catch (Exception ex) {
      value = defaultValue;
    }
    return value;
  }

  /**
   * 把Object转换成int，如Object==null 返回0
   *
   * @param obj
   * @return
   */
  public static int parseInt(Object obj) {
    return parseInt(obj, 0);
  }

  /**
   * 把Object转换成long，如Object==null 返回默认值
   *
   * @param obj
   * @param defaultValue
   * @return
   */
  public static long parseLong(Object obj, int defaultValue) {
    long value = defaultValue;
    try {
      value = Long.parseLong(parseString(obj, String
        .valueOf(defaultValue)));
    } catch (Exception ex) {
      value = defaultValue;
    }
    return value;
  }

  /**
   * 把Object转换成int，如Object==null 返回0
   *
   * @param obj
   * @return
   */
  public static long parseLong(Object obj) {
    return parseLong(obj, 0);
  }

  /**
   * 把Object转换成double，如Object==null 返回0
   *
   * @param obj
   * @return
   */
  public static double parseDouble(Object obj) {
    return parseDouble(obj, 0);
  }

  /**
   * 得到json的值
   *
   * @param _oJson
   * @param jsonKey
   * @return
   */
  public static String parseJSONString(JSONObject _oJson, String jsonKey) {
    if (_oJson == null) {
      return "";
    }
    if (_oJson.containsKey(jsonKey)) {
      return parseString(_oJson.getString(jsonKey));
    }
    return "";
  }

  /**
   * 判断字符是否为全角英数
   *
   * @param str
   * @return true是、false不是
   */
  public static boolean isFullChar(String str) {
    if (str == null || "".equals(str) || str.length() == 0) {
      return false;
    }
    try {
      if (str.length() != str.getBytes("UTF-8").length) {
        // 通过字节长度判断全半角，半角英数占1byte、全角英数根据编码不同占2/3byte
        return true;
      }
    } catch (UnsupportedEncodingException e) {
      LogKit.error(e, e);
    }
    LogKit.error("[ERR_CODE][9001]");
    return false;
  }

  /**
   * 释放空闲内存
   */
  public static void fullGC() {
    /**
     * 正在交易中，和进行转档时，不处理
     */
    /*if (SaleUtil.isHadSale() || emisKeeper.getInstance().isTurnIning()) {
      return;
    }*/
    fullGCRandom();
  }

  /**
   * 释放空闲内存，计数（每5次执行1次）,出异常后不再继续执行
   */
  public static int FULLGC_CNT = 0;

  /**
   * 释放空闲内存
   */
  public static void fullGCRandom() {
    if (FULLGC_CNT < 0) {
      return;
    }
    try {
      FULLGC_CNT++;
      if (FULLGC_CNT == 3) {
        FULLGC_CNT = 0;
      } else {
        return;
      }
      fullGCNoCheck();
    } catch (Exception e) {
      FULLGC_CNT = 0;
      LogKit.error("freeMemory Error", e);
    }

  }

  /**
   * 提示异常信息
   *
   * @param sError
   */
  public static void errorMsg(String sError) {
  }

  /**
   * 释放空闲内存
   */
  public static void fullGCNoCheck() {
    try {
      if (FULLGC_CNT < 0) {
        return;
      }
      LogKit.info("fullGCNoCheck>>>>>>>>>>>>>");
      Runtime rt = Runtime.getRuntime();
      long isFree = rt.freeMemory();
      long wasFree;
      do {
        wasFree = isFree;
        rt.runFinalization();
        rt.gc();
        isFree = rt.freeMemory();
      } while (isFree > wasFree);
      FULLGC_CNT = 0;
      LogKit.info("FREEMEMORY>>>>>>>>>>>>>" + isFree);
    } catch (Exception e) {
      FULLGC_CNT = 0;
      LogKit.error("freeMemory Error", e);
      errorMsg(e.getMessage());
    }
  }

  /**
   * 按指定格式，格式化当前日期
   *
   * @param formatStr
   * @return
   */
  public static String getDATE(String formatStr) {
    Date date = new Date();
    return new SimpleDateFormat(formatStr).format(date);
  }

  /**
   * 按指定格式，格式化传入的日期
   *
   * @param formatStr
   * @param date
   * @return
   */
  public static String getDATE(String formatStr, Date date) {
    return new SimpleDateFormat(formatStr).format(date);
  }

  /**
   * 取当前的日期返回yyyyMMddHHmmss格式字符串
   *
   * @return
   */
  public static String getSYSDATETIME() {
    Date date = new Date();
    return df_yyyyMMddHHmmss.format(date);
  }

  /**
   * 取当前的日期返回yyyyMMddHHmmssSSS格式字符串
   *
   * @return
   */
  public static String getSYSDATETIMEDETAIL() {
    Date date = new Date();
    return df_yyyyMMddHHmmssSSS.format(date);
  }

  /**
   * 取当前的日期返回yyyy-MM-dd HH:mm:ss格式字符串
   *
   * @return
   */
  public static String getSYSDATETIME2() {
    Date date = new Date();
    return df_yyyy_MM_ddHHmmss.format(date);
  }

  /**
   * 传入yyyyMMddHHmmss格式字符串，返回yyyy-MM-dd HH:mm:ss格式字符串
   *
   * @return
   */
  public static String getSYSDATETIME2(String dateTime) {
    if ("".equals(parseString(dateTime))) {
      return "";
    }
    Date date = null;
    try {
      date = df_yyyyMMddHHmmss.parse(dateTime.replace(" ", "").trim());
      return df_yyyy_MM_ddHHmmss.format(date);
    } catch (ParseException e) {
      LogKit.error(e);
    }
    return dateTime;
  }

  /**
   * 取当前的日期返回yyyyMMddHHmm格式字符串
   *
   * @return
   */
  public static String getSYSDATETIME3() {
    Date date = new Date();
    return df_yyyyMMddHHmm.format(date);
  }

  /**
   * 取long的日期返回yyyyMMddHHmm格式字符串
   *
   * @param lDate
   * @return
   */
  public static String getSYSDATETIME3(long lDate) {
    Date date = null;
    try {
      date = new Date(lDate);
      return df_yyyyMMddHHmm.format(date);
    } catch (Exception e) {
      LogKit.error(e);
    }
    return "";
  }

  /**
   * 取long的日期返回yyyy/MM/dd HH:mm格式字符串
   *
   * @param lDate
   * @return
   */
  public static String getSYSDATETIME4(long lDate) {
    Date date = null;
    try {
      date = new Date(lDate);
      return df_yyyyMMddHH.format(date);
    } catch (Exception e) {
      LogKit.error(e);
    }
    return "";
  }

  /**
   * 传入HHmmss格式字符串，返回HH:mm:ss格式字符串
   *
   * @return
   */
  public static String getSYSTIME2(String dateTime) {
    if (dateTime == null || "".equals(dateTime)) {
      return dateTime;
    }
    Date date = null;
    try {
      date = df_HHmmss.parse(dateTime);
      return df_HH_mm_ss.format(date);
    } catch (ParseException e) {
      LogKit.error(e);
    }
    return dateTime;
  }

  /**
   * 取当前的时间返回HHmmss格式字符串
   *
   * @return
   */
  public static String getSYSTIME() {
    Date date = new Date();
    return df_HHmmss.format(date);
  }

  /**
   * 取当前的时间返回HHmmssSSS格式字符串
   *
   * @return
   */
  public static String getSYSTIMESSS() {
    Date date = new Date();
    return df_HHmmssSSS.format(date);
  }

  /**
   * 取当前的日期返回yyyyMMdd格式字符串
   *
   * @return
   */
  public static String getSYSDATE() {
    Date date = new Date();
    return df_yyyyMMdd.format(date);
  }

  /**
   * 传入yyyyMMdd格式字符串，返回yyyy-MM-dd格式的字符
   *
   * @return
   */
  public static String getSYSDATE2(String dateString) {
    if ("".equals(parseString(dateString))) {
      return "";
    }
    //2021/09/29 viva modify 长沙rosa：财务端IC制卡导入的excel 有效期带-符号，但如果直接预开卡，没有进行剔除-符号，照成有问题
    dateString = parseString(dateString).replaceAll("/", "")
      .replaceAll("-", "");
    Date date = null;
    try {
      date = df_yyyyMMdd.parse(dateString);
      return df_yyyy_MM_dd.format(date);
    } catch (ParseException e) {
      LogKit.error(e);
    }
    return dateString;
  }

  /**
   * 取当前的星期
   *
   * @return
   */
  public static String getSYSWEEK() {
    Calendar c = Calendar.getInstance();
    c.getTime();
    int d = c.get(Calendar.DAY_OF_WEEK) - 1;

    // 因周日返回的是 0, 但我们要的是 7 所以在这里做这个判断
    if (d == 0) {
      d = 7;
    }
    return "" + d;
  }

  /**
   * 取当前的几号
   *
   * @return
   */
  public static String getSYSMONTHDAY() {
    Calendar c = Calendar.getInstance();
    c.getTime();
    int d = c.get(Calendar.DAY_OF_MONTH);
    return "" + d;
  }

  /**
   * 取当前的日期返回yyyy-MM-dd格式字符串
   *
   * @return
   */
  public static String getSYSDATE2() {
    Date date = new Date();
    return df_yyyy_MM_dd.format(date);
  }

  /**
   * 取昨天的日期
   *
   * @return
   */
  public static String getYesterday() {
    // SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    Calendar c = Calendar.getInstance();
    c.getTime();
    int d = c.get(Calendar.DAY_OF_MONTH);
    --d; // 将“日”减一，即得到前一天
    c.set(Calendar.DAY_OF_MONTH, d);
    return df_yyyyMMdd.format(c.getTime());
  }

  /**
   * 得加月后日期
   */
  public static String getAfterMoth(int AferMoth) {
    Calendar c = Calendar.getInstance();// 获得一个日历的实例
    int d = c.get(Calendar.MONTH);
    d = d + AferMoth;
    c.set(Calendar.MONTH, d);
    return df_yyyyMMdd.format(c.getTime());
  }

  /**
   * 得到几天前的日期
   *
   * @param iBeforeDays
   * @return
   */
  public static String getBeforeDay(int iBeforeDays) {
    Calendar c = Calendar.getInstance();
    int d = c.get(Calendar.DAY_OF_MONTH);
    d = d - iBeforeDays;
    c.set(Calendar.DAY_OF_MONTH, d);
    return df_yyyyMMdd.format(c.getTime());
  }

  /**
   * 获取两个日期之间的所有日期(yyyyMMdd)
   *
   * @param startTime 开始日期
   * @param endTime   结束日期
   * @return
   */
  public static List<String> getDays(String startTime, String endTime) {
    // 返回的日期集合
    List<String> days = new ArrayList<String>();
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    try {
      Date start = dateFormat.parse(startTime);
      Date end = dateFormat.parse(endTime);

      Calendar tempStart = Calendar.getInstance();
      tempStart.setTime(start);

      Calendar tempEnd = Calendar.getInstance();
      tempEnd.setTime(end);
      tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
      while (tempStart.before(tempEnd)) {
        days.add(dateFormat.format(tempStart.getTime()));
        tempStart.add(Calendar.DAY_OF_YEAR, 1);
      }

    } catch (ParseException e) {
      e.printStackTrace();
    }

    return days;
  }

  /**
   * 得到几天前的日期
   *
   * @param SL_DATE     :20120811
   * @param iBeforeDays
   * @return
   */
  public static String getBeforeDay(String SL_DATE, int iBeforeDays) {
    Date cDate = Convert(SL_DATE + "000000");
    Calendar c = Calendar.getInstance();
    c.setTime(cDate);
    int d = c.get(Calendar.DAY_OF_MONTH);
    d = d - iBeforeDays;
    c.set(Calendar.DAY_OF_MONTH, d);
    return df_yyyyMMdd.format(c.getTime());
  }

  /**
   * 得到几天前的日期
   *
   * @param iBeforeDays
   * @return
   */
  public static long getBeforeDayToLong(int iBeforeDays) {
    Calendar c = Calendar.getInstance();
    int d = c.get(Calendar.DAY_OF_MONTH);
    d = d - iBeforeDays;
    c.set(Calendar.DAY_OF_MONTH, d);
    return c.getTimeInMillis();
  }

  /**
   * 得到之前的某一时间
   *
   * @param TIME         :20141111120101
   * @param iBeforeTimes 数
   * @param sType        1(HOUR_OF_DAY 时) 2(MINUTE 分) 3(SECOND 秒)
   * @param sFormat      返回格式 df_HHmmss
   * @return
   */
  public static String getBeforeTime(String TIME, int iBeforeTimes, int sType, SimpleDateFormat sFormat) {
    String sTime = parseString(TIME);
    if (sTime.length() != 14) {
      sTime = getSYSDATETIME();
    }
    Date cDate = Convert(sTime);
    Calendar c = Calendar.getInstance();
    c.setTime(cDate);
    int d;
    switch (sType) {
      case 1:
        d = c.get(Calendar.HOUR_OF_DAY);
        d = d - iBeforeTimes;
        c.set(Calendar.HOUR_OF_DAY, d);
        sTime = sFormat.format(c.getTime());
        break;
      case 2:
        d = c.get(Calendar.MINUTE);
        d = d - iBeforeTimes;
        c.set(Calendar.MINUTE, d);
        sTime = sFormat.format(c.getTime());
        break;
      case 3:
        d = c.get(Calendar.SECOND);
        d = d - iBeforeTimes;
        c.set(Calendar.SECOND, d);
        sTime = sFormat.format(c.getTime());
        break;
    }
    return sTime;
  }

  /***
   * 比较两个日期字符串中 compareDate 是否最小
   *
   * @param compareDate
   * @param sysDate
   * @return
   */
  public static boolean minDate(String compareDate, String sysDate) {
    try {
      Date cDate = Convert(compareDate);
      Date sDate = Convert(sysDate);
      long cTime = cDate.getTime();
      long sTtime = sDate.getTime();

      if (cTime > sTtime)
        return false;
      else
        return true;
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * 字符串转换成Date类型
   *
   * @param strTime
   * @return
   */
  public static Date Convert(String strTime) {
    // String format = "yyyyMMddHHmmss";
    Date date = null;
    try {
      // SimpleDateFormat tf = new SimpleDateFormat(format);
      date = df_yyyyMMddHHmmss.parse(strTime);
    } catch (Exception e) {

    }
    return date;
  }

  /**
   * 日期合法check
   *
   * @param date(20141117) 需要check的日期
   * @return 日期是否合法
   */
  public static boolean chkDateFormat(String date) {
    try {
      // 如果输入日期不是8位的,判定为false.
      if (null == date || "".equals(date) || !date.matches("[0-9]{8}")) { //$NON-NLS-2$
        return false;
      }
      int year = Integer.parseInt(date.substring(0, 4));
      int month = Integer.parseInt(date.substring(4, 6)) - 1;
      int day = Integer.parseInt(date.substring(6));
      Calendar calendar = GregorianCalendar.getInstance();
      // 当 Calendar 处于 non-lenient 模式时，如果其日历字段中存在任何不一致性，它都会抛出一个异常。
      calendar.setLenient(false);
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.MONTH, month);
      calendar.set(Calendar.DATE, day);
      // 如果日期错误,执行该语句,必定抛出异常.
      calendar.get(Calendar.YEAR);
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }

  /**
   * 将该对象序列化成流,因为写在流里的是对象的一个拷贝，<br>
   * 而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
   *
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static Object deepCopy(Object sourceObj) {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos;
      oos = new ObjectOutputStream(bos);
      oos.writeObject(sourceObj);
      // 将流序列化成对象
      ByteArrayInputStream bis = new ByteArrayInputStream(bos
        .toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bis);
      return ois.readObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 默认显示2位小数，比如显示百分比
   *
   * @param obj
   * @return
   */
  public static String showValue(Object obj) {
    int iDEC_PLACE = 2;
    String sValue = null;
    double value = parseValueDouble(obj);
    sValue = sdf_pos2.format(value);
    try {
      int deciValue = parseInt(sValue.substring(sValue.length()
        - iDEC_PLACE));
      if (deciValue == 0) {
        return sValue.substring(0, sValue.length() - iDEC_PLACE - 1);
      }
    } catch (Exception e) {
    }
    return sValue;
  }

  public static boolean hadInitRoot = false;

  public static String baseStr = "";

  public static String getRoot() {

    if (!hadInitRoot) {
      File file = new File("emisPath.txt");
      if (!file.exists())
        try {
          file.createNewFile();
        } catch (IOException e) {
          e.printStackTrace();
        }

      baseStr = file.getAbsolutePath().substring(0,
        file.getAbsolutePath().indexOf("emisPath.txt"));
      System.out.println("project root path:" + baseStr);
      if (baseStr.endsWith("\\")) {
        // 不处理\\
      } else if (!baseStr.endsWith("/"))
        baseStr = baseStr + "/";// 
      hadInitRoot = true;

    }

    return baseStr;

  }

  /**
   * 本方法用于取资料存放主路径 主要包括:上传的资料路径 和 列印文件资料路径
   */
  public static String getFileRoot() {
    String fileRoot = "";
    fileRoot = getRoot() + "data/";
    if (checkDir(fileRoot))
      return fileRoot;
    else
      return "";
  }

  /**
   * 本方法用于取资料存放主路径 主要包括:上传的资料路径 和 列印文件资料路径
   */
  public static String getFileRptRoot() {
    String fileRoot = "";
    fileRoot = "data/sale/";
    if (checkDir(fileRoot))
      return fileRoot;
    else
      return "";
  }

  public static boolean checkDir(String dir) {
    boolean ok = false;
    File dfile = new File(dir);
    if (!dfile.exists())
      if (!dfile.isDirectory()) {
        try {
          dfile.mkdirs();
          ok = true;
        } catch (Exception e) {
          ok = false;
        }
      } else
        ok = true;
    else
      ok = true;
    return ok;
  }

  /**
   * 计算两个时间之间相隔时间 （计算判断）
   *
   * @param timeS      开始时间
   * @param timeE      结束时间
   * @param returnType 0 返回时 1 返回分 2 返回秒
   * @return *
   * <p>
   * 传入时间格式 yyyyMMddHHmmss
   */
  public static long IntervalTime(String timeS, String timeE, int returnType) {
    Date begin;
    Date end;
    try {
      begin = df_yyyyMMddHHmmss.parse(timeS);
      end = df_yyyyMMddHHmmss.parse(timeE);
      long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
      long hour1 = between / 3600;
      long minute1 = between % 3600 / 60;
      if (returnType == 0) {
        return hour1;
      } else if (returnType == 1) {
        return minute1;
      } else if (returnType == 2) {
        return between;
      }
      return between;
    } catch (ParseException e) {
      LogKit.error(e);
    }
    return 0;
  }

  /**
   * 计算两个时间之间相隔时间
   *
   * @param timeS 开始时间
   * @param timeE 结束时间
   * @return
   */
  public static String getIntervalTime(String timeS, String timeE) {
    return getIntervalTime(timeS, timeE, 3);
  }

  /**
   * 计算两个时间之间相隔时间
   *
   * @param timeS      开始时间
   * @param timeE      结束时间
   * @param returnType 0 返回时 1 返回分 2 返回秒 3 返回 时 分
   * @return *
   * <p>
   * 传入时间格式 yyyyMMddHHmmss
   */
  public static String getIntervalTime(String timeS, String timeE, int returnType) {
    Date begin;
    Date end;
    try {
      begin = df_yyyyMMddHHmmss.parse(timeS);
      end = df_yyyyMMddHHmmss.parse(timeE);
      long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
      long hour1 = between / 3600;
      long minute1 = between % 3600 / 60;
      if (returnType == 0) {
        return hour1 + "";
      } else if (returnType == 1) {
        return minute1 + "";
      } else if (returnType == 2) {
        return between + "";
      } else if (returnType == 3) {
        return (hour1 + "小时" + minute1 + "分");
      }
      return (between + "");
    } catch (ParseException e) {
      LogKit.error(e);
    }
    return "";
  }

  /**
   * 是否为开发模式
   *
   * @return
   */
  public static boolean isDev() {
    /*try {
      File file = new File("devVenus");
      if (file.exists()) {
        // 开发模式，不检查加密狗
        return true;
      }
    } catch (Exception e) {
    }*/
    return false;
  }

  /**
   * 計算两个日期相差的天数
   *
   * @param early 開始日期
   * @param late  結束日期
   * @return
   */
  public static final long daysBetween(String early, String late) {
    SimpleDateFormat df_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    Date earlydate = null;
    Date latedate = null;
    long days = 0;
    try {
      earlydate = df_yyyyMMdd.parse(early);
      latedate = df_yyyyMMdd.parse(late);
      Calendar calst = Calendar.getInstance();
      Calendar caled = Calendar.getInstance();
      calst.setTime(earlydate);
      caled.setTime(latedate);
      // 设置时间为0时
      calst.set(Calendar.HOUR_OF_DAY, 0);
      calst.set(Calendar.MINUTE, 0);
      calst.set(Calendar.SECOND, 0);
      caled.set(Calendar.HOUR_OF_DAY, 0);
      caled.set(Calendar.MINUTE, 0);
      caled.set(Calendar.SECOND, 0);
      // 得到两个日期相差的天数
      days = ((long) (caled.getTime().getTime() / 1000) - (long) (calst
        .getTime().getTime() / 1000)) / 3600 / 24;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return days;
  }

  /**
   * 检查网络状态
   *
   * @param httpUrl
   * @return
   */
  public static boolean checkWebState(String httpUrl) {
    if ("".equals(parseString(httpUrl))) {
      return false;
    }
    Socket sock = null;
    try {
      sock = new Socket(httpUrl, 80);
      // 超时检查，不能为空，否则长时间画面没有提示正确断线后再连接正常的信息
      sock.setSoTimeout(15000);
    } catch (Exception e) {
      return false;
    } finally {
      if (sock != null) {
        try {
          sock.close();
        } catch (IOException e) {
        }
      }
      sock = null;
    }
    return true;
  }

  /**
   * 验证Entity中是否没有数据
   *
   * @param entity 需要验证的Entity
   * @return | true Entity中没有数据 | | false 中有数据 |
   */
  public static boolean isEmpty(Entity entity) {
    if (entity == null || entity.getM_rowCount() < 1) {
      return true;
    }
    return false;
  }

  /**
   * 从后台接口中(check_alive.jsp)取得服务器时间yyyy-MM-dd HH:mm:ss
   * 注意IOUtils.toString好像只能调用一次
   *
   * @param _sContentnt 如果前面有处理IOUtils.toString，请传入_sContentnt
   * @return
   * @throws IOException
   */
  private static Date getSerCheckAliveDate(String _sContentnt) {
    try {
      if (_sContentnt == null) {
        return null;
      }
      if (isJSON(_sContentnt)) {
        JSONObject _oJsonObject = parseJSON(_sContentnt);
        String _sTime = parseString(_oJsonObject
          .getString("SRV_TIME"));
        if (!"".equals(_sTime)) {
          LogKit.info("==获取到的服务器时间：" + _sTime);
          // 保存服务器时间
          Date serDate = null;
          try {
            serDate = df_yyyy_MM_ddHHmmss.parse(_sTime.trim());
            //webStateKeeper.getInstance().setSerTime(serDate.getTime());
            return serDate;
          } catch (ParseException e) {
            LogKit.error(e);
          }
        }
      }
    } catch (Exception e) {
      LogKit.error(e);
    }
    return null;
  }

  /**
   * HTTP 协议 Header的取时间 yyyy-MM-dd HH:mm:ss
   *
   * @param responseMap
   * @throws ParseException
   */
  private static Date getHTTPHeaderDate(Map<String, Object> responseMap) {
    try {
      // 获取后台时间
      String strSerDate = parseString(responseMap.get("Date"));
      LogKit.info("==获取到的服务器时间：" + strSerDate);
      if (strSerDate != null && !"".equals(strSerDate)) {
        DateFormat sdf;
        // 格式化服务器时间
        if (strSerDate.length() == strSerDate.getBytes().length) {
          LogKit.info("==获取到的服务器时间是英文格式！");
          // 没有中文的格式
          sdf = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        } else {
          LogKit.info("==获取到的服务器时间是中文格式！");
          // 有中文的格式
          sdf = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.CHINA);
        }
        // 设置本地时间的时区
        sdf.setTimeZone(TimeZone.getDefault());
        Date serDate = sdf.parse(strSerDate);
        // 保存服务器时间
        //webStateKeeper.getInstance().setSerTime(serDate.getTime());
        return serDate;
      }
    } catch (Exception e) {
      LogKit.error(e);
    }
    return null;
  }

  /**
   * 检查IP是否通
   *
   * @param ip
   * @return
   */
  public static boolean checkIPOnline(String ip) {
    if ("".equals(parseString(ip))) {
      return false;
    }
    try {
      InetAddress address = InetAddress.getByName(ip);
      return address.isReachable(3000);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 产生txt
   *
   * @param printContent
   * @param fileName
   * @param isNeedRepace 是否需要替换特殊字符：比如放大字体的标示
   * @throws IOException
   */
  public static void createTxt(String printContent, String fileName, boolean isNeedRepace) {
  }

  public static Double emisRound(double iValue, int iDecimal) {
    double _iReturn = iValue * Math.pow(10, iDecimal);
    _iReturn = Math.round(_iReturn) / Math.pow(10, iDecimal);
    return _iReturn;
  }

  /**
   * 把Object转换成double，如Object==null 返回默认值
   *
   * @param obj
   * @param defaultValue
   * @return
   */
  public static double parseDouble(Object obj, double defaultValue) {
    // 如果是整数，直接返回
    try {
      Double d = new Double(obj.toString());
      int iValue = d.intValue();
      if (iValue == d.doubleValue()) {
        d = null;
        return iValue;
      }
      d = null;
    } catch (Exception e) {
    }
    double value = parseDefaultDouble(obj, defaultValue);
    // 多做一次保留2位小数
    //value = parseSpeDouble(value);
    return value;
  }

  /**
   * 把Object转换成double,只取整数
   *
   * @param obj
   * @return
   */
  public static int parseDoubleToInt(Object obj) {
    // 如果是整数，直接返回
    try {
      Double d = new Double(obj.toString());
      return d.intValue();
    } catch (Exception e) {
    }
    return 0;
  }

  /**
   * 默认的转double类型的方法
   *
   * @param obj
   * @param defaultValue
   * @return
   */
  public static double parseDefaultDouble(Object obj, double defaultValue) {
    // 如果Obj是NaN, 直接返回0
    if ("NaN".equals(parseString(obj))) {
      return 0;
    }
    double value = defaultValue;
    try {
      value = Double.parseDouble(parseString(obj, String
        .valueOf(defaultValue)));
    } catch (Exception ex) {
      value = defaultValue;
    }
    return value;
  }

  /**
   * 保留4位的处理double值
   *
   * @param obj
   * @return
   */
  public static double parseValueDouble(Object obj) {
    int iDEC_PLACE = 4;
    try {
      StringBuffer sbf = new StringBuffer(parseString(obj, "0")
        .trim());
      String reverseStr = sbf.reverse().toString();
      // 如果小数位＜=金额计算到小数几位,直接返回
      int curPlace = reverseStr.indexOf(".");
      if (curPlace <= iDEC_PLACE) {
        return parseDefaultDouble(obj, 0);
      }
      // 0为保留整数
      if (iDEC_PLACE == 0) {
        // 看小数位是否为零，如果是，直接返回
        if (parseInt(reverseStr.substring(0, curPlace)) == 0) {
          return parseDefaultDouble(obj, 0);
        }
      }
    } catch (Exception e) {
      return parseDefaultDouble(obj, 0);
    }
    BigDecimal money = null;
    try {
      money = new BigDecimal(parseString(obj, "0").trim());
      // AMOUNT_RULE进位法则：0：四舍五入 1：无条件进位 2：无条件舍弃位
      int iRoundingMode = BigDecimal.ROUND_HALF_UP;// 四舍五入 必须选这个
      // 设置精度，以及舍入规则
      money = money.setScale(iDEC_PLACE, iRoundingMode);
    } catch (Exception ex) {
      LogKit.error(ex);
    }
    if (money != null) {
      double amt = money.doubleValue();
      money = null;
      return amt;
    } else {
      return parseDefaultDouble(obj, 0);
    }

  }

  /**
   * 保留几位小数的处理double值
   *
   * @param obj
   * @param iDEC_PLACE 保留几位小数
   * @return
   */
  public static double parseToDouble(Object obj, int iDEC_PLACE) {
    try {
      StringBuffer sbf = new StringBuffer(parseString(obj, "0")
        .trim());
      String reverseStr = sbf.reverse().toString();
      // 如果小数位＜=金额计算到小数几位,直接返回
      int curPlace = reverseStr.indexOf(".");
      if (curPlace <= iDEC_PLACE) {
        return parseDefaultDouble(obj, 0);
      }
      // 0为保留整数
      if (iDEC_PLACE == 0) {
        // 看小数位是否为零，如果是，直接返回
        if (parseInt(reverseStr.substring(0, curPlace)) == 0) {
          return parseDefaultDouble(obj, 0);
        }
      }
    } catch (Exception e) {
      return parseDefaultDouble(obj, 0);
    }
    BigDecimal money = null;
    try {
      money = new BigDecimal(parseString(obj, "0").trim());
      // AMOUNT_RULE进位法则：0：四舍五入 1：无条件进位 2：无条件舍弃位
      int iRoundingMode = BigDecimal.ROUND_HALF_UP;// 四舍五入 必须选这个
      // 设置精度，以及舍入规则
      money = money.setScale(iDEC_PLACE, iRoundingMode);
    } catch (Exception ex) {
      LogKit.error(ex);
    }
    if (money != null) {
      double amt = money.doubleValue();
      money = null;
      return amt;
    } else {
      return parseDefaultDouble(obj, 0);
    }

  }


  /**
   * 把obj 元角分类型的字串转成double类型的字串 如100转成1.00，以分为单位
   *
   * @param obj
   * @param defaultValue
   * @return
   */
  public static double parseObjectDouble(Object obj, double defaultValue) {
    // 如果Obj是NaN, 直接返回0
    if ("NaN".equals(parseString(obj))) {
      return 0;
    }
    double value = defaultValue;
    try {
      StringBuffer sbf = new StringBuffer(parseString(obj, String.valueOf(defaultValue)));
      if (sbf.toString().indexOf(".") < 0) {
        if (sbf.length() == 1) {
          sbf.insert(0, "0.0");
        } else if (sbf.length() == 2) {
          sbf.insert(0, "0.");
        } else {
          sbf.insert(sbf.length() - 2, ".");
        }
      }
      value = Double.parseDouble(sbf.toString());
    } catch (Exception ex) {
      value = defaultValue;
    }
    return value;
  }

  /***
   * 把金额转成元角分类型的字串，以分为单位 如100.12转成10012
   *
   * @param amt
   * @return
   */
  public static String parseDoubleToString(double amt) {
    if (amt == 0) {
      return "0";
    }
    DecimalFormat df = new DecimalFormat("#0.000");
    StringBuffer sbf = new StringBuffer();
    String sAmt = parseString(df.format(Math.abs(amt)), "0").trim();
    int iNum = -1; // 统计金额小数位
    for (int i = 0; i < sAmt.length(); i++) {
      if (iNum == 2) {
        // 2位小数后的值舍掉
        break;
      }
      if ('.' == sAmt.charAt(i)) {
        iNum++;
      } else {
        if (iNum > -1) {
          iNum++;
        }
        if (sbf.length() <= 0 && '0' == sAmt.charAt(i)) {
          // 如0.01舍掉0.0
          continue;
        }
        sbf.append(sAmt.charAt(i));
      }
    }
    if (iNum == -1 || iNum == 0) {
      sbf.append("00");
    } else if (iNum == 1) {
      sbf.append("0");
    }
    return sbf.toString();
  }

  /**
   * 根据门市编号查询门市名称
   */
  public static String getS_NAME(String P_NO) {
    String P_NAME = "";
    try {
      Entity entity = new Entity();
      entity.loadBySql("select TD_NAME from tab_d where TD_NO='" + P_NO
        + "' and T_NO='STORE' and USERD='Y'");
      entity.first();
      P_NAME = parseString(entity.getString("TD_NAME"));
    } catch (Exception e) {
      LogKit.error(e);
    }

    return P_NAME;
  }

  /**
   * 产生异常Log档
   *
   * @param errInfo 异常Log档描述
   * @param errType 异常类型
   */
  public static void turnOutErrorLog(String errInfo, String errType) {
  }

  /**
   * 验证是否JSON格式数据
   */
  public static boolean isJSON(String sContentnt) {
    if (sContentnt == null || "".equals(sContentnt)
      || !((sContentnt.startsWith("{") && sContentnt.endsWith("}"))
      || !(sContentnt.startsWith("[") && sContentnt.endsWith("]")))) {
      return false;
    }

    return true;
  }

  /**
   * 将String转换为JSON格式
   *
   * @return 内容为空或格式不正确返回null
   */
  public static JSONObject parseJSON(String sContentnt) {
    if (!isJSON(sContentnt)) {
      return null;
    }
    JSONObject _oJSON = null;
    if (!"".equals(sContentnt)) {
      try {
        sContentnt = StringUtils.replace(sContentnt, ":\"null\"",
          ":\"\"");
        sContentnt = StringUtils.replace(sContentnt, ":\"NULL\"",
          ":\"\"");

        //2022/04/26 viva modify 后台返回值Json字符串中如果有换行：CFLF会造成Json解析出错:Unterminated string at character，前台做一下转义改成空
        if (sContentnt.indexOf("\r\n") != -1) {
          //将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行  :\\u000d\\u000a
          sContentnt = sContentnt.replaceAll("\r\n", "");
        }
        if (sContentnt.indexOf("\n") != -1) {
          //将换行转换一下，因为JSON串中字符串不能出现显式的换行  :\\u000a
          sContentnt = sContentnt.replaceAll("\n", "");
        }
        if (sContentnt.indexOf("\r") != -1) {
          //将换行转换一下，因为JSON串中字符串不能出现显式的换行  :\\u000d
          sContentnt = sContentnt.replaceAll("\r", "");
        }
      } catch (Exception e) {
        LogKit.error(e);
      }
    }
    try {
      _oJSON = JSONObject.fromObject(sContentnt);
    } catch (Exception e) {
      LogKit.error("===parseJSON error:" + e);
      try {
        turnOutErrorLog("【严重异常】Json解析出错:" + e.getMessage(), "SALE");
        uploadLogs("");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return _oJSON;
  }

  /**
   * 将String转换为JSON格式
   *
   * @return 内容为空或格式不正确返回null
   */
  public static JSONArray parseJSONArray(String sContentnt) {
    JSONArray _oJSON = null;
    if (!"".equals(sContentnt)) {
      try {
        sContentnt = StringUtils.replace(sContentnt, ":\"null\"",
          ":\"\"");
        sContentnt = StringUtils.replace(sContentnt, ":\"NULL\"",
          ":\"\"");
        //2022/04/26 viva modify 后台返回值Json字符串中如果有换行：CFLF会造成Json解析出错:Unterminated string at character，前台做一下转义改成空
        if (sContentnt.indexOf("\r\n") != -1) {
          //将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行  :\\u000d\\u000a
          sContentnt = sContentnt.replaceAll("\r\n", "");
        }
        if (sContentnt.indexOf("\n") != -1) {
          //将换行转换一下，因为JSON串中字符串不能出现显式的换行  :\\u000a
          sContentnt = sContentnt.replaceAll("\n", "");
        }
        if (sContentnt.indexOf("\r") != -1) {
          //将换行转换一下，因为JSON串中字符串不能出现显式的换行  :\\u000d
          sContentnt = sContentnt.replaceAll("\r", "");
        }
      } catch (Exception e) {
        LogKit.error(e);
      }
    }
    try {
      _oJSON = JSONArray.fromObject(sContentnt);
    } catch (Exception e) {
      LogKit.error(
        "===parseJSONArray error:" + e);
      try {
        turnOutErrorLog("【严重异常】Json解析出错:" + e.getMessage(), "SALE");
        uploadLogs("");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return _oJSON;
  }

  public static void uploadLogs(String sDate) {
  }

  /**
   * 获取当前操作系统名称. return 操作系统名称 例如:windows xp,linux 等.
   */
  public static String getOSName() {
    return System.getProperty("os.name").toLowerCase();
  }

  public static void main(String[] args) {
    System.out.println("==");

    System.out.println(createEasyCode("A0012"));
    System.out.println(unLockEasyCode("2999"));
  }

  /**
   * 根據VAR_NAME獲取EMISPROP表中的VAR_VALUE值
   */
  public static String getVarValue(String sVarName) {
    String _sVarValue = "";
    try {
      Entity _oEmisPropEntity = new Entity();
      _oEmisPropEntity
        .loadBySql("select VAR_VALUE from EMISPROP where VAR_NAME='"
          + sVarName + "'");
      _oEmisPropEntity.first();

      _sVarValue = parseString(_oEmisPropEntity
        .getString("VAR_VALUE"));
    } catch (Exception e) {
      LogKit.error(e);
    }
    return _sVarValue;
  }

  /**
   * 用于锁定文件，实现程序单开（即不允许多个实例）
   *
   * @param lockFile
   * @return
   */
  public static boolean lockInstance(final String lockFile) {
    try {
      final File file = new File(lockFile);
      final RandomAccessFile randomAccessFile = new RandomAccessFile(
        file, "rw");
      final FileLock fileLock = randomAccessFile.getChannel().tryLock();
      if (fileLock != null) {
        Runtime.getRuntime().addShutdownHook(new Thread("addShutdownHook") {
          public void run() {
            try {
              fileLock.release();
              randomAccessFile.close();
              file.delete();
              LogKit.info("Venus已退出……\r\n\r\n");
            } catch (Exception e) {
              LogKit.error(
                "Unable to remove lock file: " + lockFile,
                e);
            }
          }
        });
        return true;
      }
    } catch (Exception e) {
      LogKit.error(
        "Unable to create and/or lock file: " + lockFile, e);
    }
    return false;
  }

  /**
   * Venus落单系统是否已开启
   *
   * @return
   */
  public static boolean isVenusAlreadyRunning() {
    return !lockInstance(".lockVenus");
  }

  /**
   * Venus落单系统是否已开启
   *
   * @return
   */
  public static boolean isHadVenusAlreadyRunning() {
    final File file = new File(".lockVenus");
    return file.exists();
  }

  /**
   * 获取Json的参数
   *
   * @param json
   * @param key
   * @return
   */
  public static String getJsonString(JSONObject json, String key) {
    if (json != null && json.has(key)) {
      return json.getString(key);
    }
    return "";
  }

  /**
   * 转换处理网卡集
   *
   * @param macs
   * @return
   */
  public static String parseMacAddress(List<String> macs) {
    if (macs != null) {
      return macs.toString().replaceAll("\\[", "").replaceAll("\\]", "") //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        .replaceAll(", ", "@"); //$NON-NLS-2$
    } else {
      return "";
    }
  }

  /**
   * 检查是否为美食街服务台模式
   *
   * @return
   */
  public static boolean isCardServiceMode() {
    return "CS".equalsIgnoreCase(System.getProperty("emis.platform")); //$NON-NLS-2$
  }

  /**
   * 检查是否为美食街财务工具模式
   *
   * @return
   */
  public static boolean isCardFinacialMode() {
    return "CF".equalsIgnoreCase(System.getProperty("emis.platform")); //$NON-NLS-2$
  }

  /**
   * 检查是否为美食街档口模式
   *
   * @return
   */
  public static boolean isFoodCourtMode() {
    return "FC".equalsIgnoreCase(System.getProperty("emis.platform")); //$NON-NLS-2$
  }

  /**
   * 判断是否美食街系统，是则使用Asscii编码，否则维持原编码方式
   *
   * @return
   */
  public static boolean isFoodCourtSys() {
    return isFoodCourtMode() || isCardFinacialMode()
      || isCardServiceMode();
  }

  /**
   * 检查是否为Venus财务工具模式
   *
   * @return
   */
  public static boolean isVenusCardFinacialMode() {
    return "IC_GIFT".equalsIgnoreCase(System.getProperty("emis.platform")); //$NON-NLS-2$
  }

  /**
   * 必须高于JDK1.5
   *
   * @return
   */
  public static boolean isAtLeastJava15() {
    String javaVersion = System.getProperty("java.version");
    // 1.5 会黑屏暂不支持
    if (javaVersion.contains("1.5.")) {
      return false;
    }
    return true;
  }

  /**
   * 处理空值字串(空格即视为空值)，当为空值时返回defaultValue的值
   *
   * @param obj
   * @param defaultValue
   * @return
   */
  public static String parseBlankString(Object obj, String defaultValue) {
    if (null == obj) {
      return defaultValue;
    } else {
      String val = String.valueOf(obj).trim();
      return "".equals(val) ? defaultValue : val;
    }
  }

  /**
   * 获取小票随机文章文件名
   *
   * @return
   */
  public static String getArtice() {
    try {
      //先删除空文件
      File fileOld = new File(getRoot() + "Artice");
      String[] articeOld = fileOld.list();
      for (int i = 0; i < articeOld.length; i++) {
        File _artice = new File(getRoot() + "Artice\\" + articeOld[i]);
        if (!(_artice.exists() && _artice.length() > 0)) {
          _artice.delete();
        }
      }
      //重新获取
      File file = new File(getRoot() + "Artice");
      String[] artice = file.list();
      int random = artice.length;
      if (file != null && file.exists() && random > 0) {
        //返回随机文件名称
        Random r = new Random();
        String fileName = getRoot() + "Artice\\" + artice[r.nextInt(random)];
        return fileName;
      } else {
        return "";
      }
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * 获取机器所有网卡的IP（ipv4）
   *
   * @return
   */
  public static List<String> getLocalIP() {
    List<String> ipList = new ArrayList<String>();
    InetAddress ip = null;
    try {
      Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
      while (netInterfaces.hasMoreElements()) {
        NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
        // 遍历所有ip
        Enumeration<InetAddress> ips = ni.getInetAddresses();
        while (ips.hasMoreElements()) {
          ip = (InetAddress) ips.nextElement();
          if (null == ip || "".equals(ip)) {
            continue;
          }
          String sIP = ip.getHostAddress();
          if (sIP == null || sIP.indexOf(":") > -1) {
            continue;
          }
          ipList.add(sIP);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ipList;
  }

  /**
   * 将机台代码转换成数字，去掉需要左边的0,A-J 1-0
   *
   * @param SL_NO 传入的机台代码+流水号
   * @return 转换的机台代码+流水（去0）
   */
  public static String createEasyCode(String SL_NO) {
    if ("".equals(SL_NO)) {
      return SL_NO;
    }
    //先判断SL_NO长度,长度小于2或者大于五返回原字符串,默认2-5位
    if (SL_NO.length() > 5 || SL_NO.length() < 2) {
      return SL_NO;
    }
    String str[] = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String id_no_flag = SL_NO.substring(0, 1);
    String _sl_no = SL_NO.substring(1, SL_NO.length());
    for (int i = 0; i < str.length; i++) {
      if (str[i].equals(id_no_flag)) {
        if (i == str.length - 1) {
          id_no_flag = parseString(0);
        } else {
          id_no_flag = parseString(i + 1);
        }
      }
    }
    _sl_no = _sl_no.replaceFirst("^0*", "");
    return id_no_flag + _sl_no;
  }

  /**
   * 将数字转换成机台代号,A-J 1-0
   *
   * @param SL_NO 传入的机台数字+流水号
   * @return 转换的机台代码+流水（去0）
   */
  public static String unLockEasyCode(String SL_NO) {
    if ("".equals(SL_NO)) {
      return SL_NO;
    }
    //先判断SL_NO长度,大于5返回原字符串
    if (SL_NO.length() > 5) {
      return SL_NO;
    }

    String str[] = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String id_no_flag = SL_NO.substring(0, 1);
    String _sl_no = SL_NO.substring(1, SL_NO.length());
    for (int i = 0; i < str.length; i++) {
      String s = parseString(i + 1);
      if (s.equals(id_no_flag)) {
        if (i == str.length - 1) {
          id_no_flag = "J";
        } else {
          id_no_flag = str[i];
        }
      }
    }

    return id_no_flag + _sl_no;
  }

  //--------------------------------------------------------------------
  /**
   * 語言
   */
  public static String LANGUAGE = System.getProperty("user.language", "zh");
  /**
   * 國碼
   */
  public static String COUNTRY = System.getProperty("user.region", "TW");
  /**
   * 語系
   */
  public static Locale LOCALE = new Locale(LANGUAGE, COUNTRY);
  /**
   * 檔案編碼
   */
  public static String FILENCODING = System.getProperty("file.encoding", "UTF-8");
  /**
   * 路徑分隔字元
   */
  public static String FILESEPARATOR = System.getProperty("file.separator");
  /**
   * 換行字元
   */
  public static String LINESEPARATOR = System.getProperty("line.separator");

  static {
    //- IDEA 4.5執行時會傳入-Dfile.encoding=x-windows-950, 導致轉碼錯誤
    if ("x-windows-950".equals(FILENCODING)) {
      FILENCODING = "MS950";
    }
  }

  /*  Robert,2010/12/23
   *  cfg 檔加上 ${variable} 代換功能, 可以使用像 ${home.resin} 之類的 jvm properties 來代換
   */
  public static void subDynamicProperties(Properties oProps) {

    if (oProps == null) return;


    Enumeration e = oProps.keys();

    while (e.hasMoreElements()) {
      String sKey = (String) e.nextElement();
      String sValue = oProps.getProperty(sKey);
      int iStartIdx = sValue.indexOf("${");
      int iEndIdx;
      if (iStartIdx != -1) {
        iEndIdx = sValue.indexOf("}", iStartIdx + 2);
        if (iEndIdx != -1) {
          //System.out.println("Old:'" + sValue + "'");
          //System.out.println(sValue.substring(0,iStartIdx));
          //System.out.println(sValue.substring(iStartIdx + 2,iEndIdx));
          //System.out.println(sValue.substring(iEndIdx + 1 ));
          String sNewValue = sValue.substring(0, iStartIdx) +
            System.getProperty(sValue.substring(iStartIdx + 2, iEndIdx)) +
            sValue.substring(iEndIdx + 1);
          //System.out.println("New:'" + sNewValue + "'");
          oProps.setProperty(sKey, sNewValue);
        }
      }
    }
  }

  /**
   * 搜尋oProps Properties物件的Key值，如果有相符於sPrefix的值
   * 才加在回傳的Properties中
   *
   * @param sPrefix
   * @param oProps
   * @return
   */
  public static Properties subProperties(String sPrefix, Properties oProps) {
    if (oProps == null) return null;
    if (sPrefix == null) return null;

    Enumeration _oE = oProps.keys();

    Properties _oOutProps = new Properties();
    while (_oE.hasMoreElements()) {
      String _sKey = (String) _oE.nextElement();
      int _Idx = -1;
      if ((_Idx = _sKey.indexOf(sPrefix)) != -1) { // 找到了.
        _Idx = _Idx + sPrefix.length();
        String _sNewKey = _sKey.substring(_Idx);
        _oOutProps.put(_sNewKey, oProps.get(_sKey));
      }
    }
    return _oOutProps;
  }

  public static String replaceParam(String sSQL, List oParam) {
    String SQL = sSQL;
    if (oParam == null) return SQL;
    int size = oParam.size();
    if (size == 0) return SQL;

    // 2006/04/12 update by andy start
    int iTopPost = 0;
    boolean bTop = false;   //判斷是否為TOP後面的參數
    SQL = stringReplace(SQL, " TOP ", " top ", "a");
    SQL = stringReplace(SQL, " Top ", " top ", "a");
    // update by andy end

    for (int i = 0; i < size; i++) {
      int idx = SQL.indexOf("?");
      if (idx == -1) // no ?
        break;

      // 2006/04/12 update by andy start
      if (iTopPost >= 0 && iTopPost < idx) { //SQL中有TOP且TOP位置比當前？的位置小才處理.
        iTopPost = SQL.indexOf(" top ", iTopPost);
        if (iTopPost > 0 && iTopPost < idx
          && "".equals(SQL.substring(iTopPost + 5, idx).trim())) {
          bTop = true;
          iTopPost = idx + 1;  //下次搜索從當前？後開始。
        } else {
          bTop = false;
        }
      }
      // update by andy end

      // Robert,2012/04/16 這邊造成  P_NO D200/W 時,變成 D200W , 判斷不正確
      // 這邊應該沒有必要做這個轉換
      // 造成日期的沒有拿掉
      String o = stringReplace(oParam.get(i) + "", "/", "", "a");

      o = stringReplace(o, "?", "", "a");  //add by abel 2005/12/25
      o = stringReplace(o, "''", "'", "a");
      o = stringReplace(o, "'", "''", "a");
      if (o == null) {
        SQL = SQL.substring(0, idx) + "''" + SQL.substring(idx + 1);
      } else if (bTop) {  // 是TOP的參數，不能加單引號'' 2006/04/12 update by andy
        SQL = SQL.substring(0, idx) + o.toString() + SQL.substring(idx + 1);
      } else {
        SQL = SQL.substring(0, idx) + "'" + o.toString() + "'" + SQL.substring(idx + 1);
      }
    }

    return SQL;
  }
    /*
    public static String replaceParamConvertDate(Object oParam ){
         String sParam = oParam +"";
        if( sParam.substring(0,2).equalsIgnoreCase("20") ||  ){

        }else{
          return sParam;
        }

    } */

  /**
   * 檢查傳入路徑是否以指定的分隔字元結尾, 若不是則附加在其後.<br>
   * Ex:"C:\test\" = checkDirectory("c:\test","\");
   *
   * @param sDirectory
   * @param sSeparator
   * @return
   */
  public static String checkDirectory(String sDirectory, String sSeparator) {
    if (sDirectory != null) {
      if (sDirectory.length() > 0) {
        String _sLast = sDirectory.substring(sDirectory.length() - 1);

        if (!_sLast.equals(sSeparator))
          sDirectory = sDirectory.concat(sSeparator);
      }
    }
    return sDirectory;
  }

  /**
   * 依據語系傳回目前的java.util.Date時間<BR>
   * Ex:"Tue Aug 28 15:00:00 GMT+08:00 2001"
   *
   * @return
   */
  public static Date now() {
    return Calendar.getInstance(LOCALE).getTime();
  }

  /**
   * 傳回系統所在的 Calendar 物件,相當於<BR>
   * Calendar.getInstance(<BR>
   * new Locale( System.getProperty("user.region"),<BR>
   * System.getProperty("user.language")));
   *
   * @return
   */
  public static Calendar getLocaleCalendar() {
    return Calendar.getInstance(LOCALE);
  }

  /**
   * 傳回目前的日期:民國年(整數)<br>
   * Ex:2001 -> 90
   *
   * @param oCalendar
   * @return
   */
  public static int getYearN(Calendar oCalendar) {
    return oCalendar.get(Calendar.YEAR) - 1911;
  }

  /**
   * 傳回目前的日期:民國年(字串)<br>
   * Ex:2001 -> "090"
   *
   * @param oCalendar
   * @return
   */
  public static String getYearS(Calendar oCalendar) {
    int _nYear = oCalendar.get(Calendar.YEAR) - 1911;
    if (_nYear < 100) {
      return "0" + String.valueOf(_nYear);
    }
    return String.valueOf(_nYear);
  }

  /**
   * 傳回目前的日期:月份(整數)<br>
   * Ex:Aug -> 8 (int)
   *
   * @param oCalendar
   * @return
   */
  public static int getMonthN(Calendar oCalendar) {
    return oCalendar.get(Calendar.MONTH) + 1;
  }

  /**
   * 傳回目前的日期:月份(字串)<br>
   * Ex:Aug -> "08"
   *
   * @param oCalendar
   * @return
   */
  public static String getMonthS(Calendar oCalendar) {
    int _nMonth = oCalendar.get(Calendar.MONTH) + 1;
    String _sMonth = String.valueOf(_nMonth);
    if (_nMonth < 10) _sMonth = "0".concat(_sMonth);
    return _sMonth;
  }

  /**
   * 傳回目前的日期:日(整數)<br>
   * Ex:8 -> 8 (int)
   *
   * @param oCalendar
   * @return
   */
  public static int getDateN(Calendar oCalendar) {
    return oCalendar.get(Calendar.DATE);
  }

  /**
   * 傳回目前的日期:日(字串)<br>
   * Ex:8 -> "08"
   *
   * @param oCalendar
   * @return
   */
  public static String getDateS(Calendar oCalendar) {
    int _nDate = oCalendar.get(Calendar.DATE);
    String _sDate = String.valueOf(_nDate);
    if (_nDate < 10) _sDate = "0".concat(_sDate);
    return _sDate;
  }

  /**
   * 傳回目前的時間:時<BR>
   * Ex:16:00PM -> 4 (int)
   *
   * @param oCalendar
   * @return
   */
  public static int getHourN(Calendar oCalendar) {
    return oCalendar.get(Calendar.HOUR);
  }

  /**
   * 傳回目前的時間:時<BR>
   * Ex: 8:00AM -> "08"<BR>
   * 16:00PM -> "16"
   *
   * @param oCalendar
   * @return
   */
  public static String getHourS(Calendar oCalendar) {
    int _nHour = oCalendar.get(Calendar.HOUR_OF_DAY);
    String _sHour = String.valueOf(_nHour);
    if (_nHour < 10) _sHour = "0".concat(_sHour);
    return _sHour;
  }

  /**
   * 傳回目前的時間:分<BR>
   * Ex:16:08PM -> 8 (int)
   *
   * @param oCalendar
   * @return
   */
  public static int getMinN(Calendar oCalendar) {
    return oCalendar.get(Calendar.MINUTE);
  }

  /**
   * 傳回目前的時間:分<BR>
   * Ex:16:08PM -> "08"
   *
   * @param oCalendar
   * @return
   */
  public static String getMinS(Calendar oCalendar) {
    int _nMin = oCalendar.get(Calendar.MINUTE);
    String _sMin = String.valueOf(_nMin);
    if (_nMin < 10) _sMin = "0".concat(_sMin);
    return _sMin;
  }

  /**
   * 傳回目前的時間:秒<BR>
   * Ex:16:08:09PM -> 9 (int)
   *
   * @param oCalendar
   * @return
   */
  public static int getSecondN(Calendar oCalendar) {
    return oCalendar.get(Calendar.SECOND);
  }

  /**
   * 傳回目前的時間:秒<BR>
   * Ex:16:08:09PM -> "09"
   *
   * @param oCalendar
   * @return
   */
  public static String getSecondS(Calendar oCalendar) {
    int _nSecond = oCalendar.get(Calendar.SECOND);
    String _sSecond = String.valueOf(_nSecond);
    if (_nSecond < 10) _sSecond = "0".concat(_sSecond);
    return _sSecond;
  }

  /**
   * 以PrintWriter的轉向輸入至檔案中<BR>
   * isAppend = true/false, true表示寫入至檔案的尾端
   *
   * @param sFileName
   * @param isAppend
   * @return
   * @throws Exception
   */
  public static PrintWriter openPrintWriter(String sFileName, boolean isAppend)
    throws Exception {
    FileOutputStream _fOut = new FileOutputStream(sFileName, isAppend);
    BufferedOutputStream _bOut = null;
    PrintWriter _pOut = null;
    try {
      // default size is 512
      _bOut = new BufferedOutputStream(_fOut, 4096);
    } catch (Exception e1) {
      try {
        _fOut.close();
      } catch (Exception ignore1) {
        ;
      }
      e1.fillInStackTrace();
      throw e1;
    }

    try {
      _pOut = new PrintWriter(_bOut, true);
    } catch (Exception e2) {
      try {
        _bOut.close();
      } catch (Exception ignore2) {
      }
      e2.fillInStackTrace();
      throw e2;
    }
    return _pOut;
  }

  /**
   * @param sBaseName
   * @return
   * @throws Exception
   */
  public static ResourceBundle getResourceBundle(String sBaseName) throws Exception {
    return (ResourceBundle) PropertyResourceBundle.getBundle(sBaseName,
      new Locale(LANGUAGE, COUNTRY));
  }

  /**
   *  傳回時間<BR>
   *  oCalendar.set(0,0,0,nHour,nMin,nSecond)
   */
    /* Never used
    private static java.util.Date getTime(int nHour, int nMin, int nSecond) {
        Calendar oCalendar = Calendar.getInstance(LOCALE);
        oCalendar.set(0, 0, 0, nHour, nMin, nSecond);
        return oCalendar.getTime();
    } */

  /**
   * 轉換日期字串為Date型式<BR>
   * 接受以下型式的時間表示<BR>
   * 0881124  <BR>
   * 088/11/24 <BR>
   * 將會自動省略小時以下的單位
   *
   * @param sTimeStr
   * @return
   */
  public static Date strToDate(String sTimeStr) {
    if (sTimeStr == null) return null;
    sTimeStr = sTimeStr.trim();
    if (sTimeStr.equals("")) return null;
    Calendar _oCalendar = getLocaleCalendar();
    if (sTimeStr.indexOf('/') != -1)   // found '/'
      return _SlashStrToDate(sTimeStr, _oCalendar);

    return _StrToDate(sTimeStr, _oCalendar);
  }

  /**
   * 轉換日期字串(Ex:"088/23/25") to Date
   *
   * @param sTimeStr
   * @param calendar
   * @return
   */
  private static Date _SlashStrToDate(String sTimeStr, Calendar calendar) {
    int counter = 2;  // for two '/'
    int lastidx = sTimeStr.length();
    String _tmp = null;
    int[] container = {0, 0, 0};  // 將年,月,日放入container
    for (int idx = sTimeStr.length() - 1; idx >= 0; idx--) {
      if (sTimeStr.charAt(idx) == '/') {
        _tmp = sTimeStr.substring(idx + 1, lastidx);
        lastidx = idx;
        if (counter > 0)
          container[counter--] = Integer.parseInt(_tmp);
        else
          return null;
      }
    }
    _tmp = sTimeStr.substring(0, lastidx);
    if (counter >= 0)
      container[counter] = Integer.parseInt(_tmp) + 1911;
    else
      return null;

    // month is base on 1
    // if month over 12 , the year will +1
    //   year       month        date     hour....
    calendar.set(container[0], container[1] - 1, container[2], 0, 0, 0);
    return calendar.getTime();
  }

  /**
   * 轉換日期字串(Ex:"0882325") to Date
   *
   * @param sTimeStr
   * @param calendar
   * @return
   */
  private static Date _StrToDate(String sTimeStr, Calendar calendar) {
    // we assume date and month must be two digits
    int _len = sTimeStr.length();
    if (_len <= 5) return null; // it must be wrong

    calendar.set(Integer.parseInt(sTimeStr.substring(0, _len - 4)) + 1911, //year
      Integer.parseInt(sTimeStr.substring(_len - 4, _len - 2)) - 1,
      //month, month is based on 0
      Integer.parseInt(sTimeStr.substring(_len - 2, _len)), //date
      0, 0, 0);

    return calendar.getTime();
  }

  /**
   * 傳回今天日期(0890331)
   *
   * @return
   */
  public static String todayDate() {
    return formatDateTime("%Y%M%D", now());
  }

  /**
   * 傳回今天日期(089/03/31)<BR>
   * todayDate("/");<BR>
   * 分別字不可為 %
   *
   * @param seperator
   * @return
   */
  public static String todayDate(String seperator) {
    return formatDateTime("%Y" + seperator + "%M" + seperator + "%D", now());
  }

  /**
   * 傳回美式日期.
   *
   * @return
   */
  public static String todayDateAD() {
    return formatDateTime("%y%M%D", now());
  }

  /**
   * 傳回美式日期, 含分隔字元.
   *
   * @param seperator
   * @return 2004/07/03 Jerry added.
   */
  public static String todayDateAD(String seperator) {
    return formatDateTime("%y" + seperator + "%M" + seperator + "%D", now());
  }

  public static String getWeek(String time) {
    String Week = "";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    try {
      c.setTime(format.parse(time));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    int wek = c.get(Calendar.DAY_OF_WEEK);
    if (wek == 1) {
      Week = "星期日";
    }
    if (wek == 2) {
      Week = "星期一";
    }
    if (wek == 3) {
      Week = "星期二";
    }
    if (wek == 4) {
      Week = "星期三";
    }
    if (wek == 5) {
      Week = "星期四";
    }
    if (wek == 6) {
      Week = "星期五";
    }
    if (wek == 7) {
      Week = "星期六";
    }
    return Week;
  }

  /**
   * 傳回目前時間(13:12)<BR>
   * todayTime(":");<BR>
   * 分別字元不可為 %<BR>
   * 24 小時制
   *
   * @param seperator
   * @return
   */
  public static String todayTime(String seperator) {
    return formatDateTime("%h" + seperator + "%m", now());
  }

  /**
   * 傳回目前時間(1312)<BR>
   * 24 小時制
   *
   * @return
   */
  public static String todayTime() {
    return formatDateTime("%h%m", now());
  }

  /**
   * 傳回目前時間-含秒數(131201)<BR>
   * 24 小時制
   *
   * @return
   */
  public static String todayTimeS() {
    return formatDateTime("%h%m%s", now());
  }

  /**
   * 傳回目前時間-含秒數(131201)<BR>
   * 24 小時制
   *
   * @param needsSeparator 是否需要冒號
   * @return 2004/07/04 Jerry added
   */
  public static String todayTimeS(boolean needsSeparator) {
    if (needsSeparator) {
      return formatDateTime("%h:%m:%s", now());
    } else {
      return formatDateTime("%h%m%s", now());
    }
  }

  /**
   * 將輸入之日期時間格式化輸出<BR>
   *
   * @param sFormat - <BR>
   *                <FONT COLOR=RED>%Y %y %M %D %h %m %s</FONT><BR>
   * @param Time    <br>
   *                注意sFormat參數之大小寫不同 - %Y表民國年，%y表西元年<BR>
   *                Ex:"民國%Y年%M月%D日"<BR>
   *                其中, 民國的年會填滿3位，自動補 "0" <BR>
   *                月日時分秒會填滿2位，自動補 "0" <BR>
   * @param sFormat
   * @param Time
   * @return
   */
  public static String formatDateTime(String sFormat, Date Time) {
    StringBuffer sBuf = new StringBuffer();
    Calendar _oCalendar = getLocaleCalendar();
    _oCalendar.setTime(Time);
    int _nLen = sFormat.length();

    for (int i = 0; i < _nLen; i++) {
      char c = sFormat.charAt(i);
      if (c == '%') {
        if (i == (_nLen - 1)) {
          sBuf.append(c);
          break; // already end of String
        }
        char d = sFormat.charAt(++i);
        if (d == 'Y') {
          sBuf.append(getYearS(_oCalendar));
        } else if (d == 'y') {
          sBuf.append(_oCalendar.get(Calendar.YEAR));
        } else if (d == 'M') {
          // month is 0 based
          sBuf.append(getMonthS(_oCalendar));
        } else if (d == 'D') {
          sBuf.append(getDateS(_oCalendar));
        } else if (d == 'h') {
          sBuf.append(getHourS(_oCalendar));
        } else if (d == 'm') {
          sBuf.append(getMinS(_oCalendar));
        } else if (d == 's') {
          sBuf.append(emisChinese.lpad(String.valueOf(
            _oCalendar.get(Calendar.SECOND)), "0", 2));
        } else {
          // there is no such option , append it all
          sBuf.append(c);
          sBuf.append(d);
        }
      } else {
        sBuf.append(c);
      }
    }
    return sBuf.toString();
  }

  /**
   * 轉換字串之內容<BR>
   *
   * @param Source     - 原始字串<BR>
   * @param OldPattern - 原始字串內欲被取代之字串<BR>
   * @param NewPattern - 取代後的新字串<BR>
   * @param Options    - 'ia' = ignore case sensitive and replace all (可僅取某一字元使用)<BR>
   *                   Default is case sensitive and replace only first occurence<BR>
   *                   Ex:<BR>
   *                   String Test="Test AAA BBB CCC";<BR>
   *                   // default<BR>
   *                   Test = oUtil.stringReplace(Test,"Test","Lala","");<BR>
   *                   // ignorecase and replace all occurence<BR>
   *                   Test = oUtil.stringReplace(Test,"Test","Lala","ia");<BR>
   * @param Source
   * @param OldPattern
   * @param NewPattern
   * @param Options
   * @return
   */
  public static String stringReplace(String Source, String OldPattern, String NewPattern, String Options) {
    if ((OldPattern == null) || (OldPattern.length() == 0))
      return Source;

    if (Options == null)
      Options = "";
    else
      Options = Options.toLowerCase();

    boolean REPLACE_ALL = ((Options.indexOf('a')) != -1) ? true : false;
    boolean REPLACE_ICASE = ((Options.indexOf('i')) != -1) ? true : false;
    String _source = null;
    String _oldpattern = null;
    int oldlen = OldPattern.length();
    int srclen = Source.length();
    if (REPLACE_ICASE) {
      _source = Source.toLowerCase();
      _oldpattern = OldPattern.toLowerCase();
    }
    StringBuffer buf = new StringBuffer();
    int idx = 0;
    while (idx != -1) {
      int pre_idx = idx;
      if (REPLACE_ICASE)
        idx = _source.indexOf(_oldpattern, idx);
      else
        idx = Source.indexOf(OldPattern, idx);

      if (idx != -1) {
        buf.append(Source.substring(pre_idx, idx));
        buf.append(NewPattern);
        idx = idx + oldlen;
      } else {
        if (pre_idx < srclen)
          buf.append(Source.substring(pre_idx));
      }
      if (!REPLACE_ALL) {
        if (idx != -1) {
          if (idx < srclen)
            buf.append(Source.substring(idx));
        }
        break;
      }
    }
    return buf.toString();
  }

  /**
   * 將浮點數字串依指定長度與小數點位數填成等長之字串.<BR>
   * 小數會四捨五入
   *
   * @param sNumber 數字字串<BR>
   * @param iMaxLen <BR>
   * @param iDigit  小數點位數
   * @return
   * @throws Exception
   */
  public static String stringRound(String sNumber, int iMaxLen, int iDigit) throws Exception {
    double d = Double.parseDouble(sNumber);
    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMaximumFractionDigits(iDigit);
    nf.setMinimumFractionDigits(iDigit);
    nf.setGroupingUsed(false);
    sNumber = nf.format(d);
    return emisChinese.lpad(sNumber, iMaxLen);
  } // stringRound( )

  /**
   * @param fNumber
   * @param iMaxLen
   * @param iDigit
   * @return
   * @throws Exception
   */
  public static String stringRound(float fNumber, int iMaxLen, int iDigit) throws Exception {
    return stringRound(String.valueOf(fNumber), iMaxLen, iDigit);
  }

  /**
   * 取得Server IP
   *
   * @return
   */
  public static String getServerIP() {
    try {
      InetAddress addr = InetAddress.getLocalHost();
      return addr.getHostAddress();
    } catch (java.net.UnknownHostException e) {
      return "127.0.0.1";
    }
  }

  /**
   * 轉換輸入之字串(以','隔開)為Vector型態<BR>
   * Ex:tokenizer("123,456,789") => [123,456,789]
   *
   * @param sStr
   * @return
   */
  public static Vector tokenizer(String sStr) {
    if (sStr == null) return null;

    Vector _oData = new Vector();
    int _nIdx = sStr.indexOf(",");
    while (_nIdx != -1) {
      _oData.add(sStr.substring(0, _nIdx).trim());
      sStr = sStr.substring(_nIdx + 1);
      _nIdx = sStr.indexOf(",");
    }
    if (!"".equals(sStr)) {
      _oData.add(sStr.trim());
    }
    if (_oData.size() > 0) return _oData;
    return null;
  }


  /**
   * 將 sStr 中的 "<" , ">" , "&" ,"'" , """ 等符號<BR>
   * 轉成 "&lt;" ,"&gt;" ,"&amp;" ,"&apos;" , "&quot;" 等<BR>
   * xml 的 escape
   *
   * @param sStr
   * @return
   */
  public static String escapeXMLString(String sStr) {
    StringBuffer buf = new StringBuffer(sStr);
    int i = 0;
    while (i < buf.length()) {
      char c = buf.charAt(i);
      if (c == '&') {
        buf.deleteCharAt(i);
        buf.insert(i, "&amp;");
        i += 5;
        continue;
      } else if (c == '<') {
        buf.deleteCharAt(i);
        buf.insert(i, "&lt;");
        i += 4;
        continue;
      } else if (c == '>') {
        buf.deleteCharAt(i);
        buf.insert(i, "&gt;");
        i += 4;
        continue;
      } else if (c == '\'') {
        buf.deleteCharAt(i);
        buf.insert(i, "&apos;");
        i += 6;
        continue;
      } else if (c == '"') {
        buf.deleteCharAt(i);
        buf.insert(i, "&quot;");
        i += 6;
        continue;
      }
      i++;
    }
    return buf.toString();
  }

  /**
   * 轉換字串為ISO8859_1型態
   *
   * @param str
   * @return
   */
  public static String sysToISO(String str) {
    try {
      if (str != null) {
        str = new String(str.getBytes(FILENCODING), "ISO8859_1");
      }
    } catch (Exception ignore) {
      ;
    }
    return str;
  }

  /**
   * 字串補空白<BR>
   *
   * @param sStr       - string<BR>
   * @param nAlign     - emisReport.A_LEFT/A_CENTER/A_RIGHT<BR>
   * @param nTotalSize - padding後之字串長度
   * @return
   */
  public static String padding(String sStr, int nAlign, int nTotalSize) {
    if (sStr == null)
      sStr = "";
    return emisUTF8StringUtil.padding(sStr, nAlign, nTotalSize);
  }

  /**
   * 傳回今天日期<BR>
   * Ex:090/01/05<BR>
   * PS:功能和todayDate()一樣
   *
   * @param sSeparator
   * @return
   */
  public static String getFullDate(String sSeparator) {
    return formatDateTime("%Y" + sSeparator + "%M" + sSeparator + "%D", now());
  }


  /**
   * Execute float a/b，if b==0 then return fDefault
   *
   * @param a
   * @param b
   * @param fDefault
   * @return
   */
  public static float divide(float a, float b, float fDefault) {
    if (b == 0) return fDefault;
    return a / b;
  }

  /**
   * Execute double a/b，if b==0 then return dDefault
   *
   * @param a
   * @param b
   * @param dDefault
   * @return
   */
  public static double divide(double a, double b, double dDefault) {
    if (b == 0) return dDefault;
    return a / b;
  }

  /**
   * Execute int a/b，if b==0 then return nDefault
   *
   * @param a
   * @param b
   * @param nDefault
   * @return
   */
  public static int divide(int a, int b, int nDefault) {
    try {
      return a / b;
    } catch (Exception ignore) {
      ;
    }
    return nDefault;
  }

  /**
   * Execute long a/b，if b==0 then return nDefault
   *
   * @param a
   * @param b
   * @param nDefault
   * @return
   */
  public static long divide(long a, long b, long nDefault) {
    try {
      return a / b;
    } catch (Exception ignore) {
      ;
    }
    return nDefault;
  }

  /**
   * 隨機取出一個INT變數(可為負值)
   */
  private static Random oRand = new Random(System.currentTimeMillis());

  /**
   * @return
   */
  public static int random() {
    return oRand.nextInt();
  }

  /**
   * @param e
   * @return
   */
  public static String getStackTrace(Exception e) {
    if (e == null) return "";
    PrintWriter pw = null;
    try {
      StringWriter sw = new StringWriter();
      pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      return sw.toString();
    } catch (Exception ignore) {
      ;
    } finally {
      if (pw != null) pw.close();
    }
    return e.getMessage();
  }

  // used only for log forging
  public static String logForgingReplace(String info) {
    //info = info.replaceAll("\r", "_"); // Log Forging
    //info = info.replaceAll("\n", "_"); // Log Forging
    StringBuffer sb = new StringBuffer();
    int crc = 0;
    for (int i = 0; i < info.length(); i++) {
      char c = info.charAt(i);
      if (c == '\r') {
        c = '_';
      } else if (c == '\n') {
        c = '_';
      }
      crc = (crc + c) % 10;
      sb.append(c);
    }
    sb.append(" ");
    sb.append(crc); // CRC
    return sb.toString();
  }

  /**
   * 傳回 EAN 的檢查碼，目前只實作 EAN13 碼<BR>
   * PS:參數二"sType"沒使用到
   *
   * @param sPNo
   * @param sType
   * @return
   */
  public static String getEANCheckBit(String sPNo, String sType) {
    sPNo = emisChinese.lpad(sPNo, "0", 12);
    char[] list = sPNo.toCharArray();
    int _iChk = 0;
    for (int _iCnt = 0; _iCnt <= 10; _iCnt += 2) {
      char c = list[_iCnt];
      _iChk = _iChk + (c - '0');
      c = list[_iCnt + 1];
      _iChk = _iChk + ((c - '0') * 3);
    }
    _iChk %= 10;
    return ((_iChk == 0) ? "0" : String.valueOf(10 - _iChk));
  }

  /**
   * 系統公用加密 Tool function<BR>
   * 不可修改...
   *
   * @param s
   * @return
   * @throws Exception
   */
  public static String digest(String s) throws Exception {
    if (s == null) throw new Exception("can't digest null String");
    MessageDigest m = MessageDigest.getInstance("sha");  // MD5 or SHA algorithm
    byte[] d = m.digest(s.getBytes());
    StringBuffer b = new StringBuffer();
    int bLen = d.length;
    for (int i = 0; i < bLen; i++) {
      int j = (int) d[i];
      if (j < 0) j = -j;
      b.append(Integer.toHexString(j));
    }
    return b.toString().toUpperCase();
  }


  /**
   * byteToStr 將 byte array 轉換成 unsigned byte 的字串 (0~255)
   * 原本 Java 是 -127 ~ +128
   *
   * @param b
   * @return
   * @throws Exception
   */
  public static String byteToStr(byte[] b) throws Exception {
    if (b == null) return null;
    int len = b.length;
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < len; i++) {
      int bValue = (int) b[i];
      if (bValue < 0) bValue = bValue + 256;
      if (bValue < 16) // 少了一位
        buf.append("0");
      buf.append(Integer.toHexString(bValue).toUpperCase());
    }
    return buf.toString();
  }

  /**
   * 將 byteToStr 的字串轉回 byte array
   *
   * @param sStr
   * @return
   * @throws Exception
   */
  public static byte[] strToByte(String sStr) throws Exception {
    if (sStr == null) return null;
    int len = sStr.length();
    if ((len % 2) != 0)
      throw new Exception("Error Length in Input String");
    byte[] b = new byte[len / 2];
    int j = 0;
    for (int i = 0; i < len; i += 2) {
      String sValue = sStr.substring(i, i + 2);
      int nValue = Integer.parseInt(sValue, 16);
      if (nValue > 128)
        nValue = nValue - 256;
      b[j++] = (byte) nValue;

    }
    return b;
  }

  /**
   * 將 byte array 轉成 hex String, 如 byte [] b = { 0xff, 0x1c };
   * 則回傳字串 = "ff1c";.
   *
   * @param b
   * @return
   */
  public static String EncodeByteToStr(byte[] b) {
    // a byte is a value between -128 and 127
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < b.length; i++) {
      int bValue = (int) b[i];
      if (bValue < 0)
        bValue = (-bValue) + 127;
      if (bValue < 16)
        sb.append("0").append(Integer.toHexString(bValue));
      else
        sb.append(Integer.toHexString(bValue));
    }
    return sb.toString();
  }

  /**
   * 將 byte array 轉成 hex String
   * 如 byte [] b = { 0xff, 0x1c };
   * 則回傳字串 = "ff1c";
   *
   * @param sStr
   * @return
   */
  public static byte[] DecodeStrToByte(String sStr) {
    int len = sStr.length();
    byte b[] = new byte[len / 2];
    int j = 0;
    for (int i = 0; (i + 1) < len; i += 2) {
      String sByte = sStr.substring(i, i + 2);
      int nByte = Integer.parseInt(sByte, 16);
      if (nByte > 127)
        nByte = (-nByte) + 127;
      b[j++] = (byte) nByte;
    }
    return b;
  }

  /**
   * 抓取輸入date 的第一天  每週的第一天為星期一
   *
   * @param Fromat yyyyMMdd
   * @param sDate  2004/01/01  需要有分隔字串
   * @return 依照format 回傳
   * @throws Exception
   */
  public static String getFirstDayOfWeek(String Fromat, String sDate) throws Exception {
    Calendar calendar = new GregorianCalendar();
    SimpleDateFormat formatter = new SimpleDateFormat(Fromat);
    calendar.setTime(DateFormat.getDateInstance().parse(sDate));
    int currentDiff = calendar.get(Calendar.DAY_OF_WEEK);
    if (currentDiff == 1) {
      currentDiff = -6;
    } else {
      currentDiff = (currentDiff - 2) * -1;
    }
    calendar.add(Calendar.DATE, currentDiff);

    return formatter.format(calendar.getTime());
  }

  /**
   * 回傳輸入該週有的星期一到星期日的日期
   *
   * @param sDate 2004/01/01
   * @return
   * @throws Exception
   */
  public static String[] getDayOfWeekArray(String sDate) throws Exception {

    String ret[] = new String[7];
    Calendar calendar = new GregorianCalendar();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    calendar.setTime(DateFormat.getDateInstance().parse(getFirstDayOfWeek("yyyy/MM/dd", sDate)));
    for (int i = 0; i < 7; i++) {
      calendar.add(Calendar.DATE, i);
      ret[i] = formatter.format(calendar.getTime());
      calendar.add(Calendar.DATE, i * -1);
    }
    return ret;
  }

  public static boolean copyTo(String sDest, String sSource) throws Exception {
    FileInputStream fin = new FileInputStream(sSource);
    try {
      FileOutputStream fout = new FileOutputStream(sDest, false);
      try {
        fout.getChannel().tryLock();
        byte buf[] = new byte[4096];
        int readed;
        while ((readed = fin.read(buf)) != -1) {
          fout.write(buf, 0, readed);
        }
      } finally {
        fout.close();
      }
    } finally {
      fin.close();
    }
    return true;
  }

  public static String getDayOfWeekString(String sDate) throws Exception {

    String ret = "";
    Calendar calendar = new GregorianCalendar();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    calendar.setTime(DateFormat.getDateInstance().parse(getFirstDayOfWeek("yyyy/MM/dd", sDate)));
    for (int i = 0; i < 7; i++) {
      calendar.add(Calendar.DATE, i);
      ret += formatter.format(calendar.getTime()) + ",";
      calendar.add(Calendar.DATE, i * -1);
    }
    return ret.substring(0, ret.length() - 1);
  }

  public static File copyFile(File SourceFile, String sTargetPath) throws Exception {
    File oFile = new File(sTargetPath);
    if (!oFile.exists()) oFile.mkdirs();

    oFile = new File(sTargetPath, SourceFile.getName());
    FileOutputStream os = new FileOutputStream(oFile);
    try {
      BufferedOutputStream bos = new BufferedOutputStream(os);
      FileInputStream is = new FileInputStream(SourceFile);
      try {
        byte[] buf = new byte[8192];
        int readed;
        BufferedInputStream bis = new BufferedInputStream(is);
        while ((readed = bis.read(buf)) != -1) {
          bos.write(buf, 0, readed);
        }
        bos.flush();
      } finally {
        is.close();
        is = null;
      }
    } finally {
      os.close();
      os = null;
    }
    return oFile;
  }


  /**
   * Track+[19156] dana 2012/02/10  add增加計算未稅金額,稅額,含稅金額的公共方法,方便報表中調用.
   *
   * @param sF_TAX       : 稅別  2:內含 ; 1:外加 ; 3: 免稅
   * @param dTAX         : 稅率
   * @param dAMT         : 單據金額 default = 0
   * @param dP_DISC_RATE : 折扣比率  default = 0
   * @param dP_SDISC_AMT : 折讓金額  default = 0
   * @return : 未稅金額, 稅額, 含稅金額
   */
  public static long[] countTAX(String sF_TAX, double dTAX, double dAMT, int dP_DISC_RATE, double dP_SDISC_AMT) {
    long dF_WOTAXAMT = 0;  //未稅金額
    long dF_TAXAMT = 0;    //稅    額
    long dF_AMT = 0;       //含稅金額
//alert('countTax:' + iF_TAX + ':' + dTAX + ':' + dAMT );
    if (dAMT == 0) {
      return new long[]{0, 0, 0};
    }


    if (sF_TAX.equals("1")) {  //外加
      dF_WOTAXAMT = Math.round(dAMT * (100 - dP_DISC_RATE) / 100 - dP_SDISC_AMT);
      dF_TAXAMT = Math.round(dF_WOTAXAMT * dTAX);
      dF_AMT = dF_WOTAXAMT + dF_TAXAMT;
    } else if (sF_TAX.equals("2")) {  //內含
      dF_AMT = Math.round(dAMT * (100 - dP_DISC_RATE) / 100 - dP_SDISC_AMT);
      dF_TAXAMT = Math.round((dF_AMT / (1 + dTAX)) * dTAX);
      dF_WOTAXAMT = dF_AMT - dF_TAXAMT;
    } else if (sF_TAX.equals("3")) { //免稅
      dF_TAXAMT = 0;
      dF_WOTAXAMT = dF_AMT = Math.round(dAMT * (100 - dP_DISC_RATE) / 100 - dP_SDISC_AMT);
    } else {
      return new long[]{Math.round(dAMT), 0, 0}; // 代收 ?
    }
    return new long[]{dF_WOTAXAMT, dF_TAXAMT, dF_AMT};
  }

  /**
   * 將半角特殊字符替換成‘’
   *
   * @param sValue 七彩藝石┐B18*46.5 (┐為全角,此符號不會被替換且對系統沒有影響，為半角時會被替換成‘’)
   * @return 七彩藝石B18*46.5
   */
  public static String replaceSpecialChar(String sValue) {
    String returnvalue = sValue.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
    return returnvalue;
  }

  /*
   * Robert, 2014/02/21
   * a unify function to test if we are in TOMCAT
   */
  static int m_isTomcat = -1;

  public static boolean isTomcat() {
    if (m_isTomcat == -1) {
      String catalina = System.getProperty("catalina.base");
      if ((catalina != null) && !"".equals(catalina)) {
        m_isTomcat = 1;
        return true;
      } else {
        m_isTomcat = 0;
        return false;
      }
    } else {
      if (m_isTomcat == 1) return true;
      return false;
    }
  }


  public static Object simpleInvoke(Object obj, String methodName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Method method = obj.getClass().getMethod(methodName);
    return method.invoke(obj);
  }

  /**
   * Track+[27225] water.lin 2014/09/15 [JHDS2] 新增標準版一個取代函數
   * 將emisCond (replace屬性), emisCondition使用的SQL轉碼. Only for SQL statement.
   *
   * @param sValue
   * @return
   */
  public static String replaceQuote(String sValue) {
    sValue = sValue.replace("&amp;#x27;", "'");
    sValue = sValue.replace("&amp;gt;", ">");
    sValue = sValue.replace("&gt;", ">");
    sValue = sValue.replace("&amp;lt;", "<");
    sValue = sValue.replace("&lt;", "<");
    sValue = sValue.replace("''", "'");
    return sValue;
  }

  /**
   * 轉換ROC Date或西元日期為西元日期加斜線.
   *
   * @param sDate
   * @return
   */
  public static String convertDate(String sDate) {
    String _sDate = "";
    if (sDate.length() == 0) return "";
    else if (sDate.length() == 7 && sDate.startsWith("1")) {  // ROC Date, "1081231"
      int _iYear = parseInt(sDate.substring(0, 3)) + 1911;
      _sDate = _iYear + "/" + sDate.substring(3, 5) + "/" + sDate.substring(5, 7);
    } else if (sDate.length() == 8 && sDate.startsWith("2")) {  // WesternC Date, "20191231"
      _sDate = sDate.substring(0, 4) + "/" + sDate.substring(4, 6) + "/" + sDate.substring(6);
    } else if (sDate.length() == 9 && sDate.startsWith("1")) {  // ROC Date, "108/12/31"
      int _iYear = parseInt(sDate.substring(0, 3)) + 1911;
      _sDate = _iYear + sDate.substring(3);
    } else if (sDate.length() == 10 && sDate.startsWith("20")) {  // Western Date, "20191231"
      _sDate = sDate;
    }
    return _sDate;
  }

  private static DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);

  public static String getNumberWithComma(int iValue) {
    formatter.applyPattern("#,###,###,###");
    String formattedString = formatter.format(iValue);
    return formattedString;
  }

  public static String getNumberWithComma(double value) {
    formatter.applyPattern("#,###,###,###");

    return formatter.format(value);
  }

  public static ArrayList mapArrayStrToMap(String text) {
    ArrayList mapArray = new ArrayList();

    Pattern p = Pattern.compile("[\\{\\}]++");
    String[] split = p.split(text);
    if (text.length() <= 1) {
      return mapArray;
    }
    for (int i = 0; i < split.length; i = i + 2) {
      String p1 = split[i + 1];
      Map<String, Object> map = mapStringToMap(p1);
      mapArray.add(map);
    }

    return mapArray;
  }

  public static Map mapStringToMap(String text) {
    HashMap<String, Object> data = new HashMap<String, Object>();
    Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
    String[] split = p.split(text);
    for (int i = 0; i + 2 <= split.length; i += 2) {
      data.put(split[i], split[i + 1]);
    }
    return data;
  }

  public static String getJsonString(org.json.JSONObject json, String name) {
    String result = "";

    try {
      result = json.getString(name);
    } catch (Exception e) {
      emisLog.addLog(e.getLocalizedMessage());
    }

    return result;
  }

  public static String getSL_PLATFORM_NAME(String SL_PLATFORM) {
    switch (SL_PLATFORM) {
      case "1":
        return "UberEats";
      case "2":
        return "FoodPanda";
      case "3":
        return "你訂";
      case "5":
        return "Louisa";
      case "6":
        return "一芳";
      default:
        return SL_PLATFORM;
    }
  }

  public static void playAlertTone(final Context context) {
    Thread t = new Thread() {
      public void run() {
        MediaPlayer player = null;
        int countBeep = 0;
        while (countBeep < 2) {
          player = MediaPlayer.create(context, R.raw.alert_new);
          player.start();
          countBeep += 1;
          try {
            // 100 milisecond is duration gap between two beep
            Thread.sleep(player.getDuration() + 100);
            player.release();
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    };

    t.start();
  }

  // 取得四碼隨機碼
  public static String generateRandom() {
    String result = "";

    Random r = new Random();
    result += r.nextInt(10);
    result += r.nextInt(10);
    result += r.nextInt(10);
    result += r.nextInt(10);

    return result;
  }

  public static Activity getRunningActivity() {
    Activity result = null;
    try {
      Class activityThreadClass = Class.forName("android.app.ActivityThread");
      Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);

      Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
      activitiesField.setAccessible(true);

      ArrayMap activities = (ArrayMap) activitiesField.get(activityThread);
      for (Object activityRecord : activities.values()) {

        Class activityRecordClass = activityRecord.getClass();
        Field pausedField = activityRecordClass.getDeclaredField("paused");
        pausedField.setAccessible(true);

        if (!pausedField.getBoolean(activityRecord)) {
          Field activityField = activityRecordClass.getDeclaredField("activity");
          activityField.setAccessible(true);

          result = (Activity) activityField.get(activityRecord);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // throw new RuntimeException("Didn't find the running activity");
    return result;
  }

}
