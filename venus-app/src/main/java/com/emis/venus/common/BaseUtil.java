package com.emis.venus.common;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisSQLiteWrapper;
import com.emis.venus.util.AES;
import com.emis.venus.util.emisAndroidUtil;
import com.emis.venus.util.emisLog;
import com.emis.venus.util.emisUtil;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import okhttp3.*;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class BaseUtil {

  public static final int LOGIN_ACT = 1;
  public static final int SALES_CASH_ACT = 10;
  public static final int SALES_SKU_ACT = 20;
  public static final int PAY_ACT = 22;
  public static final int SCAN_ACT_SUNMI = 24;
  public static final int SCAN_ACT_A920 = 25;
  public static final int SCAN_PAY_ACT = 25;
  public static final int SALES_FAST_FOOD_ACT = 30;
  public static final int SALES_READ_ACT = 40;
  public static final int SALES_CLEAR_ACT = 50;
  public static final int SALES_REFUND_ACT = 80;
  public static final int REPORT_ACT = 90;
  public static final int REPRINT_ACT = 91;
  public static final int FAST_FOOD_ACT = 70;
  // public static final int SETTINGS_ACT = 900;
  public static final int CLEAR_ACT = 50;
  public static final int SEND_OUT_ACT = 23;
  public static final int EXIT_NOW = 999;

  private static String sS_NO_ = null;
  private static String sS_NAME_ = null;
  private static String sID_NO_ = null;
  private static String sUserID_ = null;
  private static String sToday_ = emisUtil.todayDateAD();
  private static SQLiteStatement stmtInsertSaleD_ = null;
  private static SQLiteStatement stmtInsertSaleH_ = null;
  private static SQLiteStatement stmtInsertSaleDis_ = null;
  private static SQLiteStatement stmtInsertSaleP_ = null;
  private static SQLiteStatement stmtInsertSaleS_ = null;

  public static void setsToday(String sToday) {
    sToday_ = sToday;
  }

  public static String today() {
    return sToday_;
  }

  public static void setLoginData(String sUserID, String sS_NO, String sS_NAME, String sID_NO) {
    BaseUtil.setStoreNo(sS_NO);
    BaseUtil.setStoreName(sS_NAME);
    BaseUtil.setCashRegisterID(sID_NO);
    BaseUtil.setUserID(sUserID);
  }

  public static String getStoreNo() {
    return sS_NO_;
  }

  public static void setStoreNo(String sS_NO) {
    BaseUtil.sS_NO_ = sS_NO;
  }

  public static String getStoreName() {
    return sS_NAME_;
  }

  public static void setStoreName(String sS_NAME) {
    BaseUtil.sS_NAME_ = sS_NAME;
  }

  public static String getCashRegisterID() {
    return sID_NO_;
  }

  public static void setCashRegisterID(String sID_NO) {
    BaseUtil.sID_NO_ = sID_NO;
  }

  public static String getUserID() {
    return sUserID_;
  }

  public static void setUserID(String sUserID) {
    BaseUtil.sUserID_ = sUserID;
  }

  public static String getRandom() {
    int min = 1000;
    int max = 9999;
    Random random = new Random();
    int num = random.nextInt(max) % (max - min + 1) + min;
    return Integer.toString(num);
  }

  public static int getMaxSL_NO(final String sNo, final String iDNo, final String slDate) throws Exception {
    emisSQLiteWrapper db = emisDb.getInstance();

    int result = -1;
    try {
      db.compileStmt("select max(sl_no) as SL_NO from SALE_H where (1 = 1) and (S_NO = ?) and (ID_NO = ?) and (SL_DATE = ?) ");
      db.setString(1, sNo);
      db.setString(2, iDNo);
      db.setString(3, slDate);

      result = emisUtil.parseInt(db.executeQueryString());
    } finally {
      if (db != null) db.close();
    }
    return result;
  }

  public static String getNextSL_NO(final String sNo, final String iDNo, final String slDate) throws Exception {
    int _iValue = getMaxSL_NO(sNo, iDNo, slDate) + 1;

    System.out.println("MaxSL_NO:" + _iValue);

    String _sValue = "" + _iValue;
    _sValue = "000".substring(0, (4 - _sValue.length())) + _sValue;

    return _sValue;
  }

  public static double getSaleSum() {
    double result = 0;

    emisSQLiteWrapper db = emisDb.getInstance();
    try {
      db.compileStmt("select sum(sl_amt) from sale_h where eo_date='' ");
      result = (double) db.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (db != null) db.close();
    }
    return result;
  }

  public static double getSaleSum(String EO_DATE, String EO_TIME) {
    double result = 0;

    emisSQLiteWrapper db = emisDb.getInstance();
    try {
      db.compileStmt("select sum(sl_amt) from sale_h where eo_date='" + EO_DATE + "' and eo_time='" + EO_TIME + "' ");
      result = (double) db.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (db != null) db.close();
    }
    return result;
  }

  public static int getSaleNum() {
    int num = 0;
    String SQL = "select count(*) from sale_h " +
      "where S_NO=? and ID_NO=? and eo_date=''";

    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);
      _oDb.setString(1, BaseUtil.getStoreNo());
      _oDb.setString(2, BaseUtil.getCashRegisterID());
      num = (int) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return num;
  }

  public static int getSaleNum(String EO_DATE, String EO_TIME) {
    int num = 0;
    String SQL = "select count(*) from sale_h " +
      "where S_NO=? and ID_NO=? and eo_date='" + EO_DATE + "' and eo_time='" + EO_TIME + "'";

    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);
      _oDb.setString(1, BaseUtil.getStoreNo());
      _oDb.setString(2, BaseUtil.getCashRegisterID());
      num = (int) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return num;
  }

  public static String getMinInvno() {
    String result = "";

    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteCursor cursor;
    try {
      // cursor = db.executeQuery("select min(SL_INVNO_S) as SL_INVNO_S from SALE_H where (1 = 1) and (SL_INVNO_S != '00000000') and (eo_date = '') ");
      cursor = db.executeQuery("select SL_INVNO_S from SALE_H where (1 = 1) and (SL_INVNO_S != '00000000') and (eo_date = '') order by SL_INVNO_S asc ");

      // if (cursor.moveToNext()) result = cursor.getString(cursor.getColumnIndex("SL_INVNO_S"));
      if (cursor.moveToFirst()) result = cursor.getString(cursor.getColumnIndex("SL_INVNO_S"));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (db != null) db.close();
    }
    return result;
  }

  public static String getMinInvno(String EO_DATE, String EO_TIME) {
    String result = "";

    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteCursor cursor;
    try {
      // cursor = db.executeQuery("select min(SL_INVNO_S) as SL_INVNO_S from SALE_H where (1 = 1) and (SL_INVNO_S != '00000000') and (eo_date = '') ");
      cursor = db.executeQuery("select SL_INVNO_S from SALE_H where (1 = 1) and (SL_INVNO_S != '00000000') and (eo_date = '" + EO_DATE + "')and (eo_time='" + EO_TIME + "') order by SL_INVNO_S asc ");

      // if (cursor.moveToNext()) result = cursor.getString(cursor.getColumnIndex("SL_INVNO_S"));
      if (cursor.moveToFirst()) result = cursor.getString(cursor.getColumnIndex("SL_INVNO_S"));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (db != null) db.close();
    }
    return result;
  }

  public static String getMaxInvno() {
    String result = "";

    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteCursor cursor;
    try {
      // cursor =  db.executeQuery("select max(SL_INVNO_S) as SL_INVNO_S from SALE_H where (1 = 1) and (eo_date = '') ");
      cursor = db.executeQuery("select SL_INVNO_S from SALE_H where (1 = 1) and (eo_date = '') order by SL_INVNO_S asc ");

      if (cursor.moveToLast()) result = cursor.getString(cursor.getColumnIndex("SL_INVNO_S"));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (db != null) db.close();
    }
    return result;
  }

  public static String getMaxInvno(String EO_DATE, String EO_TIME) {
    String result = "";

    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteCursor cursor;
    try {
      // cursor =  db.executeQuery("select max(SL_INVNO_S) as SL_INVNO_S from SALE_H where (1 = 1) and (eo_date = '') ");
      cursor = db.executeQuery("select SL_INVNO_S from SALE_H where (1 = 1) " +
        "and (eo_date = '" + EO_DATE + "')and (eo_time='" + EO_TIME + "') order by SL_INVNO_S asc ");

      if (cursor.moveToLast()) result = cursor.getString(cursor.getColumnIndex("SL_INVNO_S"));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (db != null) db.close();
    }
    return result;
  }

  public static boolean checkInvno(String invno) {
    boolean exist = false;
    String SQL = "Select count(1) from invoice " +
      "where ? >= INV_NO_S and ?<=INV_NO_E";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      int count = 0;
      _oDb.compileStmt(SQL);
      _oDb.setString(1, invno);
      _oDb.setString(2, invno);
      count = _oDb.executeQuery();
      if (count >= 1) {
        exist = true;
      } else {
        exist = false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return exist;
  }

  public static boolean checkInvnoExist(String invno) {
    boolean exist = false;
    String SQL = "Select count(1) from sale_h " +
      "where sl_invno_s =?";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);
      _oDb.setString(1, invno);
      int count = _oDb.executeQuery();
      if (count == 1) {
        exist = true;
      } else {
        exist = false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return exist;
  }

  public static double getCashSum() {
    double sum = 0;
    // String SQL = "select sum(pay_amt)as pay_amt from sale_p where SL_DATE=? and pay_no='01' and SL_KEY =(select SL_KEY from sale_h where eo_date='')";
    String SQL = "select sum(p.pay_amt) from sale_p as p inner join sale_h where p.pay_no='01' and p.sl_key=sale_h.sl_key and sale_h.eo_date='' ";

    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);
      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getCashSum(String EO_DATE, String EO_TIME) {
    double sum = 0;
    // String SQL = "select sum(pay_amt)as pay_amt from sale_p where SL_DATE=? and pay_no='01' and SL_KEY =(select SL_KEY from sale_h where eo_date='')";
    String SQL = "select sum(p.pay_amt) from sale_p as p inner join sale_h where " +
      "p.pay_no='01' and p.sl_key=sale_h.sl_key " +
      "and sale_h.eo_date='" + EO_DATE + "' and sale_h.eo_time='" + EO_TIME + "' ";

    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);
      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getCreditSum() {
    double sum = 0;
    String SQL = "select sum(pay_amt)as pay_amt from sale_p " +
      "where SL_DATE=? and pay_no='02'";
    SQL = "select sum(p.pay_amt) from sale_p as p inner join sale_h " +
      "where p.pay_no='02' and p.sl_key=sale_h.sl_key and sale_h.eo_date=''";

    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);
      // _oDb.setString(1, BaseUtil.today());

      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getCreditSum(String EO_DATE, String EO_TIME) {
    double sum = 0;
    String SQL = "select sum(p.pay_amt) from sale_p as p inner join sale_h " +
      "where p.pay_no='02' and p.sl_key=sale_h.sl_key " +
      "and sale_h.eo_date='" + EO_DATE + "' and sale_h.eo_time='" + EO_TIME + "'";

    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);
      // _oDb.setString(1, BaseUtil.today());

      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getElecSum() {
    double sum = 0;
    String SQL = "select sum(p.pay_amt) from sale_p as p inner join sale_h " +
      "where (p.pay_no !='02' and p.pay_no!='01') and p.sl_key=sale_h.sl_key and sale_h.eo_date=''";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);
      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getElecSum(String EO_DATE, String EO_TIME) {
    double sum = 0;
    String SQL = "select sum(p.pay_amt) from sale_p as p inner join sale_h " +
      "where (p.pay_no !='02' and p.pay_no!='01') and p.sl_key=sale_h.sl_key and sale_h.eo_date=''";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);
      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getTaxSum() {
    double sum = 0;

    String sql = "select sum(sl_taxamt)as tax_amt from sale_h" +
      " where sl_taxamt>0 and eo_date=''";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(sql);
      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getTaxSum(String EO_DATE, String EO_TIME) {
    double sum = 0;

    String sql = "select sum(sl_taxamt)as tax_amt from sale_h" +
      " where sl_taxamt>0 and eo_date='" + EO_DATE + "' and eo_time='" + EO_TIME + "'";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(sql);
      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getFreeSum() {
    double sum = 0;

    String sql = "select sum(sl_notaxamt)as tax_amt from sale_h" +
      " where sl_notaxamt>0 and eo_date=''";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(sql);

      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getFreeSum(String EO_DATE, String EO_TIME) {
    double sum = 0;

    String sql = "select sum(sl_notaxamt)as tax_amt from sale_h" +
      " where sl_notaxamt>0 and eo_date='" + EO_DATE + "' and eo_time='" + EO_TIME + "'";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(sql);

      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getDiscSum() {
    double sum = 0;
    String SQL = "select sum(disc_amt)as disc_amt from sale_dis " +
      "where disc_date=? and disc_amt>0 and SL_KEY =(select SL_KEY from sale_h where eo_date='')";
    SQL = "select sum(dis.disc_amt) from sale_dis as dis inner join sale_h " +
      "where  dis.disc_amt>0 and dis.sl_key=sale_h.sl_key and sale_h.eo_date=''";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);

      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getDiscSum(String EO_DATE, String EO_TIME) {
    double sum = 0;
    String SQL = "select sum(dis.disc_amt) from sale_dis as dis inner join sale_h " +
      "where  dis.disc_amt>0 and dis.sl_key=sale_h.sl_key and sale_h.eo_date='" + EO_DATE + "' and sale_h.eo_time='" + EO_TIME + "'";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);

      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getRefSum() {
    double sum = 0;
    String SQL = "select sum(p.pay_amt) from sale_p as p inner join sale_h " +
      "where pay_amt<0 and p.sl_key=sale_h.sl_key and sale_h.eo_date=''";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);

      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static double getRefSum(String EO_DATE, String EO_TIME) {
    double sum = 0;
    String SQL = "select sum(p.pay_amt) from sale_p as p inner join sale_h " +
      "where pay_amt<0 and p.sl_key=sale_h.sl_key and sale_h.eo_date='" + EO_DATE + "' and sale_h.eo_time='" + EO_TIME + "'";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.compileStmt(SQL);

      sum = (double) _oDb.executeQuery();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
    return sum;
  }

  public static String getClearLastDate() {
    String data = "";
    String sqlD = "select value from emisprop where name='LastClearDate'";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      SQLiteCursor date = _oDb.executeQuery(sqlD);
      date.moveToNext();
      data = date.getString(date.getColumnIndex("value"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return data;
  }

  public static String getClearLastTime() {
    String data = "";
    String sqlD = "select value from emisprop where name='LastClearTime'";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      SQLiteCursor date = _oDb.executeQuery(sqlD);
      date.moveToNext();
      data = date.getString(date.getColumnIndex("value"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return data;
  }

  public static boolean isYClear(String date) {
    boolean result = true;

    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteCursor cursor = null;
    try {
      cursor = db.executeQuery("select * from sale_h where sl_date!='" + date + "' and eo_date='' ");

      if (cursor.moveToNext()) result = false;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static boolean isClear() {
    String sqlD = "select * from sale_h where eo_date=''";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      SQLiteCursor bill = _oDb.executeQuery(sqlD);
      if (bill.moveToNext())
        return false;
      else
        return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  public static String getRefBill() {
    String _sBillNo = "";
    String sqlD = "select sl_invno_s from sale_h where eo_date='' and sl_inkind='2'";

    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      SQLiteCursor billNoList = _oDb.executeQuery(sqlD);
      int num = 0;
      while (billNoList.moveToNext()) {
        String billNo = billNoList.getString(0);
        System.out.println(billNo);
        _sBillNo += billNo + ",";
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return _sBillNo;
  }

  public static double getDiscSum(String SLKEY) {
    double result = 0;

    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteCursor cursor;
    try {
      cursor = db.executeQuery("select sum(disc_amt) from sale_dis where sl_key='" + SLKEY + "'");

      if (cursor.moveToNext()) result = cursor.getDouble(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static double getNoDCloseDiscSum(String SLKEY) {
    double result = 0;

    SQLiteCursor cursor;
    try {
      cursor = (emisDb.getInstance()).executeQuery("select sum(disc_amt) from NODCLOSE_SALE_DIS where sl_key='" + SLKEY + "' ");

      if (cursor.moveToNext()) result = cursor.getDouble(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static String getAppid() {
    String appid = "";
    String sql = "select value from emisprop where name ='AppId'";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      SQLiteCursor sqlC = _oDb.executeQuery(sql);
      if (sqlC.moveToNext()) {
        appid = sqlC.getString(0);
      } else {
        appid = "ae431c48-09b8-4219-81e7-7bfaf05d26fc";
        sql = "insert into emisprop(name,value) values ('AppId','" + appid + "')";
        _oDb.executeQuery(sql);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return appid;
  }

  public static int isDiscount(List lst) {
    int n = 0;
    for (int i = 0; i < lst.size(); i++) {
      Map<String, Object> obj = (Map<String, Object>) lst.get(i);
      List dis = (List) obj.get("discount");
      for (int j = 0; j < dis.size(); j++) {
        Map<String, Object> discDetail = (Map<String, Object>) dis.get(j);
        n = n + ((Double) discDetail.get("DISC_AMT")).intValue();
      }
    }
    return n;
  }

  private static String createEmisPropTable = "create table if not exists emisprop ( \n" +
    "\tNAME    \tvarchar(30) NOT NULL,\n" +
    "\tVALUE   \tvarchar(2100) NULL,\n" +
    "\tKIND    \tvarchar(20) NULL,\n" +
    "\tREMARK  \tvarchar(200) NULL,\n" +
    "\tREMARK2 \tvarchar(500) NULL,\n" +
    "\tMENU    \tvarchar(20) NULL,\n" +
    "\tUPD_USER\tvarchar(20) NULL,\n" +
    "\tUPD_DATE\tvarchar(8) NULL,\n" +
    "\tISROOT  \tvarchar(1) NULL,\n" +
    "\tprimary key(NAME)\n" +
    ")";

  private static String createPart_Table = "create table if not exists part ( \n" +
    "\tP_NO                \tvarchar(30) NOT NULL,\n" +
    "\tP_EAN               \tvarchar(20) NULL,\n" +
    "\tP_NAME              \tvarchar(100) NULL,\n" +
    "\tP_NAME_S            \tvarchar(100) NULL,\n" +
    "\tP_TAX               \tvarchar(1) NULL,\n" +
    "\tP_PU                \tvarchar(1) NULL,\n" +
    "\tD_NO                \tvarchar(20) NULL,\n" +
    "\tP_PHOTO             \tvarchar(50) NULL,\n" +
    "\tP_PRICE             \tdecimal(12,2) NULL,\n" +
    "\tP_PRICE2            \tdecimal(12,2) NULL,\n" +
    "\tP_PRICE3            \tdecimal(12,2) NULL,\n" +
    "\tP_PRICE4            \tdecimal(12,2) NULL,\n" +
    "\tP_PRICE5            \tdecimal(12,2) NULL,\n" +
    "\tP_PRICE6            \tdecimal(12,2) NULL,\n" +
    "\tUN_NO               \tvarchar(80) NULL,\n" +
    "\tP_IN_UN             \tvarchar(80) NULL,\n" +
    "\tP_RATE              \tdecimal(12,2) NULL,\n" +
    "\tP_DEF1              \tvarchar(20) NULL,\n" +
    "\tP_DEF2              \tvarchar(20) NULL,\n" +
    "\tP_DEF4              \tvarchar(20) NULL,\n" +
    "\tP_DEF3              \tvarchar(20) NULL,\n" +
    "\tP_STATUS            \tvarchar(1) NULL,\n" +
    "\tP_PS_QTY            \tvarchar(1) NULL,\n" +
    "\tCRE_DATE            \tvarchar(8) NULL,\n" +
    "\tUPD_DATE            \tvarchar(8) NULL,\n" +
    "\tP_DEFA              \tvarchar(20) NULL,\n" +
    "\tP_DEFB              \tvarchar(10) NULL,\n" +
    "\tP_DEFC              \tvarchar(10) NULL,\n" +
    "\tP_DEFD              \tvarchar(10) NULL,\n" +
    "\tP_PRICE_ORI         \tdecimal(12,2) NULL,\n" +
    "\tP_TAX_RATE          \tdecimal(10,2) NULL,\n" +
    "\tDD_NO               \tvarchar(25) NULL,\n" +
    "\tP_PY                \tvarchar(50) NULL,\n" +
    "\tLBL_NAME            \tvarchar(50) NULL,\n" +
    "\tIS_POR              \tvarchar(1) NULL,\n" +
    "\tIS_WEIGH            \tvarchar(2) NULL,\n" +
    "\tP_DC                \tvarchar(3) NULL,\n" +
    "\tSTORAGE_LIFE        \tint(11) NULL,\n" +
    "\tP_NO_OD             \tvarchar(5) NULL,\n" +
    "\tREM_CODE            \tvarchar(10) NULL,\n" +
    "\tWM_SETTING_TYPE     \tvarchar(1) NULL,\n" +
    "\tWM_MIN_ORDER_NUM    \tint(11) NULL,\n" +
    "\tWM_PACKAGE_BOX_NUM  \tint(11) NULL,\n" +
    "\tWM_PACKAGE_BOX_PRICE\tdecimal(12,2) NULL,\n" +
    "\tWM_USED             \tvarchar(1) NULL,\n" +
    "\tWM_DESC             \tvarchar(300) NULL,\n" +
    "\tWM_ATTR             \tvarchar(10) NULL,\n" +
    "\tP_NEW_DATE          \tvarchar(8) NULL,\n" +
    "\tDISABLE_SEA_ITEM_NO \tvarchar(1000) NULL,\n" +
    "\tWM_SALE_OUT         \tvarchar(2) NULL,\n" +
    "\tWM_SALE_OUT_FLAG    \tvarchar(1) NULL,\n" +
    "\tprimary key(P_NO)\n" +
    ")";

  private static String createApilog_Table = "create table if not exists Log (" +
    "S_NO varchar(6)," +
    "ID_NO varchar(8)," +
    "EXG_ID varchar(50)," +
    "EXG_IDNAME varchar(50)," +
    "EXG_FILE varchar(50)," +
    "EXG_KIND varchar(20)," +
    "EXG_DATE varchar(8)," +
    "EXG_TIME varchar(6)," +
    "EXG_LOG varchar(400)" +
    ")";

  private static String createDepart_Table = "create table if not exists depart ( \n" +
    "\tD_NO                \tvarchar(20) NOT NULL,\n" +
    "\tSUBDEP              \tvarchar(10) NULL,\n" +
    "\tD_CNAME             \tvarchar(100) NULL,\n" +
    "\tD_ENAME             \tvarchar(100) NULL,\n" +
    "\tD_TYPE              \tvarchar(1) NULL,\n" +
    "\tCRE_DATE            \tvarchar(8) NULL,\n" +
    "\tUPD_DATE            \tvarchar(8) NULL,\n" +
    "\tD_DOWN              \tvarchar(1) NULL,\n" +
    "\tSEA_NO              \tvarchar(200) NULL,\n" +
    "\tUN_NO               \tvarchar(200) NULL,\n" +
    "\tSALE_OPT            \tvarchar(3) NULL,\n" +
    "\tUSED                \tchar(1) NULL,\n" +
    "\tDP_TYPE             \tvarchar(2) NULL,\n" +
    "\tD_NO_OD             \tvarchar(2) NULL,\n" +
    "\tWM_MIN_ORDER_NUM    \tint(11) NULL,\n" +
    "\tWM_PACKAGE_BOX_NUM  \tint(11) NULL,\n" +
    "\tWM_PACKAGE_BOX_PRICE\tdecimal(12,2) NULL,\n" +
    "\tWM_USED             \tvarchar(1) NULL,\n" +
    "\tWM_D_NO             \tvarchar(20) NULL,\n" +
    "\tD_OVERTIME          \tint(11) NULL,\n" +
    "\tSEQ_NO              \tint(11) NULL,\n" +
    "\tprimary key(D_NO)\n" +
    ")";

  private static String createPart_N_Table = "create table if not exists PART_N(\n" +
    "P_EAN nVarChar(20) not null,\n" +
    "P_NO nVarChar(20) not null,\n" +
    "P_IS_EAN nVarChar(20),\n" +
    "primary key(P_EAN,P_NO)\n" +
    ")";

  private static String createSeasoning_H_Table = "create table if not exists seasoning_h ( \n" +
    "\tSEA_NO   \tvarchar(10) NOT NULL,\n" +
    "\tSEA_NAME \tvarchar(30) NULL,\n" +
    "\tSEA_ENAME\tvarchar(30) NULL,\n" +
    "\tSEA_TYPE \tchar(1) NULL,\n" +
    "\tIS_SINGLE\tchar(1) NULL,\n" +
    "\tUSED     \tchar(1) NULL,\n" +
    "\tSEQ_NO   \tint(11) NULL,\n" +
    "\tWM_CANUSE\tvarchar(1) NULL,\n" +
    "\tprimary key(SEA_NO)\n" +
    ")";

  private static String createSeasoning_D_TABLE = "create table if not exists seasoning_d ( \n" +
    "\tSEA_ITEM_NO   \tvarchar(10) NOT NULL,\n" +
    "\tSEA_NO        \tvarchar(10) NOT NULL,\n" +
    "\tSEA_ITEM_NAME \tvarchar(30) NULL,\n" +
    "\tSEA_ITEM_ENAME\tvarchar(30) NULL,\n" +
    "\tDEF_CHOOSE    \tchar(1) NULL,\n" +
    "\tPRICE         \tdecimal(14,2) NULL,\n" +
    "\tPRICE2        \tdecimal(14,2) NULL,\n" +
    "\tPRICE3        \tdecimal(14,2) NULL,\n" +
    "\tUSED          \tchar(1) NULL,\n" +
    "\tSEQ_NO        \tint(11) NULL,\n" +
    "\tSEA_ITEM_NO_OD\tvarchar(3) NULL,\n" +
    "\tSEA_REM_CODE  \tvarchar(5) NULL,\n" +
    "\tprimary key(SEA_NO,SEA_ITEM_NO)\n" +
    ")";

  private static String createSmenu_H_Table = "create table if not exists smenu_h ( \n" +
    "\tSM_NO               \tvarchar(30) NOT NULL,\n" +
    "\tSM_NAME             \tvarchar(100) NULL,\n" +
    "\tSM_UN_NO            \tvarchar(80) NULL,\n" +
    "\tSM_TAX              \tvarchar(1) NULL,\n" +
    "\tSM_DP_NO            \tvarchar(20) NULL,\n" +
    "\tSM_PRICE            \tdecimal(10,2) NULL,\n" +
    "\tSM_PRICE2           \tdecimal(10,2) NULL,\n" +
    "\tSM_PRICE3           \tdecimal(10,2) NULL,\n" +
    "\tSM_PRICE4           \tdecimal(10,2) NULL,\n" +
    "\tSM_PRICE5           \tdecimal(10,2) NULL,\n" +
    "\tSM_PRICE6           \tdecimal(10,2) NULL,\n" +
    "\tFLS_NO              \tvarchar(2) NULL,\n" +
    "\tCRE_DATE            \tvarchar(8) NULL,\n" +
    "\tUPD_DATE            \tvarchar(8) NULL,\n" +
    "\tP_PU                \tvarchar(1) NULL,\n" +
    "\tSM_TAX_RATE         \tdecimal(12,2) NULL,\n" +
    "\tSM_PY               \tvarchar(50) NULL,\n" +
    "\tGROUP_NAME_A        \tvarchar(50) NULL,\n" +
    "\tMAX_NUM_A           \tint(11) NULL,\n" +
    "\tGROUP_NAME_B        \tvarchar(50) NULL,\n" +
    "\tMAX_NUM_B           \tint(11) NULL,\n" +
    "\tGROUP_NAME_C        \tvarchar(50) NULL,\n" +
    "\tMAX_NUM_C           \tint(11) NULL,\n" +
    "\tGROUP_NAME_D        \tvarchar(50) NULL,\n" +
    "\tMAX_NUM_D           \tint(11) NULL,\n" +
    "\tGROUP_NAME_E        \tvarchar(50) NULL,\n" +
    "\tMAX_NUM_E           \tint(11) NULL,\n" +
    "\tP_NO_S_OD           \tvarchar(2) NULL,\n" +
    "\tWM_SETTING_TYPE     \tvarchar(1) NULL,\n" +
    "\tWM_MIN_ORDER_NUM    \tint(11) NULL,\n" +
    "\tWM_PACKAGE_BOX_NUM  \tint(11) NULL,\n" +
    "\tWM_PACKAGE_BOX_PRICE\tdecimal(12,2) NULL,\n" +
    "\tWM_USED             \tvarchar(1) NULL,\n" +
    "\tWM_DESC             \tvarchar(300) NULL,\n" +
    "\tSM_PHOTO            \tvarchar(100) NULL,\n" +
    "\tWM_ATTR             \tvarchar(10) NULL,\n" +
    "\tSEQ_NO              \tint(11) NULL,\n" +
    "\tWM_SALE_OUT         \tvarchar(2) NULL,\n" +
    "\tWM_SALE_OUT_FLAG    \tvarchar(1) NULL,\n" +
    "\tMIN_NUM_A           \tint(11) NULL,\n" +
    "\tMIN_NUM_B           \tint(11) NULL,\n" +
    "\tMIN_NUM_C           \tint(11) NULL,\n" +
    "\tMIN_NUM_D           \tint(11) NULL,\n" +
    "\tMIN_NUM_E           \tint(11) NULL,\n" +
    "\tprimary key(SM_NO)\n" +
    ")";

  private static String createSmenu_D_Table = "create table if not exists smenu_d ( \n" +
    "\tSM_NO           \tvarchar(20) NOT NULL,\n" +
    "\tRECNO           \tint(11) NOT NULL,\n" +
    "\tP_NO            \tvarchar(20) NULL,\n" +
    "\tSM_QTY          \tdecimal(12,4) NULL,\n" +
    "\tSM_PRICE        \tdecimal(10,2) NULL,\n" +
    "\tGROUP_TYPE      \tvarchar(1) NULL,\n" +
    "\tADD_PRICE       \tdecimal(5,2) NULL,\n" +
    "\tP_NO_S_OD       \tvarchar(2) NULL,\n" +
    "\tWM_GET_PLAN_ATTR\tvarchar(1) NULL,\n" +
    "\tprimary key(SM_NO,RECNO)\n" +
    ")";

  private static String createStaff_Table = "create table if not exists STAFF (\n"
    + "ST_NO nvarchar(10) not null,\n"
    + "OP_LEVEL nvarchar(1),\n"
    + "OP_PASSWORD nvarchar(50),\n"
    + "USERID nvarchar(10),\n"
    + "PASSWD nvarchar(50),\n"
    + "primary key(ST_NO)\n"
    + ")";

  private static String createSubdep_Table = "create table if not exists subdep ( \n" +
    "\tSUBDEP  \tvarchar(10) NOT NULL,\n" +
    "\tSUB_NAME\tvarchar(50) NULL,\n" +
    "\tCRE_DATE\tvarchar(8) NULL,\n" +
    "\tUPD_DATE\tvarchar(8) NULL,\n" +
    "\tprimary key(SUBDEP)\n" +
    ")";

  public static void createTable() {
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    String sql;
    try {
      // bigMonitor SQL
      _oDb.execSQL(createEmisPropTable);
      _oDb.execSQL(createDepart_Table);
      _oDb.execSQL(createSubdep_Table);
      _oDb.execSQL(createPart_Table);
      //_oDb.execSQL(createPart_N_Table); // 原有SQL
      _oDb.execSQL(createSeasoning_H_Table);
      _oDb.execSQL(createSeasoning_D_TABLE);
      _oDb.execSQL(createSmenu_H_Table);
      _oDb.execSQL(createSmenu_D_Table);
      //_oDb.execSQL(createStaff_Table); // 原有SQL
      _oDb.execSQL(createApilog_Table); // 原有SQL

      sql = "create table if not exists ddepart ( \n" +
        "\tDD_NO      \tvarchar(25) NOT NULL,\n" +
        "\tD_NO       \tvarchar(20) NOT NULL,\n" +
        "\tDD_CNAME   \tvarchar(100) NOT NULL,\n" +
        "\tDD_ENAME   \tvarchar(100) NULL,\n" +
        "\tCRE_DATE   \tvarchar(8) NULL,\n" +
        "\tUPD_DATE   \tvarchar(8) NULL,\n" +
        "\tSEA_NO     \tvarchar(200) NULL,\n" +
        "\tD_DOWN     \tvarchar(1) NULL,\n" +
        "\tPRINT_LABLE\tvarchar(1) NULL,\n" +
        "\tDISP_FLAG  \tvarchar(10) NULL,\n" +
        "\tERP_NO     \tvarchar(30) NULL,\n" +
        "\tSEQ_NO     \tint(11) NULL,\n" +
        "\tprimary key(DD_NO)\n" +
        ")";
      _oDb.execSQL(sql);

      sql = "create table if not exists store ( \n" +
        "\tS_NO             \tvarchar(12) NOT NULL,\n" +
        "\tS_NAME           \tvarchar(50) NULL,\n" +
        "\tS_NAME_S         \tvarchar(10) NULL,\n" +
        "\tR_NO             \tvarchar(10) NULL,\n" +
        "\tS_KIND           \tvarchar(1) NULL,\n" +
        "\tS_LEVEL          \tint(11) NULL,\n" +
        "\tS_ADDR           \tvarchar(50) NULL,\n" +
        "\tS_TEL            \tvarchar(15) NULL,\n" +
        "\tS_FAX            \tvarchar(15) NULL,\n" +
        "\tS_EMAIL          \tvarchar(50) NULL,\n" +
        "\tCRE_DATE         \tvarchar(8) NULL,\n" +
        "\tCRE_USER         \tvarchar(20) NULL,\n" +
        "\tUPD_DATE         \tvarchar(8) NULL,\n" +
        "\tUPD_USER         \tvarchar(20) NULL,\n" +
        "\tREMARK           \tvarchar(50) NULL,\n" +
        "\tS_STATUS         \tvarchar(1) NULL,\n" +
        "\tMU_NO            \tvarchar(10) NULL,\n" +
        "\tS_TYPE           \tvarchar(2) NULL,\n" +
        "\tS_REGION_B       \tvarchar(2) NULL,\n" +
        "\tS_PROVINCE       \tvarchar(4) NULL,\n" +
        "\tS_CITY           \tvarchar(8) NULL,\n" +
        "\tS_REGION_S       \tvarchar(10) NULL,\n" +
        "\tS_CLOSE_D        \tvarchar(8) NULL,\n" +
        "\tSEND_LINE        \tvarchar(30) NULL,\n" +
        "\tWC_STORE         \tvarchar(2) NULL,\n" +
        "\tMAP_LNG          \tdecimal(14,6) NULL,\n" +
        "\tMAP_LAT          \tdecimal(14,6) NULL,\n" +
        "\tWM_MU_NO         \tvarchar(10) NULL,\n" +
        "\tWECHAT_SEND_RANGE\tint(11) NULL,\n" +
        "\tprimary key(S_NO)\n" +
        ")";
      _oDb.execSQL(sql);

      sql = "create table if not exists tab_h ( \n" +
        "\tT_NO  \tvarchar(20) NOT NULL,\n" +
        "\tT_NAME\tvarchar(50) NULL,\n" +
        "\tprimary key(T_NO)\n" +
        ")";
      _oDb.execSQL(sql);

      sql = "create table if not exists tab_d ( \n" +
        "\tT_NO   \tvarchar(20) NOT NULL,\n" +
        "\tTD_NO  \tvarchar(20) NOT NULL,\n" +
        "\tTD_NAME\tvarchar(2000) NULL,\n" +
        "\tTD_SEQ \tint(11) NULL,\n" +
        "\tprimary key(T_NO,TD_NO)\n" +
        ")";
      _oDb.execSQL(sql);

      sql = "create table if not exists users ( \n" +
        "\tS_NO       \tvarchar(12) NOT NULL,\n" +
        "\tUSERID     \tvarchar(20) NOT NULL,\n" +
        "\tST_KEY     \tvarchar(10) NULL,\n" +
        "\tUSERNAME   \tvarchar(20) NULL,\n" +
        "\tUSERGROUPS \tvarchar(10) NULL,\n" +
        "\tPASSWD     \tvarchar(100) NULL,\n" +
        "\tMENUS      \tvarchar(30) NULL,\n" +
        "\tUSER_TYPE  \tvarchar(1) NULL,\n" +
        "\tUSER_IP    \tvarchar(20) NULL,\n" +
        "\tSYSTEM_TYPE\tvarchar(2) NULL,\n" +
        "\tCOM_NO     \tvarchar(2) NULL,\n" +
        "\tCRE_DATE   \tvarchar(8) NULL,\n" +
        "\tCRE_USER   \tvarchar(20) NULL,\n" +
        "\tUPD_DATE   \tvarchar(8) NULL,\n" +
        "\tUPD_USER   \tvarchar(20) NULL,\n" +
        "\tB_ID       \tvarchar(20) NULL,\n" +
        "\tEMAIL      \tvarchar(500) NULL,\n" +
        "\tIS_AMT     \tvarchar(1) NULL,\n" +
        "\tST_DEPT    \tvarchar(3) NULL,\n" +
        "\tWC_ID      \tvarchar(100) NULL,\n" +
        "\tprimary key(USERID)\n" +
        ")";
      _oDb.execSQL(sql);

      // venusRTWeb SQL
      sql = "create table if not exists cashier ( \n" +
        "\tOP_NO      \tvarchar(30) NOT NULL,\n" +
        "\tOP_NAME    \tvarchar(30) NULL,\n" +
        "\tOP_SEX     \tchar(1) NULL,\n" +
        "\tOP_TEL1    \tvarchar(15) NULL,\n" +
        "\tOP_TEL2    \tvarchar(15) NULL,\n" +
        "\tOP_ADDR1   \tvarchar(60) NULL,\n" +
        "\tOP_ADDR2   \tvarchar(60) NULL,\n" +
        "\tOP_ID      \tvarchar(18) NULL,\n" +
        "\tOP_BIRTH   \tvarchar(8) NULL,\n" +
        "\tOP_PASSWORD\tvarchar(300) NULL,\n" +
        "\tOP_LEVEL   \tvarchar(10) NULL,\n" +
        "\tOP_STATUS  \tchar(1) NULL,\n" +
        "\tCSHER_CARD \tvarchar(30) NULL,\n" +
        "\tREMARK     \tvarchar(60) NULL,\n" +
        "\tUPD_DATE   \tvarchar(8) NULL,\n" +
        "\tUPD_USER   \tvarchar(20) NULL,\n" +
        "\tOP_DWN     \tvarchar(2) NULL DEFAULT 'Y',\n" +
        "\tS_NO       \tvarchar(12) NULL,\n" +
        "\tIS_ACC     \tchar(1) NULL DEFAULT 'N',\n" +
        "\tIS_SALER   \tvarchar(1) NULL DEFAULT 'Y',\n" +
        "\tSPA_MAN    \tvarchar(2) NULL DEFAULT 'N',\n" +
        "\tCARD_NO    \tvarchar(2) NULL,\n" +
        "\tC_TIME     \tvarchar(3) NULL,\n" +
        "\tS_TAMT     \tdecimal(10,2) NULL,\n" +
        "\tIS_HR      \tvarchar(2) NULL,\n" +
        "\tIS_SENDER  \tvarchar(2) NULL,\n" +
        "\tprimary key(OP_NO)\n" +
        ")";
      _oDb.execSQL(sql);

      // 原有SQL
      sql = "create table if not exists FUNCTIONS_MPOS ( "
        + "ID_NO nvarchar(20) not null, "
        + "FUNC_ID nvarchar(20) not null, "
        + "FUNC_NAME nvarchar(50), "
        + "GROUPID nvarchar(20), "
        + "USED nvarchar(8), "
        + "FUNC_STR1 nvarchar(30), "
        + "FUNC_STR2 nvarchar(100), "
        + "FUNC_NUM1 integer, "
        + "FUNC_NUM2 decimal(14, 2), "
        + "REMARK nvarchar(100), "
        + "FUNC_RIGHT nvarchar(10), "
        + "SEQ_NO integer, "
        + "primary key(ID_NO, FUNC_ID)"
        + ")";
      _oDb.execSQL(sql);

      saveInProp("LastClearDate", Time.getOnlyDate());
      saveInProp("LastClearTime", Time.getOnlyTime());
      saveInProp("INIT", "Y");
      saveInProp("LastUpdateDate", "");

      // saveClearInfo();
    } catch (Exception e) {
      Log.d("createTable()", e.getMessage());
      e.printStackTrace();
    } finally {
      if (_oDb != null) _oDb.close();
    }
  }

  public static ArrayList getSaleH(String BillNo, String SLDate, String ProNo, String VeNo, String UniNo) {
    ArrayList _alSales = new ArrayList();

    String _sWhere = "sh.sl_date='" + SLDate + "'";

    // if (!"".equals(BillNo)) _sWhere+=" and SL_INVNO_S = '"+BillNo+"'";
    if (!"".equals(BillNo))
      _sWhere += " and ((sh.SL_INVNO_S = '" + BillNo + "') or (sh.SL_NO='" + BillNo + "'))";
    if (!"".equals(ProNo)) {
      _sWhere += " and sd.P_NO='" + ProNo + "'";
    }
    if (!"".equals(VeNo)) {
      _sWhere += " and sh.ECLEAR_NO = '" + VeNo + "'";
    }
    if (!"".equals(UniNo)) {
      _sWhere += " and sh.SL_INVID = '" + UniNo + "'";
    }
    String sql = "select sh.SL_DATE, sh.SL_TIME, sh.SL_NO, sh.SL_INVNO_S, sh.ERANDOM, sh.SL_INVID," +
      "  sh.SL_KEY, sh.SL_AMT, sh.ECLEAR_NO, sh.SL_INKIND, sh.SL_KEY_O ,sh.SL_TAXAMT " +
      "from Sale_H sh " +
      "where " + _sWhere +
      "  and sh.SL_KEY not in (select sh2.SL_KEY_O from Sale_h sh2 where sh2.SL_DATE='" + SLDate + "' and sh2.SL_INKIND='2') " +
      "order by sh.SL_NO";

    System.out.println(sql);
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      SQLiteCursor cursor = _oDb.executeQuery(sql);
      while (cursor.moveToNext()) {
        String _sSL_DATE = cursor.getString(0);
        String _sSL_KEY = cursor.getString(6);
        String _sSL_KEY_O = cursor.getString(10);
        String _sSL_TIME = cursor.getString(1);
        String _sSL_NO = cursor.getString(2);
        String _sSL_INVNO_S = cursor.getString(3);
        String _sERANDOM = cursor.getString(4);
        String _sSL_INVID = cursor.getString(5);
        String _sECLEAR_NO = cursor.getString(8);
        String _sSL_INKIND = cursor.getString(9);  // 收入類別
        Double _fSL_AMT = cursor.getDouble(7);
        String _sEClear_NO = cursor.getString(8);
        Double _fSL_TAXAMT = cursor.getDouble(11);

        HashMap<String, Object> _mapSale = new HashMap<String, Object>();
        _mapSale.put("sl_key", _sSL_KEY);
        _mapSale.put("sl_inkind", _sSL_INKIND);
        _mapSale.put("sl_date", _sSL_DATE);
        _mapSale.put("sl_time", _sSL_TIME);
        _mapSale.put("sl_no", _sSL_NO);
        _mapSale.put("sl_invno", _sSL_INVNO_S);
        _mapSale.put("random", _sERANDOM);
        _mapSale.put("uni_no", _sSL_INVID);
        _mapSale.put("vehicle_no", _sECLEAR_NO);
        _mapSale.put("sl_amt", _fSL_AMT);
        _mapSale.put("sl_taxamt", _fSL_TAXAMT);
        _mapSale.put("eClear_no", _sEClear_NO);

        _alSales.add(_mapSale);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return _alSales;
  }

  public static Map<String, Object> getSaleRefund(String Invno, String SLDate) {
    String sqlCount = "select count(1) from sale_h where sl_inkind='2'";
    if (!"".equals(Invno)) {
      sqlCount += " and \n" +
        "sl_invno_s ='" + Invno + "'";
    }
    String s = "select SL_KEY,S_NO,ID_NO,SL_DATE,SL_NO," +
      "SL_TIME,SL_INKIND,SL_INVID,C_NO,OP_NO," +
      "SA_NO,SL_SOURCE,SL_INVTYPE,SL_INVNO_S,SL_KEY_O," +
      "TIME_NO,PR_NO,FLS_NO,SL_CONFIRM_E,SL_CONFIRM_D," +
      "SL_QTY,SL_AMT,SL_TAXAMT,SL_NOTAXAMT,SL_DISC_AMT," +
      "SL_NDISC_AMT,SL_RECV_AMT,SL_TAXAMT_AMT,SL_INVAMT,REMARK," +
      "CRE_USER,CRE_DATE,CC_NO,S_NO_OUT,CLIENT_COUNT," +
      "SL_EINV,CT_NO,ePrintMark,eRandom,ePrnTimes," +
      "eClear_NO,eHide_NO,eDonate,SL_TYPE,MANUAL_DISC," +
      "MANUAL_DISC_CODE,SL_SORT " +
      "from SALE_H where SL_INKIND='1' " +
      "and SL_DATE='" + SLDate + "'";
    if (!"".equals(Invno)) {
      s += " and SL_INVNO_S='" + Invno + "'";
    }
    Map<String, Object> sale = new HashMap<>();

    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      _oDb.prepareStmt(sqlCount);
      int count = _oDb.executeQuery();
      if (count == 1) {
        return null;
      }

      SQLiteCursor cursor = _oDb.executeQuery(s);
      if (cursor.moveToNext()) {
        String sSL_KEY = cursor.getString(0);
        String sS_NO = cursor.getString(1);
        String sID_NO = cursor.getString(2);
        String sSL_DATE = cursor.getString(3);
        String sSL_NO = cursor.getString(4);

        String sSL_TIME = cursor.getString(5);
        String sSL_INKIND = cursor.getString(6);
        String sSL_INVID = cursor.getString(7);
        String sC_NO = cursor.getString(8);
        String sOP_NO = cursor.getString(9);

        String sSA_NO = cursor.getString(10);
        String sSL_SOURCE = cursor.getString(11);
        String sSL_INVTYPE = cursor.getString(12);
        String sSL_INVNO_S = cursor.getString(13);
        String sSL_KEY_O = cursor.getString(14);

        String sTIME_NO = cursor.getString(15);
        String sPR_NO = cursor.getString(16);
        String sFLS_NO = cursor.getString(17);
        String sSL_CONFIRM_E = cursor.getString(18);
        String sSL_CONFIRM_D = cursor.getString(19);

        int iSL_QTY = cursor.getInt(20);
        int iSL_AMT = cursor.getInt(21);
        int iSL_TAXAMT = cursor.getInt(22);
        int iSL_NOTAXAMT = cursor.getInt(23);
        int iSL_DISC_AMT = cursor.getInt(24);

        int iSL_NDISC_AMT = cursor.getInt(25);
        int iSL_RECV_AMT = cursor.getInt(26);
        int iSL_TAXAMT_AMT = cursor.getInt(27);
        int iSL_INVAMT = cursor.getInt(28);
        String sRemark = cursor.getString(29);

        String sCRE_USER = cursor.getString(30);
        String sCRE_DATE = cursor.getString(31);
        String sCC_NO = cursor.getString(32);
        String sS_NO_OUT = cursor.getString(33);
        int iCLINET_COUNT = cursor.getInt(34);

        String sSL_EINV = cursor.getString(35);
        String sCT_NO = cursor.getString(36);
        String sPrintMark = cursor.getString(37);
        String sRandom = cursor.getString(38);
        int iPrnTimes = cursor.getInt(39);

        String sClear_NO = cursor.getString(40);
        String sHide_NO = cursor.getString(41);
        String sDonate = cursor.getString(42);
        String sSL_TYPE = cursor.getString(43);
        String sManual_Disc = cursor.getString(44);

        String sManual_disc_code = cursor.getString(45);
        String sSL_SORT = cursor.getString(46);

        ArrayList detail = getSaleDMap(sSL_KEY);
        ArrayList payment = getSalePay(sSL_KEY);

        sale.put("SL_KEY", sSL_KEY);
        sale.put("S_NO", sS_NO);
        sale.put("ID_NO", sID_NO);
        sale.put("SL_DATE", sSL_DATE);
        sale.put("SL_NO", sSL_NO);

        sale.put("SL_TIME", sSL_TIME);
        sale.put("SL_INKIND", sSL_INKIND);
        sale.put("SL_INVID", sSL_INVID);
        sale.put("C_NO", sC_NO);
        sale.put("OP_NO", sOP_NO);

        sale.put("SA_NO", sSA_NO);
        sale.put("SL_SOURCE", sSL_SOURCE);
        sale.put("SL_INVTYPE", sSL_INVTYPE);
        sale.put("SL_INVNO_S", sSL_INVNO_S);
        sale.put("SL_KEY_O", sSL_KEY_O);

        sale.put("TIME_NO", sTIME_NO);
        sale.put("PR_NO", sPR_NO);
        sale.put("FLS_NO", sFLS_NO);
        sale.put("SL_CONFIRM_E", sSL_CONFIRM_E);
        sale.put("SL_CONFIRM_D", sSL_CONFIRM_D);

        sale.put("SL_QTY", iSL_QTY);
        sale.put("SL_AMT", iSL_AMT);
        sale.put("SL_TAXAMT", iSL_TAXAMT);
        sale.put("SL_NOTAXAMT", iSL_NOTAXAMT);
        sale.put("SL_DISC_AMT", iSL_DISC_AMT);

        sale.put("SL_NDISC_AMT", iSL_NDISC_AMT);
        sale.put("SL_RECV_AMT", iSL_RECV_AMT);
        sale.put("SL_TAXAMT_AMT", iSL_TAXAMT_AMT);
        sale.put("SL_INVAMT", iSL_INVAMT);
        sale.put("REMARK", sRemark);

        sale.put("CRE_USER", sCRE_USER);
        sale.put("CRE_DATE", sCRE_DATE);
        sale.put("CC_NO", sCC_NO);
        sale.put("S_NO_OUT", sS_NO_OUT);
        sale.put("CLIENT_COUNT", iCLINET_COUNT);

        sale.put("SL_EINV", sSL_EINV);
        sale.put("CT_NO", sCT_NO);
        sale.put("ePrintMark", sPrintMark);
        sale.put("eRandom", sRandom);
        sale.put("ePrnTimes", iPrnTimes);

        sale.put("eClear_NO", sClear_NO);
        sale.put("eHide_NO", sHide_NO);
        sale.put("eDonate", sDonate);
        sale.put("SL_TYPE", sSL_TYPE);
        sale.put("MANUAL_DISC", sManual_Disc);

        sale.put("MANUAL_DISC_CODE", sManual_disc_code);
        sale.put("SL_SORT", sSL_SORT);

        sale.put("DETAIL", detail);
        sale.put("PAYMENT", payment);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sale;
  }

  public static ArrayList getSaleDMap(String SLKey) {
    Map<String, Object> data = new HashMap<>();
    String _sSQL = "select SL_KEY,RECNO,S_NO,ID_NO,SL_DATE,\n" +
      "SL_NO,FLS_NO,P_NO,DP_NO,P_TAX,\n" +
      "SL_QTY,SL_PRICE,SL_AMT,SL_NOTAXAMT,SL_DISC_AMT,\n" +
      "SL_NDISC_AMT,RECNO_CCR,P_PRICE,PK_RECNO,SL_TAXAMT_AMT,\n" +
      "REMARK,SL_MEMO,DD_NO,SYS_DISC,SYS_DISC_AMT,\n" +
      "SYS_DIS_AMT_D,SL_TAX_AMT,SEA_NO,SEA_AMT,SL_TAXAMT from SALE_D\n" +
      "where sl_key='" + SLKey + "'";
    ArrayList _alSales = new ArrayList();
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      SQLiteCursor cursor = _oDb.executeQuery(_sSQL);
      int _iCount = 0;
      while (cursor.moveToNext()) {
        _iCount++;
        String _sSL_KEY = cursor.getString(0);
        int _iRECNO = cursor.getInt(1);
        String _sS_NO = cursor.getString(2);
        String _sID_NO = cursor.getString(3);
        String _sSL_DATE = cursor.getString(4);

        String _sSL_NO = cursor.getString(5);
        String _sFLS_NO = cursor.getString(6);
        String _sP_NO = cursor.getString(7);
        String _sDP_NO = cursor.getString(8);
        String _sP_TAX = cursor.getString(9);

        int _iSL_QTY = cursor.getInt(10);
        Double _dSL_PRICE = cursor.getDouble(11);
        Double _dSL_AMT = cursor.getDouble(12);
        Double _dSL_NOTAXAMT = cursor.getDouble(13);
        Double _dSL_DISC_AMT = cursor.getDouble(14);

        Double _dSL_NDISC_AMT = cursor.getDouble(15);
        int _iRECNO_CCR = cursor.getInt(16);
        Double _dP_PRICE = cursor.getDouble(17);
        String _sPK_RECNO = cursor.getString(18);
        Double _dSL_TAXAMT_AMT = cursor.getDouble(19);

        String _sREMARK = cursor.getString(20);
        String _sSL_MEMO = cursor.getString(21);
        String _sDD_NO = cursor.getString(22);
        String _sSYS_DISC = cursor.getString(23);
        Double _dSYS_DISC_AMT = cursor.getDouble(24);

        String _sSYS_DIS_AMT_D = cursor.getString(25);
        Double _dSL_TAX_AMT = cursor.getDouble(26);
        String _sSEA_NO = cursor.getString(27);
        Double _dSEA_AMT = cursor.getDouble(28);
        Double _dSL_TAXAMT = cursor.getDouble(29);

        HashMap<String, Object> _mapSale = new HashMap<String, Object>();
        _mapSale.put("sl_key", _sSL_KEY);
        _mapSale.put("recno", _iRECNO);
        _mapSale.put("s_no", _sS_NO);
        _mapSale.put("id_no", _sID_NO);
        _mapSale.put("sl_date", _sSL_DATE);

        _mapSale.put("sl_no", _sSL_NO);
        _mapSale.put("fls_no", _sFLS_NO);
        _mapSale.put("p_no", _sP_NO);
        _mapSale.put("dp_no", _sDP_NO);
        _mapSale.put("p_tax", _sP_TAX);

        _mapSale.put("sl_qty", _iSL_QTY);
        _mapSale.put("sl_price", _dSL_PRICE);
        _mapSale.put("sl_amt", _dSL_AMT);
        _mapSale.put("sl_notaxamt", _dSL_NOTAXAMT);
        _mapSale.put("sl_disc_amt", _dSL_DISC_AMT);

        _mapSale.put("sl_ndisc_amt", _dSL_NDISC_AMT);
        _mapSale.put("recno_ccr", _iRECNO_CCR);
        _mapSale.put("p_price", _dP_PRICE);
        _mapSale.put("pk_recno", _sPK_RECNO);
        _mapSale.put("sl_taxamt_amt", _dSL_TAXAMT_AMT);

        _mapSale.put("remark", _sREMARK);
        _mapSale.put("sl_memo", _sSL_MEMO);
        _mapSale.put("dd_no", _sDD_NO);
        _mapSale.put("sys_disc", _sSYS_DISC);
        _mapSale.put("sys_disc_amt", _dSYS_DISC_AMT);

        _mapSale.put("sys_dis_amt_d", _sSYS_DIS_AMT_D);
        _mapSale.put("sl_tax_amt", _dSL_TAX_AMT);
        _mapSale.put("sea_no", _sSEA_NO);
        _mapSale.put("sea_amt", _dSEA_AMT);

        _mapSale.put("sl_taxamt", _dSL_TAXAMT);

        ArrayList _alDiscount = new ArrayList();
        _alDiscount = getSaleDis(SLKey, _iRECNO_CCR);
        _mapSale.put("discount", _alDiscount);
        _alSales.add(_mapSale);
      }
      data.put("data", _alSales);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return _alSales;
  }

  public static Map getSaleMap(String SL_KEY) {
    Map data = new HashMap<String, Object>();
    String _sSQL = "select sd.SL_KEY,sd.RECNO,sd.S_NO,sd.ID_NO,sd.SL_DATE,\n" +
      "sd.SL_NO,sd.FLS_NO,sd.P_NO,sd.DP_NO,sd.P_TAX,\n" +
      "SL_QTY,sd.SL_PRICE,sd.SL_AMT,SL_NOTAXAMT,SL_DISC_AMT,\n" +
      "SL_NDISC_AMT,RECNO_CCR,P_PRICE,PK_RECNO,SL_TAXAMT_AMT,\n" +
      "REMARK,SL_MEMO,DD_NO,SYS_DISC,SYS_DISC_AMT,\n" +
      "SYS_DIS_AMT_D,SL_TAX_AMT,SEA_NO,SEA_AMT,SL_TAXAMT,name from SALE_D sd \n" +
      "left join part p on p.P_NO= sd.P_NO where sl_key='" + SL_KEY + "' ";
    String _sSQL2 = "select sl_date,sl_time,sl_invno_s,sl_no,eRandom,sl_invid,\n" +
      "ct_no,edonate,eClear_no from sale_h where sl_key='" + SL_KEY + "'";
    ArrayList _alSales = new ArrayList();
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      SQLiteCursor cursor = _oDb.executeQuery(_sSQL);
      int _iCount = 0;
      while (cursor.moveToNext()) {
        _iCount++;
        String _sSL_KEY = cursor.getString(0);
        int _iRECNO = cursor.getInt(1);
        String _sS_NO = cursor.getString(2);
        String _sID_NO = cursor.getString(3);
        String _sSL_DATE = cursor.getString(4);

        String _sSL_NO = cursor.getString(5);
        String _sFLS_NO = cursor.getString(6);
        String _sP_NO = cursor.getString(7);
        String _sDP_NO = cursor.getString(8);
        String _sP_TAX = cursor.getString(9);

        int _iSL_QTY = cursor.getInt(10);
        Double _dSL_PRICE = cursor.getDouble(11);
        Double _dSL_AMT = cursor.getDouble(12);
        Double _dSL_NOTAXAMT = cursor.getDouble(13);
        Double _dSL_DISC_AMT = cursor.getDouble(14);

        Double _dSL_NDISC_AMT = cursor.getDouble(15);
        int _iRECNO_CCR = cursor.getInt(16);
        Double _dP_PRICE = cursor.getDouble(17);
        String _sPK_RECNO = cursor.getString(18);
        Double _dSL_TAXAMT_AMT = cursor.getDouble(19);

        String _sREMARK = cursor.getString(20);
        String _sSL_MEMO = cursor.getString(21);
        String _sDD_NO = cursor.getString(22);
        String _sSYS_DISC = cursor.getString(23);
        Double _dSYS_DISC_AMT = cursor.getDouble(24);

        String _sSYS_DIS_AMT_D = cursor.getString(25);
        Double _dSL_TAX_AMT = cursor.getDouble(26);
        String _sSEA_NO = cursor.getString(27);
        Double _dSEA_AMT = cursor.getDouble(28);
        Double _dSL_TAXAMT = cursor.getDouble(29);

        String _sP_NAME = cursor.getString(30);

        HashMap<String, Object> _mapSale = new HashMap<String, Object>();
        _mapSale.put("sl_key", _sSL_KEY);
        _mapSale.put("recno", _iRECNO);
        _mapSale.put("s_no", _sS_NO);
        _mapSale.put("id_no", _sID_NO);
        _mapSale.put("sl_date", _sSL_DATE);

        _mapSale.put("sl_no", _sSL_NO);
        _mapSale.put("fls_no", _sFLS_NO);
        _mapSale.put("p_no", _sP_NO);
        _mapSale.put("dp_no", _sDP_NO);
        _mapSale.put("p_tax", _sP_TAX);

        _mapSale.put("sl_qty", _iSL_QTY);
        _mapSale.put("sl_price", _dSL_PRICE);
        _mapSale.put("sl_amt", _dSL_AMT);
        _mapSale.put("sl_notaxamt", _dSL_NOTAXAMT);
        _mapSale.put("sl_disc_amt", _dSL_DISC_AMT);

        _mapSale.put("sl_ndisc_amt", _dSL_NDISC_AMT);
        _mapSale.put("recno_ccr", _iRECNO_CCR);
        _mapSale.put("p_price", _dP_PRICE);
        _mapSale.put("pk_recno", _sPK_RECNO);
        _mapSale.put("sl_taxamt_amt", _dSL_TAXAMT_AMT);

        _mapSale.put("remark", _sREMARK);
        _mapSale.put("sl_memo", _sSL_MEMO);
        _mapSale.put("dd_no", _sDD_NO);
        _mapSale.put("sys_disc", _sSYS_DISC);
        _mapSale.put("sys_disc_amt", _dSYS_DISC_AMT);

        _mapSale.put("sys_dis_amt_d", _sSYS_DIS_AMT_D);
        _mapSale.put("sl_tax_amt", _dSL_TAX_AMT);
        _mapSale.put("sea_no", _sSEA_NO);
        _mapSale.put("sea_amt", _dSEA_AMT);
        _mapSale.put("sl_taxamt", _dSL_TAXAMT);

        _mapSale.put("p_name", _sP_NAME);
        ArrayList _alDiscount = new ArrayList();
        _alDiscount = getSaleDis(SL_KEY, _iRECNO_CCR);
        _mapSale.put("discount", _alDiscount);
        _alSales.add(_mapSale);
      }
      cursor = _oDb.executeQuery(_sSQL2);
      if (cursor.moveToNext()) {
        String sl_date = cursor.getString(0);
        String sl_time = cursor.getString(1);
        String sl_invno_s = cursor.getString(2);
        String sl_no = cursor.getString(3);
        String random = cursor.getString(4);
        String sl_invid = cursor.getString(5);
        String ct_no = cursor.getString(6);
        String donate = cursor.getString(7);
        String eClear_NO = cursor.getString(8);
        data.put("sl_date", sl_date);
        data.put("sl_key", SL_KEY);
        data.put("sl_time", sl_time);
        data.put("sl_no", sl_no);
        data.put("inv_no", sl_invno_s);
        data.put("random", random);
        data.put("uni_no", sl_invid);
        data.put("vehicle_no", ct_no);
        data.put("donate", donate);
        data.put("eClear_no", eClear_NO);
      }
      data.put("data", _alSales);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return data;
  }

  public static ArrayList getSaleDis(String sSL_KEY, int iRECNO) {
    ArrayList _alDiscount = new ArrayList();
    String sql = "select sd.RECNO, sd.DISC_QTY, sd.DISC_AMT, sd.REASON, sh.SL_NO " +
      "from Sale_Dis sd " +
      "left join sale_h sh on sh.sl_key= sd.sl_key " +
      "where sd.SL_KEY='" + sSL_KEY + "' and sd.RECNO=" + iRECNO + " " +
      "order by sd.RECNO";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      SQLiteCursor cursor = _oDb.executeQuery(sql);

      while (cursor.moveToNext()) {
        String _sREASON = cursor.getString(3);
        int _iRECNO = cursor.getInt(0);

        String _sSL_NO = cursor.getString(4);
        int _iDISC_QTY = cursor.getInt(1);
        float _fDISC_AMT = cursor.getFloat(2);

        HashMap<String, Object> _mapDiscount = new HashMap<String, Object>();
        _mapDiscount.put("recno", _iRECNO);
        _mapDiscount.put("sl_no", _sSL_NO);
        _mapDiscount.put("disc_qty", _iDISC_QTY);
        _mapDiscount.put("disc_amt", _fDISC_AMT);
        _mapDiscount.put("reason", _sREASON);
        _alDiscount.add(_mapDiscount);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return _alDiscount;
  }

  public static ArrayList getSalePay(String SL_KEY) {
    ArrayList _alPayment = new ArrayList();
    String sql = "select RECNO, PAY_NO, PAY_AMT, PAY_EX1, PAY_EX2, PAY_EX3 " +
      "from SALE_P where SL_KEY='" + SL_KEY + "' order by RECNO";
    emisSQLiteWrapper _oDb = emisDb.getInstance();
    try {
      SQLiteCursor cursor = _oDb.executeQuery(sql);
      while (cursor.moveToNext()) {
        HashMap<String, Object> pay = new HashMap<>();
        Double recno = cursor.getDouble(0);
        String pay_no = cursor.getString(1);
        Double pay_amt = cursor.getDouble(2);
        String pay_ex1 = cursor.getString(3);
        String pay_ex2 = cursor.getString(4);
        String pay_ex3 = cursor.getString(5);

        pay.put("RECNO", recno);
        pay.put("PAY_NO", pay_no);
        pay.put("PAY_AMT", pay_amt);
        pay.put("PAY_EX1", pay_ex1);
        pay.put("PAY_EX2", pay_ex2);
        pay.put("PAY_EX3", pay_ex3);
        _alPayment.add(pay);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return _alPayment;
  }

  public static void saveClearInfo() {
    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteStatement stmt = null;
    try {
      stmt = db.compileStmt("insert into emisprop (name,value) values (?,?) ");
      stmt.bindString(1, "LastClearDate");
      stmt.bindString(2, Time.getOnlyDate());
      stmt.executeInsert();

      stmt.bindString(1, "LastClearTime");
      stmt.bindString(2, Time.getOnlyTime());
      stmt.executeInsert();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void updateClearInfo(int type) {
    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteStatement stmt;

    String date = Time.getOnlyDate();
    String time = Time.getOnlyTime();
    try {
      stmt = db.compileStmt("update emisprop set value = ? where name = ? ;");

      if (type == 1) {
        stmt.bindString(1, date);
        stmt.bindString(2, "LastClearDate");
        stmt.executeInsert();

        stmt.bindString(1, time);
        stmt.bindString(2, "LastClearTime");
        stmt.executeInsert();
      } else {
        stmt.bindString(1, "250000");
        stmt.bindString(2, "LastClearTime");
        stmt.executeInsert();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void saveInProp(String name, String value) {
    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteStatement stmtDelete = null;
    SQLiteStatement stmtInsert = null;
    try {
      stmtDelete = db.compileStmt("delete from emisprop where name = ? ");
      stmtDelete.bindString(1, name);
      stmtDelete.executeUpdateDelete();

      stmtInsert = db.compileStmt("insert into emisprop (name, value) values (?, ?) ");
      stmtInsert.bindString(1, name);
      stmtInsert.bindString(2, value);
      stmtInsert.executeInsert();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void deleteInProp(String name) {
    SQLiteStatement stmtDelInProp;
    String insertProp_D = "delete from emisprop where name=?";
    emisSQLiteWrapper db = emisDb.getInstance();
    try {
      stmtDelInProp = db.compileStmt(insertProp_D);
      stmtDelInProp.bindString(1, name);
      stmtDelInProp.executeInsert();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String getFromProp(String name) {
    String result = "";
    SQLiteCursor cursor = null;
    try {
      cursor = (emisDb.getInstance()).executeQuery("select value from emisprop where name='" + name + "'");

      if (cursor.moveToNext()) result = cursor.getString(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static void updateFromProp(final String name, final String value) {
    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteStatement stmt;
    try {
      stmt = db.compileStmt("update emisprop set value = ? where name = ? ");
      stmt.bindString(1, value);
      stmt.bindString(2, name);
      stmt.executeInsert();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void saveProduct(ArrayList proList) throws Exception {
    SQLiteStatement stmtSaveInProp;
    String deletePart = "delete from part";
    String insertPart = "insert into part (P_NO,name,price,price2,price3," +
      "price4,price5," +
      "price6,p_pu,p_tax,p_ean," +
      "d_no,subdep,p_defc,price_max,sea_item_no,name_s)values(?,?,?,?,?," +
      "?,?," +
      "?,?,?,?," +
      "?,?,?,?,?,?)";
    emisSQLiteWrapper db = emisDb.getInstance();
    try {
      db.beginTransaction();
      db.execSQL(deletePart);
      stmtSaveInProp = db.compileStmt(insertPart);
      for (int i = 0; i < proList.size(); i++) {
        LinkedTreeMap<String, Object> product = (LinkedTreeMap<String, Object>) proList.get(i);
        stmtSaveInProp.bindString(1, (String) product.get("p_no"));
        stmtSaveInProp.bindString(2, (String) product.get("name"));
        stmtSaveInProp.bindDouble(3, (Double) product.get("price"));
        stmtSaveInProp.bindDouble(4, (Double) product.get("price2"));
        stmtSaveInProp.bindDouble(5, (Double) product.get("price3"));
        stmtSaveInProp.bindDouble(6, (Double) product.get("price4"));
        stmtSaveInProp.bindDouble(7, (Double) product.get("price5"));
        stmtSaveInProp.bindDouble(8, (Double) product.get("price6"));
        stmtSaveInProp.bindString(9, (String) product.get("p_pu"));
        stmtSaveInProp.bindString(10, (String) product.get("p_tax"));
        stmtSaveInProp.bindString(11, (String) product.get("p_ean"));
        stmtSaveInProp.bindString(12, (String) product.get("d_no"));
        stmtSaveInProp.bindString(13, (String) product.get("subdep"));
        stmtSaveInProp.bindString(14, (String) product.get("p_defc"));
        stmtSaveInProp.bindDouble(15, (Double) product.get("price_max"));
        stmtSaveInProp.bindString(16, (String) product.get("sea_item_no"));
        stmtSaveInProp.bindString(17, (String) product.get("name_s"));
        stmtSaveInProp.executeInsert();
      }
      db.setTransactionSuccessful();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      db.endTransaction();
    }
  }

  public static void saveEinvoice(ArrayList invList) throws Exception {
    SQLiteStatement stmtSaveInProp;
    SQLiteStatement stmtCheckEINV;
    String clearEINV = "delete from INVOICE";
    String insertEINV = "insert into INVOICE (INV_DATE_S,INV_DATE_E," +
      "INV_NO_S,INV_NO_E" +
      ")values(" +
      "?,?,?,?)";
    String selectEINV = "select count(1) from " +
      "(select * from Invoice where INV_NO_S=? and INV_NO_E =? " +
      "union select * from Invoice_Hist where INV_NO_S=? and INV_NO_E =?)";
    emisSQLiteWrapper db = emisDb.getInstance();
    try {
      db.execSQL(clearEINV);
      db.execSQL("delete from invoice_hist");
      for (int i = 0; i < invList.size(); i++) {
        LinkedTreeMap<String, Object> inv = (LinkedTreeMap<String, Object>) invList.get(i);
        stmtCheckEINV = db.compileStmt(selectEINV);
        db.prepareStmt(selectEINV);
        db.setString(1, (String) inv.get("inv_no_s"));
        db.setString(2, (String) inv.get("inv_no_e"));
        db.setString(3, (String) inv.get("inv_no_s"));
        db.setString(4, (String) inv.get("inv_no_e"));
        int count = db.executeQuery();
        if (count == 1) {
          continue;
        }
        stmtSaveInProp = db.compileStmt(insertEINV);
        stmtSaveInProp.bindString(1, (String) inv.get("inv_date_s"));
        stmtSaveInProp.bindString(2, (String) inv.get("inv_date_e"));
        stmtSaveInProp.bindString(3, (String) inv.get("inv_no_s"));
        stmtSaveInProp.bindString(4, (String) inv.get("inv_no_e"));
        stmtSaveInProp.executeInsert();
      }
    } catch (Exception e) {
      throw e;
    }
  }

  public static void removeEinvoice(String invno) {
    SQLiteStatement stmtSaveInProp;
    String insertEINV = "insert into INVOICE_HIST " +
      "select inv_date_s,INV_DATE_E,inv_no_s,inv_no_e from INVOICE where inv_no_e<=?";
    String deleteEINV = "delete from INVOICE where inv_no_e<=?";
    emisSQLiteWrapper db = emisDb.getInstance();
    try {
      stmtSaveInProp = db.compileStmt(insertEINV);
      stmtSaveInProp.bindString(1, invno);
      stmtSaveInProp.executeInsert();

      stmtSaveInProp = db.compileStmt(deleteEINV);
      stmtSaveInProp.bindString(1, invno);
      stmtSaveInProp.executeUpdateDelete();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String[] getInvoiceMonth(String date) {
    String[] result = new String[2];
    String year = date.substring(0, 4);
    String month = date.substring(4, 6);

    switch (month) {
      case "01":
      case "02":
        result[0] = year + "01";
        result[1] = year + "02";
        break;
      case "03":
      case "04":
        result[0] = year + "03";
        result[1] = year + "04";
        break;
      case "05":
      case "06":
        result[0] = year + "05";
        result[1] = year + "06";
        break;
      case "07":
      case "08":
        result[0] = year + "07";
        result[1] = year + "08";
        break;
      case "09":
      case "10":
        result[0] = year + "09";
        result[1] = year + "10";
        break;
      case "11":
      case "12":
        result[0] = year + "11";
        result[1] = year + "12";
        break;
    }
    return result;
  }

  public static String getNextInvoiceNo(final String invoiceNo) {
    String result = "";

    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteCursor cursor;

    String[] invoiceMonth = getInvoiceMonth(emisUtil.todayDateAD());
    String no = invoiceNo;

    try {
      if (invoiceNo.length() == 10) no = invoiceNo.substring(2, 10);

      if (no.equals("")) {
        // 取得當期第一筆發票號碼。
        cursor = db.executeQuery("select INV_NO_S, INV_NO_E from INVOICE where (1=1) and INV_DATE_S = '" + invoiceMonth[0] + "' and INV_DATE_E = '" + invoiceMonth[1] + "' order by INV_DATE_S, INV_DATE_E ");
        if (cursor.moveToNext()) result = cursor.getString(cursor.getColumnIndex("INV_NO_S"));
      } else {
        // cursor = db.executeQuery("select INV_NO_S, INV_NO_E from INVOICE where (1=1) and INV_DATE_S = '" + invoiceMonth[0] + "' and INV_DATE_E = '" + invoiceMonth[1] + "' and INV_NO_S <= '" + result + "' and INV_NO_E >= '" + result + "' order by INV_DATE_S, INV_DATE_E ");
        // cursor = db.executeQuery("select INV_NO_S, INV_NO_E from INVOICE where (1=1) and INV_DATE_S = '" + invoiceMonth[0] + "' and INV_DATE_E = '" + invoiceMonth[1] + "' and INV_NO_S <= '" + result + "' order by INV_DATE_S, INV_DATE_E ");
        cursor = db.executeQuery("select INV_NO_S, INV_NO_E from INVOICE where (1=1) and INV_DATE_S = '" + invoiceMonth[0] + "' and INV_DATE_E = '" + invoiceMonth[1] + "' order by INV_DATE_S, INV_DATE_E ");
        while (cursor.moveToNext()) {
          // System.out.println("result:" + result);

          if (invoiceNo.compareTo(cursor.getString(cursor.getColumnIndex("INV_NO_S"))) >= 0) {
            result = invoiceNo.substring(0, 2) + String.format("%08d", Integer.valueOf(no) + 1);
            if (result.compareTo(cursor.getString(cursor.getColumnIndex("INV_NO_E"))) <= 0) {
              break;  // 區間內還有發票號碼可用。
            } else {
              result = "";
            }
          } else {
            result = cursor.getString(cursor.getColumnIndex("INV_NO_S"));
            break;
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static boolean checkInvoiceIsCrossMonth(final String invoiceNo) {
    boolean result = false;

    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteCursor cursor;

    String[] invoiceMonth = getInvoiceMonth(emisUtil.todayDateAD());
    try {
      cursor = db.executeQuery("select INV_NO_S from INVOICE where (1=1) and INV_DATE_S = '" + invoiceMonth[0] + "' and INV_DATE_E = '" + invoiceMonth[1] + "' and INV_NO_S <= '" + invoiceNo + "' and INV_NO_E >= '" + invoiceNo + "' ");
      if (cursor.getCount() == 0) result = true; // 不存在則表示已跨月？
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static int getEinvoiceRest(String invno) {
    emisSQLiteWrapper db;
    SQLiteCursor cursor;

    int now = Integer.valueOf(invno.substring(2, 10));
    String einv_s = "";
    String einv_e = "";
    int result = 0;
    String[] date = Encode.getEinvMonth();

    try {
      db = emisDb.getInstance();

      cursor = db.executeQuery("select INV_NO_S,INV_NO_E from Invoice where INV_DATE_S='" + date[0] + "' ");
      while (cursor.moveToNext()) {
        einv_s = cursor.getString(0);
        einv_e = cursor.getString(1);

        int start = Integer.valueOf(einv_s.substring(2, 10));
        int end = Integer.valueOf(einv_e.substring(2, 10));

        if (now < end && now > start) start = now;

        if (now > end) continue;

        result += (end - start + 1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static void savePayment(ArrayList payList) throws Exception {
    SQLiteStatement stmtSaveInProp;
    String deletePayment = "delete from payment";
    String insertPayment = "insert into payment (pay_no,pay_name," +
      "edc_no" +
      ")values(" +
      "?,?,?)";
    emisSQLiteWrapper db = emisDb.getInstance();
    try {
      db.beginTransaction();
      db.execSQL(deletePayment);
      stmtSaveInProp = db.compileStmt(insertPayment);
      for (int i = 0; i < payList.size(); i++) {
        LinkedTreeMap<String, Object> inv = (LinkedTreeMap<String, Object>) payList.get(i);
        stmtSaveInProp.bindString(1, (String) inv.get("pay_no"));
        stmtSaveInProp.bindString(2, (String) inv.get("pay_name"));

        stmtSaveInProp.bindString(3, (String) inv.get("edc_no"));
        // System.out.println("payment:" + (String)inv.get("pay_no")+":"+(String)inv.get("edc_no"));
        stmtSaveInProp.executeInsert();
      }
      db.setTransactionSuccessful();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      db.endTransaction();
    }
  }

  public static String getEDC_NO(String name) {
    String sql = "select edc_no from payment where Pay_name='" + name + "'";

    emisSQLiteWrapper db = emisDb.getInstance();
    String edc_no = "";
    try {
      SQLiteCursor cursor = db.executeQuery(sql);
      if (cursor.moveToNext()) {
        edc_no = cursor.getString(0);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return edc_no;
  }

  public static String getEDC_NO_PAYNO(String PAY_NO) {
    String sql = "select edc_no from payment where pay_no='" + PAY_NO + "'";

    emisSQLiteWrapper db = emisDb.getInstance();
    String edc_no = "";
    try {
      SQLiteCursor cursor = db.executeQuery(sql);
      if (cursor.moveToNext()) {
        edc_no = cursor.getString(0);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return edc_no;
  }

  public static String getPay_NO(String name) {
    String sql = "select pay_no from payment where Pay_name='" + name + "'";

    emisSQLiteWrapper db = emisDb.getInstance();
    String pay_no = "";
    try {
      SQLiteCursor cursor = db.executeQuery(sql);
      if (cursor.moveToNext()) {
        pay_no = cursor.getString(0);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return pay_no;
  }

  public static String getPay_Name(String PAY_NO) {
    String sql = "select pay_name from payment where pay_no='" + PAY_NO + "'";

    emisSQLiteWrapper db = emisDb.getInstance();
    String pay_name = "";
    try {
      SQLiteCursor cursor = db.executeQuery(sql);
      if (cursor.moveToNext()) {
        pay_name = cursor.getString(0);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return pay_name;
  }

  public static String getProduct(String P_NO) {
    emisSQLiteWrapper db = emisDb.getInstance();
    String Product = "";
    HashMap<String, Object> proOBJ = new HashMap<>();
    Gson gson = new Gson();
    try {
      Cursor c = db.executeQuery("select P_NAME,P_PRICE,P_PRICE2,P_PRICE3,P_PRICE4, " +
        "P_PRICE5,P_PRICE6,P_NO,P_TAX,D_NO,DD_NO,P_DEFC,P_PU from part where " +
        "P_NO='" + P_NO + "' or P_EAN='" + P_NO + "'");
      if (c.moveToNext()) {
        proOBJ.put("name", c.getString(0));
        proOBJ.put("p_no", c.getString(7));
        proOBJ.put("price", c.getString(1));
        proOBJ.put("price2", c.getString(2));
        proOBJ.put("price3", c.getString(3));
        proOBJ.put("price4", c.getString(4));
        proOBJ.put("price5", c.getString(5));
        proOBJ.put("price6", c.getString(6));
        proOBJ.put("p_tax", c.getString(8));
        proOBJ.put("d_no", c.getString(9));
        proOBJ.put("subdep", c.getString(10));
        proOBJ.put("p_defc", c.getString(11));
        proOBJ.put("p_pu", c.getString(12));
      } else {
        proOBJ.put("name", "金額入帳");
        proOBJ.put("p_no", P_NO);
        proOBJ.put("price", "0");
        proOBJ.put("price2", "0");
        proOBJ.put("price3", "0");
        proOBJ.put("price4", "0");
        proOBJ.put("price5", "0");
        proOBJ.put("price6", "0");
        proOBJ.put("p_tax", "2");
        proOBJ.put("d_no", "");
        proOBJ.put("subdep", "");
        proOBJ.put("p_defc", "");
        proOBJ.put("p_pu", "1");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    Product = gson.toJson(proOBJ);
    return Product;
  }

  public static ArrayList getProductBySubdep(String subdep) {
    ArrayList proList = new ArrayList();
    emisSQLiteWrapper db = emisDb.getInstance();
    String SQL = "select name,price,price2,price3,price4, " +
      "price5,price6,p_no,p_tax,name_s,p_pu from PART where " +
      "subdep='" + subdep + "'";

    try {
      SQLiteCursor cursor = db.executeQuery(SQL);
      while (cursor.moveToNext()) {
        String name = cursor.getString(0);
        Double price = cursor.getDouble(1);
        Double price2 = cursor.getDouble(2);
        Double price3 = cursor.getDouble(3);
        Double price4 = cursor.getDouble(4);
        Double price5 = cursor.getDouble(5);
        Double price6 = cursor.getDouble(6);
        String p_no = cursor.getString(7);
        String p_tax = cursor.getString(8);
        String name_s = cursor.getString(9);
        String p_pu = cursor.getString(10);

        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("price", price);
        product.put("price2", price2);
        product.put("price3", price3);
        product.put("price4", price4);
        product.put("price5", price5);
        product.put("price6", price6);
        product.put("p_no", p_no);
        product.put("p_tax", p_tax);
        product.put("name_s", name_s);
        product.put("p_pu", p_pu);
        proList.add(product);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return proList;
  }

  public static void saveDepart(ArrayList departList) throws Exception {
    SQLiteStatement stmtSaveInProp;
    SQLiteStatement stmtDeleteDepart;
    String deleteDepart = "delete from DEPART";
    String insertDepart = "insert into DEPART(D_NO,D_NAME,D_NAME_S,CCRUSED,REMARK)\n" +
      "values(?,?,?,?,?)";
    emisSQLiteWrapper db = emisDb.getInstance();
    stmtSaveInProp = db.compileStmt(insertDepart);
    stmtDeleteDepart = db.compileStmt(deleteDepart);
    try {
      db.beginTransaction();
      stmtDeleteDepart.executeUpdateDelete();
      for (int i = 0; i < departList.size(); i++) {
        LinkedTreeMap<String, Object> depart = (LinkedTreeMap<String, Object>) departList.get(i);
        String D_NO = (String) depart.get("d_no");
        String D_NAME = (String) depart.get("d_name");
        String D_NAME_S = (String) depart.get("d_name_s");
        String CCRUSED = (String) depart.get("used");
        String REMARK = (String) depart.get("remark");
        stmtSaveInProp.clearBindings();
        stmtSaveInProp.bindString(1, D_NO);
        stmtSaveInProp.bindString(2, D_NAME);
        stmtSaveInProp.bindString(3, D_NAME_S);
        stmtSaveInProp.bindString(4, CCRUSED);
        stmtSaveInProp.bindString(5, REMARK);
        stmtSaveInProp.executeInsert();
      }
      db.setTransactionSuccessful();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      db.endTransaction();
    }
  }

  public static ArrayList getDepart() {
    emisSQLiteWrapper db = emisDb.getInstance();
    ArrayList departList = new ArrayList();
    String sql = "select D_NO,D_NAME,D_NAME_S from DEPART where CCRUSED='Y'";
    try {
      SQLiteCursor cursor = db.executeQuery(sql);

      while (cursor.moveToNext()) {
        Map<String, String> depart = new HashMap<>();
        String D_NO = cursor.getString(0);
        String D_NAME = cursor.getString(1);
        String D_NAME_S = cursor.getString(2);
        depart.put("d_no", D_NO);
        depart.put("d_name", D_NAME);
        depart.put("d_name_s", D_NAME_S);
        departList.add(depart);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return departList;
  }

  public static ArrayList getSubdep(String D_NO) {
    emisSQLiteWrapper db = emisDb.getInstance();

    String sql = "select sub.SUBDEP,sub.SUB_NAME,sub.SUB_NAME_S," +
      "p.p_no,p.name,p.price,p.price2,p.price3,p.price4,price5,price6," +
      "p_tax from subdep sub " +
      "left join part p on p.subdep=sub.SUBDEP " +
      "where sub.D_NO='" + D_NO + "' group by sub.subdep";
    System.out.println("SQL:" + sql);
    ArrayList subdepList = new ArrayList();
    try {
      SQLiteCursor cursor = db.executeQuery(sql);
      while (cursor.moveToNext()) {
        String SUBDEP = cursor.getString(0);
        String SUB_NAME = cursor.getString(1);
        String SUB_NAME_S = cursor.getString(2);
        String P_NO = cursor.getString(3);
        String P_NAME = cursor.getString(4);
        Double price = cursor.getDouble(5);
        Double price2 = cursor.getDouble(6);
        Double price3 = cursor.getDouble(7);
        Double price4 = cursor.getDouble(8);
        Double price5 = cursor.getDouble(9);
        Double price6 = cursor.getDouble(10);
        String p_tax = cursor.getString(11);

        Map<String, Object> subdep = new HashMap<>();
        subdep.put("subdep", SUBDEP);
        subdep.put("sub_name", SUB_NAME);
        subdep.put("sub_name_s", SUB_NAME_S);
        subdep.put("p_no", P_NO);
        subdep.put("p_name", P_NAME);
        subdep.put("price", price);
        subdep.put("price2", price2);
        subdep.put("price3", price3);
        subdep.put("price4", price4);
        subdep.put("price5", price5);
        subdep.put("price6", price6);
        subdep.put("p_tax", p_tax);
        subdepList.add(subdep);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return subdepList;
  }

  public static void saveSubDepart(ArrayList subdepartList) throws Exception {
    SQLiteStatement stmtSaveInProp;
    String insertDepart = "insert into SUBDEP(SUBDEP,SUB_NAME,SUB_NAME_S,D_NO,USED) \n" +
      "values(?,?,?,?,?)";
    emisSQLiteWrapper db = emisDb.getInstance();
    stmtSaveInProp = db.compileStmt(insertDepart);
    try {
      db.beginTransaction();
      db.execSQL("delete from subdep");
      for (int i = 0; i < subdepartList.size(); i++) {
        LinkedTreeMap<String, Object> depart = (LinkedTreeMap<String, Object>) subdepartList.get(i);
        String SUBDEP = (String) depart.get("subdep");
        String SUB_NAME = (String) depart.get("sub_name");
        String SUB_NAME_S = (String) depart.get("sub_name_s");
        String D_NO = (String) depart.get("d_no");
        String USED = (String) depart.get("used");

        stmtSaveInProp.clearBindings();
        stmtSaveInProp.bindString(1, SUBDEP);
        stmtSaveInProp.bindString(2, SUB_NAME);
        stmtSaveInProp.bindString(3, SUB_NAME_S);
        stmtSaveInProp.bindString(4, D_NO);
        stmtSaveInProp.bindString(5, USED);
        stmtSaveInProp.executeInsert();
      }
      db.setTransactionSuccessful();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      db.endTransaction();
    }
  }

  public static void saveSeaH(ArrayList seaHList) throws Exception {
    SQLiteStatement stmtSave;
    String insertDepart = "insert into SEASONING_H(SEA_NO,SEA_NAME,SEA_TYPE) \n" +
      "values(?,?,?)";
    emisSQLiteWrapper db = emisDb.getInstance();
    stmtSave = db.compileStmt(insertDepart);
    try {
      db.beginTransaction();
      db.execSQL("delete from seasoning_h");
      for (int i = 0; i < seaHList.size(); i++) {
        LinkedTreeMap<String, Object> object = (LinkedTreeMap<String, Object>) seaHList.get(i);
        String SEA_NO = (String) object.get("sea_no");
        String SEA_NAME = (String) object.get("sea_name");
        String SEA_TYPE = (String) object.get("sea_type");

        stmtSave.clearBindings();
        stmtSave.bindString(1, SEA_NO);
        stmtSave.bindString(2, SEA_NAME);
        stmtSave.bindString(3, SEA_TYPE);
        stmtSave.executeInsert();
      }
      db.setTransactionSuccessful();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      db.endTransaction();
    }
  }

  public static void saveSeaD(ArrayList seaDList) throws Exception {
    SQLiteStatement stmtSave;
    String insertDepart = "insert into SEASONING_D(SEA_ITEM_NO,SEA_NO,SEA_ITEM_NAME,DEF_CHOOSE,PRICE,PRICE2,PRICE3,USED,P_NO) \n" +
      "values(?,?,?,?,?,?,?,?,?)";
    emisSQLiteWrapper db = emisDb.getInstance();
    stmtSave = db.compileStmt(insertDepart);
    try {
      db.beginTransaction();
      db.execSQL("delete from seasoning_d");
      for (int i = 0; i < seaDList.size(); i++) {
        LinkedTreeMap<String, Object> object = (LinkedTreeMap<String, Object>) seaDList.get(i);
        String SEA_ITEM_NO = (String) object.get("sea_item_no");
        String SEA_NO = (String) object.get("sea_no");
        String SEA_ITEM_NAME = (String) object.get("sea_item_name");
        String DEF_CHOOSE = "N";
        Double PRICE = (Double) object.get("price");
        Double PRICE2 = (Double) object.get("price2");
        Double PRICE3 = (Double) object.get("price3");
        String USED = (String) object.get("used");
        String P_NO = (String) object.get("p_no");

        stmtSave.clearBindings();
        stmtSave.bindString(1, SEA_ITEM_NO);
        stmtSave.bindString(2, SEA_NO);
        stmtSave.bindString(3, SEA_ITEM_NAME);
        stmtSave.bindString(4, DEF_CHOOSE);
        stmtSave.bindDouble(5, PRICE);
        stmtSave.bindDouble(6, PRICE2);
        stmtSave.bindDouble(7, PRICE3);
        stmtSave.bindString(8, USED);
        stmtSave.bindString(9, P_NO);
        stmtSave.executeInsert();
      }
      db.setTransactionSuccessful();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      db.endTransaction();
    }
  }

  public static void savePart_N(ArrayList peanList) throws Exception {
    SQLiteStatement stmtSave;
    String insertDepart = "insert into part_n (p_ean,p_no,p_is_ean) " +
      "values(?,?,?)";
    emisSQLiteWrapper db = emisDb.getInstance();
    stmtSave = db.compileStmt(insertDepart);
    try {
      db.beginTransaction();
      db.execSQL("delete from part_n");
      for (int i = 0; i < peanList.size(); i++) {
        LinkedTreeMap<String, Object> object = (LinkedTreeMap<String, Object>) peanList.get(i);
        String P_EAN = (String) object.get("p_ean");
        String P_NO = (String) object.get("p_no");
        String P_IS_EAN = (String) object.get("p_is_ean");

        stmtSave.clearBindings();
        stmtSave.bindString(1, P_EAN);
        stmtSave.bindString(2, P_NO);
        stmtSave.bindString(3, P_IS_EAN);
        stmtSave.executeInsert();
      }
      db.setTransactionSuccessful();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      db.endTransaction();
    }
  }

  public static void saveStaff(ArrayList listSource) throws Exception {
    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteStatement stmt;

    String sql = "insert into STAFF "
      + "(ST_NO, OP_LEVEL, OP_PASSWORD, USERID, PASSWD) "
      + "values "
      + "(?, ?, ?, ?, ?) ";
    int index;
    LinkedTreeMap<String, Object> map;

    try {
      stmt = db.compileStmt(sql);
      db.beginTransaction();

      db.execSQL("delete from STAFF");

      for (index = 0; index < listSource.size(); index++) {
        map = (LinkedTreeMap<String, Object>) listSource.get(index);

        System.out.println(map.toString());

        stmt.clearBindings();
        stmt.bindString(1, (String) map.get("st_no"));
        stmt.bindString(2, (String) map.get("op_level"));
        stmt.bindString(3, (String) map.get("op_password"));
        stmt.bindString(4, (String) map.get("userid"));
        stmt.bindString(5, (String) map.get("passwd"));
        // stmt.bindString(4, "");
        // stmt.bindString(5, "");
        stmt.executeInsert();
      }
      db.setTransactionSuccessful();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      db.endTransaction();
    }
  }

  public static void saveSMENU_H(ArrayList smenu_hList) throws Exception {
    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteStatement stmt;

    String sql = "insert into SMENU_H "
      + "(SM_NO, SM_NAME, USED, REMARK, GROUP_TYPE, GROUP_NAME, MAX_NUM, SM_DATE_S, SM_DATE_E) "
      + "values "
      + "(?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    int index;
    LinkedTreeMap<String, Object> map;

    try {
      stmt = db.compileStmt(sql);
      db.beginTransaction();

      db.execSQL("delete from SMENU_H");

      for (index = 0; index < smenu_hList.size(); index++) {
        map = (LinkedTreeMap<String, Object>) smenu_hList.get(index);

        stmt.clearBindings();
        stmt.bindString(1, (String) map.get("sm_no"));
        stmt.bindString(2, (String) map.get("sm_name"));
        stmt.bindString(3, (String) map.get("used"));
        stmt.bindString(4, (String) map.get("remark"));
        stmt.bindString(5, (String) map.get("group_type"));
        stmt.bindString(6, (String) map.get("group_name"));
        stmt.bindLong(7, ((Double) map.get("max_num")).intValue());
        stmt.bindString(8, (String) map.get("sm_date_s"));
        stmt.bindString(9, (String) map.get("sm_date_e"));
        stmt.executeInsert();
      }

      db.setTransactionSuccessful();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      db.endTransaction();
    }
  }

  public static void saveSMENU_D(ArrayList smnue_dList) throws Exception {
    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteStatement stmt;

    String sql = "insert into SMENU_D "
      + "(SM_NO, RECNO, P_NO, SM_QTY, GROUP_TYPE, ADD_PRICE) "
      + "values "
      + "(?, ?, ?, ?, ?, ?) ";
    int index;
    LinkedTreeMap<String, Object> map;

    try {
      stmt = db.compileStmt(sql);
      db.beginTransaction();

      db.execSQL("delete from SMENU_D");

      for (index = 0; index < smnue_dList.size(); index++) {
        map = (LinkedTreeMap<String, Object>) smnue_dList.get(index);

        stmt.clearBindings();
        stmt.bindString(1, (String) map.get("sm_no"));
        stmt.bindLong(2, ((Double) map.get("recno")).intValue());
        stmt.bindString(3, (String) map.get("p_no"));
        stmt.bindLong(4, ((Double) map.get("sm_qty")).intValue());
        stmt.bindString(5, (String) map.get("group_type"));
        stmt.bindLong(6, ((Double) map.get("add_price")).intValue());
        stmt.executeInsert();
      }

      db.setTransactionSuccessful();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      db.endTransaction();
    }
  }

  public static void saveDisc(ArrayList disList) throws Exception {
    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteStatement stmtSave;

    String sql = "insert into functions_disc "
      + "(FUNC_ID, FUNC_NAME, GROUP_ID, USED, FUNC_STR1, FUNC_STR2, FUNC_NUM1, FUNC_NUM2, REMARK, SEQ_NO) "
      + "values "
      + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    int index;
    LinkedTreeMap<String, Object> map;

    try {
      stmtSave = db.compileStmt(sql);
      db.beginTransaction();

      db.execSQL("delete from functions_disc");

      for (index = 0; index < disList.size(); index++) {
        map = (LinkedTreeMap<String, Object>) disList.get(index);

        stmtSave.clearBindings();
        stmtSave.bindString(1, (String) map.get("func_id"));
        stmtSave.bindString(2, (String) map.get("func_name"));
        stmtSave.bindString(3, (String) map.get("group_id"));
        stmtSave.bindString(4, (String) map.get("used"));
        stmtSave.bindString(5, (String) map.get("func_str1"));
        stmtSave.bindString(6, (String) map.get("func_str2"));
        stmtSave.bindLong(7, ((Double) map.get("func_num1")).intValue());
        stmtSave.bindLong(8, ((Double) map.get("func_num2")).intValue());
        stmtSave.bindString(9, (String) map.get("remark"));
        stmtSave.bindLong(10, index);
        stmtSave.executeInsert();
      }

      db.setTransactionSuccessful();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      db.endTransaction();
    }
  }

  public static void saveFUNCTIONS_MPOS(ArrayList listSource) throws Exception {
    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteStatement stmt;

    String sql = "insert into FUNCTIONS_MPOS "
      + "(ID_NO, FUNC_ID, FUNC_NAME, GROUPID, USED, FUNC_STR1, FUNC_STR2, FUNC_NUM1, FUNC_NUM2, REMARK, "
      + " FUNC_RIGHT, SEQ_NO) "
      + "values "
      + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
      + " ?, ?) ";
    int index;
    LinkedTreeMap<String, Object> map;

    try {
      stmt = db.compileStmt(sql);
      db.beginTransaction();

      db.execSQL("delete from FUNCTIONS_MPOS");

      for (index = 0; index < listSource.size(); index++) {
        map = (LinkedTreeMap<String, Object>) listSource.get(index);

        stmt.clearBindings();
        stmt.bindString(1, (String) map.get("ID_NO"));
        stmt.bindString(2, (String) map.get("FUNC_ID"));
        stmt.bindString(3, (String) map.get("FUNC_NAME"));
        stmt.bindString(4, "");
        stmt.bindString(5, (String) map.get("USED"));
        stmt.bindString(6, "");
        stmt.bindString(7, "");
        stmt.bindLong(8, 0);
        stmt.bindDouble(9, 0.0);
        stmt.bindString(10, "");
        stmt.bindString(11, (String) map.get("FUNC_RIGHT"));
        stmt.bindLong(12, ((Double) map.get("SEQ_NO")).intValue());
        stmt.executeInsert();
      }

      db.setTransactionSuccessful();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      db.endTransaction();
    }
  }

  public static boolean synProp(ArrayList listSource) throws Exception {
    emisLog.addLog("-- synProp --");
    emisLog.closeLog();

    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteStatement updDataStmt;
    SQLiteStatement insDataStmt;
    int index;
    LinkedTreeMap<String, Object> map;

    String sql = "update Emisprop set VALUE = ?, KIND = ?, REMARK = ?, REMARK2 = ?, MENU = ?, UPD_DATE = ?, ISROOT = ? where NAME = ? ";
    String sql2 = "insert into Emisprop(NAME, VALUE, KIND, REMARK, REMARK2, MENU, UPD_USER, UPD_DATE, ISROOT) values(?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    try {
      String today = emisUtil.todayDateAD();
      updDataStmt = db.compileStmt(sql);
      insDataStmt = db.compileStmt(sql2);
      db.beginTransaction();

      int iParam = 1;
      for (index = 0; index < listSource.size(); index++) {
        map = (LinkedTreeMap<String, Object>) listSource.get(index);
        iParam = 1;
        updDataStmt.clearBindings();
        updDataStmt.bindString(iParam++, (String) map.get("value"));
        updDataStmt.bindString(iParam++, (String) map.get("kind"));
        updDataStmt.bindString(iParam++, (String) map.get("remark"));
        updDataStmt.bindString(iParam++, (String) map.get("remark2"));
        updDataStmt.bindString(iParam++, (String) map.get("menu"));
        updDataStmt.bindString(iParam++, today);
        updDataStmt.bindString(iParam++, (String) map.get("isRoot"));
        updDataStmt.bindString(iParam++, (String) map.get("name"));
        if (updDataStmt.executeUpdateDelete() <= 0) {
          iParam = 1;
          insDataStmt.clearBindings();
          insDataStmt.bindString(iParam++, (String) map.get("name"));
          insDataStmt.bindString(iParam++, (String) map.get("value"));
          insDataStmt.bindString(iParam++, (String) map.get("kind"));
          insDataStmt.bindString(iParam++, (String) map.get("remark"));
          insDataStmt.bindString(iParam++, (String) map.get("remark2"));
          insDataStmt.bindString(iParam++, (String) map.get("menu"));
          insDataStmt.bindString(iParam++, "");
          insDataStmt.bindString(iParam++, today);
          insDataStmt.bindString(iParam++, (String) map.get("isRoot"));
          insDataStmt.executeInsert();
        }
      }
      db.setTransactionSuccessful();
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
      //throw ex;
    } finally {
      db.endTransaction();
    }
  }

  public static ArrayList getDisc() {
    emisSQLiteWrapper db = emisDb.getInstance();
    ArrayList discList = new ArrayList();
    String sql = "select FUNC_ID,FUNC_NAME,FUNC_NUM1,GROUP_ID,SEQ_NO from FUNCTIONS_DISC where USED='Y'";

    try {
      SQLiteCursor cursor = db.executeQuery(sql);

      while (cursor.moveToNext()) {
        Map<String, Object> discount = new HashMap<>();

        String FUNC_ID = cursor.getString(0);
        String FUNC_NAME = cursor.getString(1);
        int FUNC_NUM1 = cursor.getInt(2);
        String GROUP_ID = cursor.getString(3);
        int SEQ_NO = cursor.getInt(4);

        discount.put("func_id", FUNC_ID);
        discount.put("func_name", FUNC_NAME);
        discount.put("func_num1", FUNC_NUM1);
        discount.put("group_id", GROUP_ID);
        discount.put("da_seq", SEQ_NO);
        discList.add(discount);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return discList;
  }

  public static void updateClearDate(String date) {
    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteStatement stmt;

    try {
      String sTime = emisUtil.todayTimeS(false);
      stmt = db.compileStmt("update sale_h set eo_date = ?,eo_time=? where eo_date = ''; ");
      stmt.bindString(1, date);
      stmt.bindString(2, sTime);
      stmt.execute();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void removeOldDate(String date) {
    emisSQLiteWrapper db = emisDb.getInstance();

    String sqlRemove = "delete from sale_h where sl_date<'" + date + "'";
    db.execSQL(sqlRemove);
    sqlRemove = "delete from sale_d where sl_date<'" + date + "'";
    db.execSQL(sqlRemove);
    sqlRemove = "delete from sale_p where sl_date<'" + date + "'";
    db.execSQL(sqlRemove);
    sqlRemove = "delete from sale_dis where sl_date<'" + date + "'";
    db.execSQL(sqlRemove);
    sqlRemove = "delete from sale_s where sl_date<'" + date + "'";
    db.execSQL(sqlRemove);
  }

  public static Intent transaction_test(int type, String... data) {
    Intent intent = new Intent();
    switch (type) {
      case 1:
        intent.setClassName("com.cybersoft.a920", "com.cybersoft.a920.activity.MainActivity");
        break;
      case 2:
        intent.setClassName("com.cybersoft.a920.scp", "com.cybersoft.a920.scp.activity.MainActivity");
        break;
      case 3:
        intent.setClassName("com.cybersoft.a920", "com.cybersoft.a920.activity.MainActivity");
        break;
      case 4:
        intent.setClassName("com.cybersoft.a920.scp", "com.cybersoft.a920.scp.activity.MainActivity");
        break;
      case 5:
        intent.setClassName("com.cybersoft.a920", "com.cybersoft.a920.activity.MainActivity");
        break;
      case 6:
        intent.setClassName("com.cybersoft.a920.scp", "com.cybersoft.a920.scp.activity.MainActivity");
        break;
    }
    Bundle bundle = new Bundle();

    switch (type) {
      case 1:
        bundle.putString("POS_REQUEST", "{\"Trans_Type\":\"11\",\"Trans_Amount\": \"" + data[0] + "\"}");
        break;
      //Credit
      //case 2:bundle.putString("POS_REQUEST", "{\"Trans_Type\":\"04\",\"Trans_Amount\": \"1000\",\"Installment_Period\":\"3\"}");break;
      //Elec
      case 2:
        bundle.putString("POS_REQUEST", "{\"Trans_Amount\":\"" + data[0] + "\",\"Trans_Type\": \"11\",\"Training_Mode\":true}");
        break;
      case 3:
        bundle.putString("POS_REQUEST", "{\"Trans_Type\":\"50\"}");
        break;
      case 4:
        bundle.putString("POS_REQUEST", "{\"Trans_Type\":\"50\"}");
        break;
      case 5:
        bundle.putString("POS_REQUEST", "{\"Trans_Type\":\"12\",\"Trans_Amount\": \"" + data[0] + "\"}");
        break;
      case 6:
        bundle.putString("POS_REQUEST", "{\"Trans_Type\":\"12\",\"Trans_Amount\": \"" + data[0] + "\"}");
        break;
      case 7:
        bundle.putString("POS_REQUEST", "{\"Trans_Type\":\"02\",\"Trans_Amount\": \"1000\"}");
        break;
      case 8:
        bundle.putString("POS_REQUEST", "{\"Trans_Type\":\"22\",\"Trans_Amount\": \"1000\"}");
        break;
      case 9:
        bundle.putString("POS_REQUEST", "{\"Trans_Type\":\"32\",\"Trans_Amount\": \"1000\"}");
        break;
      case 10:
        bundle.putString("POS_REQUEST", "{\"Trans_Type\":\"30\",\"Trans_Amount\": \"1000\"}");
        break;
      case 11:
        bundle.putString("POS_REQUEST", "{\"Trans_Type\":\"50\",\"Trans_Amount\": \"1000\"}");
        break;
      default:
        break;
    }

    intent.putExtras(bundle);
    return intent;
  }

  public static String getOldDate(int distanceDay) {
    SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
    Date beginDate = new Date();
    Calendar date = Calendar.getInstance();

    date.setTime(beginDate);
    date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
    Date endDate = null;

    try {
      endDate = dft.parse(dft.format(date.getTime()));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return dft.format(endDate);
  }

  public static String isBasedataEmpty() {
    String res = "";
    emisSQLiteWrapper _oDb = emisDb.getInstance();

    int count = 0;

    /*String SQL = "select count(1) from PART";
    _oDb.compileStmt(SQL);
    count = _oDb.executeQuery();
    if (count == 0) {
      res = "沒有獲取到商品資訊。";
      return res;
    }*/

    /*SQL = "select count(1) from STAFF";
    _oDb.compileStmt(SQL);
    count = _oDb.executeQuery();
    if (count == 0) {
      res = "沒有獲取到用戶資訊。";
      return res;
    }*/

    res = "success";
    return res;
  }

  public static boolean isOverDate() {
    boolean result = false;

    DateFormat df = new SimpleDateFormat("yyyyMMdd");
    Date d1, d2;
    long days;

    try {
      d1 = df.parse(getFromProp("LastUpdateDate"));
      d2 = df.parse(Time.getOnlyDate());

      days = (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24); // 天数差

      if (days < 7) result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static boolean checkUser(final String user, final String password) {
    boolean result = false;
    emisSQLiteWrapper db = emisDb.getInstance();

    if ("emis".equalsIgnoreCase(user)) {
      //星期+日期+月（十六进制）+小时（十六进制）
      String curDate = emisUtil.getSYSWEEK() + emisUtil.getSYSDATE()
        + Integer.toHexString(emisUtil.parseInt(emisUtil
        .getDATE("MM"))) //$NON-NLS-1$
        + Integer.toHexString(emisUtil.parseInt(emisUtil
        .getDATE("hh"))); //$NON-NLS-1$
      result = (curDate.equalsIgnoreCase(password));
    } else {
      db.compileStmt("select count(1) from cashier where OP_NO = ? and OP_PASSWORD = ? ");
      db.setString(1, user);
      db.setString(2, password);
      if (db.executeQuery() == 1) result = true;
    }

    return result;
  }

  public static void allHostsValid() throws NoSuchAlgorithmException, KeyManagementException {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
      public X509Certificate[] getAcceptedIssuers() {
        return null;
      }

      public void checkClientTrusted(X509Certificate[] certs, String authType) {
      }

      public void checkServerTrusted(X509Certificate[] certs, String authType) {
      }
    }
    };

    // Install the all-trusting trust manager
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
      public boolean verify(String hostname, SSLSession session) {
        return true;
      }
    };

    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
  }

  public static ArrayList getPayment() {
    emisSQLiteWrapper db = emisDb.getInstance();
    ArrayList payList = new ArrayList();
    String sSql = "select PAY_NO,PAY_NAME,EDC_NO from payment";
    try {
      SQLiteCursor cursor = db.executeQuery(sSql);
      while (cursor.moveToNext()) {
        Map<String, Object> payment = new HashMap<>();
        String PAY_NO = cursor.getString(0);
        String PAY_NAME = cursor.getString(1);
        String EDC_NO = cursor.getString(2);
        payment.put("pay_no", PAY_NO);
        payment.put("pay_name", PAY_NAME);
        payment.put("edc_no", EDC_NO);
        payList.add(payment);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return payList;
  }

  public static Map<String, Object> queryLocalPartMap(String P_NO) {
    Map<String, Object> result = null;
    Gson gson = new Gson();

    String product = BaseUtil.getProduct(P_NO);
    if (product.contains("price"))
      result = (Map<String, Object>) gson.fromJson(product, (new HashMap<String, Object>()).getClass());

    return result;
  }

  public static String queryLocalPartName(String P_NO) {
    String result = "";

    Map<String, Object> map = BaseUtil.queryLocalPartMap(P_NO);
    if (map != null) result = (String) map.get("name");

    return result;
  }

  public static Map<String, Object> queryLocalSeaItem(String seaItemNo) {
    HashMap<String, Object> result = null;

    emisSQLiteWrapper db = emisDb.getInstance();
    Cursor cursor;
    String sql = "select "
      + "SEA_ITEM_NO, "
      + "SEA_NO, "
      + "SEA_ITEM_NAME, "
      + "DEF_CHOOSE, "
      + "PRICE, "
      + "PRICE2, "
      + "PRICE3, "
      + "USED, "
      + "P_NO "
      + "from SEASONING_D "
      + "where (1=1) "
      + "and SEA_ITEM_NO='" + seaItemNo + "' ";

    try {
      cursor = db.executeQuery(sql);
      if (cursor.moveToNext()) {
        result = new HashMap<String, Object>();

        result.put("SEA_ITEM_NO", cursor.getString(0));
        result.put("SEA_NO", cursor.getString(1));
        result.put("SEA_ITEM_NAME", cursor.getString(2));
        result.put("DEF_CHOOSE", cursor.getString(3));
        result.put("PRICE", cursor.getDouble(4));
        result.put("PRICE2", cursor.getDouble(5));
        result.put("PRICE3", cursor.getDouble(6));
        result.put("USED", cursor.getString(7));
        result.put("P_NO", cursor.getString(8));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static String queryLocalSeaItemName(String seaItemNo) {
    String result = "";

    Map<String, Object> map = queryLocalSeaItem(seaItemNo);
    if (map != null) result = (String) map.get("SEA_ITEM_NAME");

    return result;
  }

  public static void getInitData(final String S_NO, final String ID_NO, final String ORG_DOMAIN, final String API_URL, Callback callback) throws Exception {
    final OkHttpClient okClient = new OkHttpClient().newBuilder().sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();

    JSONObject jsonObj = new JSONObject();
    jsonObj.put("S_NO", S_NO);
    jsonObj.put("ID_NO", ID_NO);
    jsonObj.put("K", BaseUtil.getAppid());

    System.out.println("getInitData POST content:" + jsonObj.toString());

    RequestBody body = RequestBody.create(emisAndroidUtil.JSON, AES.encrypt(jsonObj.toString()));
    Request request = new Request.Builder().addHeader("sUser", ORG_DOMAIN).url(API_URL + "/ws/sunmi/v1/getInitData").post(body).build();

    okClient.newCall(request).enqueue(callback);
  }

  public static void saveInitData(Context context, Map<String, Object> map) {
    // System.out.println("tax:" + ((Double) map.get("tax")).toString());

    emisKeeper.getInstance().setsInfo("taxRate", ((Double) map.get("tax")).toString(), context);
    emisKeeper.getInstance().setsInfo("s_name", (String) map.get("s_name"), context);
    emisKeeper.getInstance().setsInfo("addr", (String) map.get("s_addr"), context);
    emisKeeper.getInstance().setsInfo("tel", (String) map.get("s_tel"), context);
    emisKeeper.getInstance().setsInfo("company", (String) map.get("company"), context);
    emisKeeper.getInstance().setsInfo("key", (String) map.get("key"), context);
    emisKeeper.getInstance().setsInfo("unino", (String) map.get("uni_no"), context);

    BaseUtil.saveInProp("qrKey", (String) map.get("key"));
    BaseUtil.saveInProp("LOGO", (String) map.get("logo"));

    BaseUtil.updateFromProp("INIT", "Y");
  }

  public static void saveSettings(ArrayList emisprop_pos, Context context) throws Exception {
    int index;
    boolean booValue;
    try {
      for (index = 0; index < emisprop_pos.size(); index++) {
        LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) emisprop_pos.get(index);

        if (((String) map.get("name")).equals("ACTIVENIDIN")) {
          booValue = false;
          if (((String) map.get("value")).equals("Y")) booValue = true;

          emisKeeper.getInstance().setbInfo("ActiveNidin", booValue, context);
        } else if (((String) map.get("name")).equals("DETAIL_ON")) {
          booValue = false;
          if (((String) map.get("value")).equals("Y")) booValue = true;

          emisKeeper.getInstance().setbInfo("DETAIL_ON", booValue, context);
        } else if (((String) map.get("name")).equals("EINV_ON")) {
          emisKeeper.getInstance().setsInfo("EINV_ON", (String) map.get("value"), context);
        } else if (((String) map.get("name")).equals("DAY_REFUND")) {
          emisKeeper.getInstance().setsInfo("Day_Refund", (String) map.get("value"), context);
        } else if (((String) map.get("name")).equals("MCHTYPE")) {
          emisKeeper.getInstance().setsInfo("mchType", (String) map.get("value"), context);
        } else if (((String) map.get("name")).equals("MPAY_TIMEOUT")) {
          emisKeeper.getInstance().setsInfo("MPAY_TIMEOUT", (String) map.get("value"), context);
        } else if (((String) map.get("name")).equals("MQTT_URL")) {
          emisKeeper.getInstance().setsInfo("MQTT_URL", (String) map.get("value"), context);
        } else if (((String) map.get("name")).equals("PRICE_SELECT")) {
          emisKeeper.getInstance().setsInfo("Price_select", (String) map.get("value"), context);
        } else if (((String) map.get("name")).equals("TIME_CLEAR_S")) {
          emisKeeper.getInstance().setsInfo("Time_Clear_S", (String) map.get("value"), context);
        } else if (((String) map.get("name")).equals("TIME_CLEAR_E")) {
          emisKeeper.getInstance().setsInfo("Time_Clear_E", (String) map.get("value"), context);
        } else if (((String) map.get("name")).equals("SAO_CHK_TIME")) {
          emisKeeper.getInstance().setsInfo("SAO_CHK_TIME", (String) map.get("value"), context);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
    }
  }

  public static void sendFileToPostlog(final String orgDomain, File file, Callback callback) {
    final OkHttpClient okClient = new OkHttpClient().newBuilder().sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(SSLSocketClient.getHostnameVerifier()).build();

    RequestBody requestBody;
    Request request;
    try {
      requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("filename", file.getName())
        .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
        .build();

      request = new Request.Builder().addHeader("sUser", orgDomain).url(emisAndroidUtil.WWW_EMIS_URL + "/PostLog").post(requestBody).build();

      okClient.newCall(request).enqueue(callback);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void saveToNODCLOSE() {
    emisSQLiteWrapper db = emisDb.getInstance();
    String sql;
    String date = Time.getOnlyDate();
    String time = Time.getOnlyTime();

    try {
      db.beginTransaction();

      sql = "insert into NODCLOSE_SALE_H "
        + "( "
        + "SL_KEY, S_NO, ID_NO, SL_DATE, SL_NO, SL_TIME, SL_INKIND, SL_INVID, C_NO, OP_NO, "
        + "SA_NO, SL_SOURCE, SL_INVTYPE, SL_INVNO_S, SL_KEY_O, TIME_NO, PR_NO, FLS_NO, SL_CONFIRM_E, SL_CONFIRM_D, "
        + "SL_QTY, SL_AMT, SL_TAXAMT, SL_NOTAXAMT, SL_DISC_AMT, SL_NDISC_AMT, SL_RECV_AMT, SL_TAXAMT_AMT, SL_INVAMT, REMARK, "
        + "CRE_USER, CRE_DATE, CC_NO, S_NO_OUT, CLIENT_COUNT, SL_EINV, CT_NO, ePrintMark, eRandom, ePrnTimes, "
        + "eClear_NO, eHide_NO, eDonate, SL_TYPE, MANUAL_DISC, MANUAL_DISC_CODE, SL_SORT "
        + ", DCLOSE_DATE, DCLOSE_TIME) "
        + "select "
        + "SL_KEY, S_NO, ID_NO, SL_DATE, SL_NO, SL_TIME, SL_INKIND, SL_INVID, C_NO, OP_NO, "
        + "SA_NO, SL_SOURCE, SL_INVTYPE, SL_INVNO_S, SL_KEY_O, TIME_NO, PR_NO, FLS_NO, SL_CONFIRM_E, SL_CONFIRM_D, "
        + "SL_QTY, SL_AMT, SL_TAXAMT, SL_NOTAXAMT, SL_DISC_AMT, SL_NDISC_AMT, SL_RECV_AMT, SL_TAXAMT_AMT, SL_INVAMT, REMARK, "
        + "CRE_USER, CRE_DATE, CC_NO, S_NO_OUT, CLIENT_COUNT, SL_EINV, CT_NO, ePrintMark, eRandom, ePrnTimes, "
        + "eClear_NO, eHide_NO, eDonate, SL_TYPE, MANUAL_DISC, MANUAL_DISC_CODE, SL_SORT "
        + ", " + date + ", " + time + " "
        + "from SALE_H "
        + "where (1 = 1) "
        + "and SL_KEY in (select SL_KEY from SALE_H where (1 = 1) and (EO_DATE = '')) ";
      db.execSQL(sql);

      sql = "insert into NODCLOSE_SALE_D "
        + "( "
        + "SL_KEY, RECNO, S_NO, ID_NO, SL_DATE, SL_NO, FLS_NO, P_NO, DP_NO, P_TAX, "
        + "SL_QTY, SL_PRICE, SL_AMT, SL_TAXAMT, SL_NOTAXAMT, SL_DISC_AMT, SL_NDISC_AMT, RECNO_CCR, P_PRICE, PK_RECNO, "
        + "SL_TAXAMT_AMT, REMARK, SL_MEMO, DD_NO, SYS_DISC, SYS_DISC_AMT, SYS_DIS_AMT_D, SL_TAX_AMT, SEA_NO, SEA_AMT "
        + ") "
        + "select "
        + "SL_KEY, RECNO, S_NO, ID_NO, SL_DATE, SL_NO, FLS_NO, P_NO, DP_NO, P_TAX, "
        + "SL_QTY, SL_PRICE, SL_AMT, SL_TAXAMT, SL_NOTAXAMT, SL_DISC_AMT, SL_NDISC_AMT, RECNO_CCR, P_PRICE, PK_RECNO, "
        + "SL_TAXAMT_AMT, REMARK, SL_MEMO, DD_NO, SYS_DISC, SYS_DISC_AMT, SYS_DIS_AMT_D, SL_TAX_AMT, SEA_NO, SEA_AMT "
        + "from SALE_D "
        + "where (1 = 1) "
        + "and SL_KEY in (select SL_KEY from SALE_H where (1 = 1) and (EO_DATE = '')) ";
      db.execSQL(sql);

      sql = "insert into NODCLOSE_SALE_P "
        + "( "
        + "SL_KEY, RECNO, S_NO, ID_NO, SL_DATE, SL_TIME, SL_NO, PAY_NO, PAY_AMT, PAY_EX1, "
        + "PAY_EX2, PAY_EX3, PAY_EX6, PAY_EX7, PAY_EX8, PAY_EX9, PAY_EX10, BANK_CODE "
        + ") "
        + "select "
        + "SL_KEY, RECNO, S_NO, ID_NO, SL_DATE, SL_TIME, SL_NO, PAY_NO, PAY_AMT, PAY_EX1, "
        + "PAY_EX2, PAY_EX3, PAY_EX6, PAY_EX7, PAY_EX8, PAY_EX9, PAY_EX10, BANK_CODE "
        + "from SALE_P "
        + "where (1 = 1) "
        + "and SL_KEY in (select SL_KEY from SALE_H where (1 = 1) and (EO_DATE = '')) ";
      db.execSQL(sql);

      sql = "insert into NODCLOSE_SALE_PK "
        + "( "
        + "SL_KEY, PK_RECNO, S_NO, ID_NO, SL_DATE, SL_NO, FLS_NO, P_NO, P_TAX, SL_QTY, "
        + "SL_PRICE, SL_AMT, SL_TAXAMT, SL_NOTAXAMT, SL_DISC_AMT, SL_NDISC_AMT, SL_DISC_RATE, SL_TAXAMT_AMT "
        + ") "
        + "select "
        + "SL_KEY, PK_RECNO, S_NO, ID_NO, SL_DATE, SL_NO, FLS_NO, P_NO, P_TAX, SL_QTY, "
        + "SL_PRICE, SL_AMT, SL_TAXAMT, SL_NOTAXAMT, SL_DISC_AMT, SL_NDISC_AMT, SL_DISC_RATE, SL_TAXAMT_AMT "
        + "from SALE_PK "
        + "where (1 = 1) "
        + "and SL_KEY in (select SL_KEY from SALE_H where (1 = 1) and (EO_DATE = '')) ";
      db.execSQL(sql);

      sql = "insert into NODCLOSE_SALE_S "
        + "( "
        + "SL_KEY, SD_RECNO, RECNO, S_NO, ID_NO, SL_DATE, SL_NO, P_NO, SEA_NO, SEA_TYPE, "
        + "SEA_ITEM_NAME, SEA_QTY, SEA_AMT, P_NO_S "
        + ") "
        + "select "
        + "SL_KEY, SD_RECNO, RECNO, S_NO, ID_NO, SL_DATE, SL_NO, P_NO, SEA_NO, SEA_TYPE, "
        + "SEA_ITEM_NAME, SEA_QTY, SEA_AMT, P_NO_S "
        + "from SALE_S "
        + "where (1 = 1) "
        + "and SL_KEY in (select SL_KEY from SALE_H where (1 = 1) and (EO_DATE = '')) ";
      db.execSQL(sql);

      sql = "insert into NODCLOSE_SALE_DIS "
        + "( "
        + "SL_KEY, S_NO, ID_NO, SL_DATE, SL_NO, SL_SOURCE, RECNO, DISC_SN, DISC_CODE, DISC_NO, "
        + "DISC_AMT, DISC_QTY, P_NO, IS_SALEPK, REASON "
        + ") "
        + "select "
        + "SL_KEY, S_NO, ID_NO, SL_DATE, SL_NO, SL_SOURCE, RECNO, DISC_SN, DISC_CODE, DISC_NO, "
        + "DISC_AMT, DISC_QTY, P_NO, IS_SALEPK, REASON "
        + "from SALE_DIS "
        + "where (1 = 1) "
        + "and SL_KEY in (select SL_KEY from SALE_H where (1 = 1) and (EO_DATE = '')) ";
      db.execSQL(sql);

      db.execSQL("update emisprop set value = '" + date + "' where name = 'LastClearDate' ;");
      db.execSQL("update emisprop set value = '" + time + "' where name = 'LastClearTime' ;");

      db.execSQL("update sale_h set eo_date = '" + date + "',eo_time='" + time + "' where eo_date = ''; ");

      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
      if (db != null) db.close();
    }
  }

  public static void clearNODCLOSE() {
    emisSQLiteWrapper db = emisDb.getInstance();
    try {
      db.beginTransaction();

      db.execSQL("delete from NODCLOSE_SALE_H ");
      db.execSQL("delete from NODCLOSE_SALE_D ");
      db.execSQL("delete from NODCLOSE_SALE_P ");
      db.execSQL("delete from NODCLOSE_SALE_PK ");
      db.execSQL("delete from NODCLOSE_SALE_S ");
      db.execSQL("delete from NODCLOSE_SALE_DIS ");

      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
      if (db != null) db.close();
    }
  }

  public static boolean isExistNextMonthInvoice() {
    boolean result = false;

    Calendar calendar = Calendar.getInstance();
    String[] invoiceMonth;

    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteCursor cursor;

    try {
      calendar.add(Calendar.MONTH, 1);
      emisUtil.formatDateTime("%y%M%D", calendar.getTime());
      invoiceMonth = getInvoiceMonth(emisUtil.formatDateTime("%y%M%D", calendar.getTime()));
      System.out.println("select INV_NO_S from INVOICE where (1=1) and INV_DATE_S = '" + invoiceMonth[0] + "' and INV_DATE_E = '" + invoiceMonth[1] + "' ");

      cursor = db.executeQuery("select INV_NO_S from INVOICE where (1=1) and INV_DATE_S = '" + invoiceMonth[0] + "' and INV_DATE_E = '" + invoiceMonth[1] + "' ");
      if (cursor.getCount() != 0) result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static String[] getNextMonthInvoice() {
    String[] result = new String[2];

    Calendar calendar = Calendar.getInstance();
    String[] invoiceMonth;

    emisSQLiteWrapper db = emisDb.getInstance();
    SQLiteCursor cursor;

    try {
      calendar.add(Calendar.MONTH, 1);
      emisUtil.formatDateTime("%y%M%D", calendar.getTime());
      invoiceMonth = getInvoiceMonth(emisUtil.formatDateTime("%y%M%D", calendar.getTime()));
      System.out.println("select INV_NO_S,INV_NO_E from INVOICE where (1=1) and INV_DATE_S = '" + invoiceMonth[0] + "' and INV_DATE_E = '" + invoiceMonth[1] + "' ");

      cursor = db.executeQuery("select INV_NO_S,INV_NO_E from INVOICE where (1=1) and INV_DATE_S = '" + invoiceMonth[0] + "' and INV_DATE_E = '" + invoiceMonth[1] + "' ");
      if (cursor.getCount() != 0) {
        cursor.move(1);
        result[0] = cursor.getString(cursor.getColumnIndex("INV_NO_S"));
        result[1] = cursor.getString(cursor.getColumnIndex("INV_NO_E"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static boolean isChinese(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
      || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
      || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
      || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
      return true;
    }
    return false;
  }

  // 完整的判斷中文漢字和符號
  public static boolean isChinese(String strName) {
    char[] ch = strName.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      if (isChinese(c)) {
        return true;
      }
    }
    return false;
  }

  public static int getLen(String strName) {
    int len = 0;
    char[] ch = strName.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      if (isChinese(c)) {
        len = len + 2;
      } else {
        len++;
      }
    }
    return len;
  }

  // 只能判斷部分CJK字元（CJK統一漢字）
  public static boolean isChineseByREG(String str) {
    if (str == null) {
      return false;
    }
    Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]");
    return pattern.matcher(str.trim()).find();
  }

  // 只能判斷部分CJK字元（CJK統一漢字）
  public static boolean isChineseByName(String str) {
    if (str == null) {
      return false;
    }
    // 大小寫不同：\\p 表示包含，\\P 表示不包含
    // \\p{Cn} 的意思為 Unicode 中未被定義字元的編碼，\\P{Cn} 就表示 Unicode中已經被定義字元的編碼
    String reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}";
    Pattern pattern = Pattern.compile(reg);
    return pattern.matcher(str.trim()).find();
  }
}
