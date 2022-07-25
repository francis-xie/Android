package com.emis.venus.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time {
  public static String getDate() {
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    String date = sdfDate.format(new Date());
    return date;
  }

  public static String getDateType2() {
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
    String date = sdfDate.format(new Date());
    return date;
  }

  public static String getOnlyDate() {
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
    String date = sdfDate.format(new Date());
    return date;
  }

  public static String getTime() {
    SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
    String time = sdfDate.format(new Date());
    return time;
  }

  public static String getOnlyTime() {
    SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmss");
    String time = sdfDate.format(new Date());
    return time;
  }

  public static String getMonth() {
    SimpleDateFormat sdfDate = new SimpleDateFormat("MM");
    String time = sdfDate.format(new Date());
    return time;
  }

  public static String getDay() {
    SimpleDateFormat sdfDate = new SimpleDateFormat("dd");
    String time = sdfDate.format(new Date());
    return time;
  }

  public static String getYesterday() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1);
    String yesterday = new SimpleDateFormat("yyyyMMdd ").format(cal.getTime());
    return yesterday;
  }

  public static String getMTime() {
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM");
    String time = sdfDate.format(new Date());
    String[] data = time.split("-");
    String year = Integer.toString(Integer.valueOf(data[0]) - 1911);
    String month;
    switch (data[1]) {
      case "01":
        month = "01-02";
        break;
      case "02":
        month = "01-02";
        break;
      case "03":
        month = "03-04";
        break;
      case "04":
        month = "03-04";
        break;
      case "05":
        month = "05-06";
        break;
      case "06":
        month = "05-06";
        break;
      case "07":
        month = "07-08";
        break;
      case "08":
        month = "07-08";
        break;
      case "09":
        month = "09-10";
        break;
      case "10":
        month = "09-10";
        break;
      case "11":
        month = "11-12";
        break;
      case "12":
        month = "11-12";
        break;
      default:
        month = "00-00";
        break;

    }
    return year + "年" + month + "月";
  }

  public static String getSysTime() {
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String date = sdfDate.format(new Date());
    return date;
  }
}
