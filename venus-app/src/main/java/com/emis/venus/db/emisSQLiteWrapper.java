package com.emis.venus.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.emis.venus.util.log4j.LogKit;

import java.util.ArrayList;

/**
 * SQLite教程：https://www.yiibai.com/sqlite
 * Android 数据库：https://www.w3cschool.cn/android/android-dababase.html
 * https://developer.android.google.cn/training/data-storage/sqlite
 * https://developer.android.google.cn/reference/android/database/sqlite/package-summary
 */
public class emisSQLiteWrapper {
  SQLiteDatabase oSQLiteDb_;
  SQLiteStatement oStmt_;
  ArrayList alResource_ = new ArrayList();

  public emisSQLiteWrapper(SQLiteDatabase lite) {
    oSQLiteDb_ = lite;
		/*
		lite.beginTransaction();
		lite.endTransaction();
		lite.delete(String table, String whereClause, String [] whereArgs)
		lite.execSQL(String sql);
		lite.execSQL(String sql, Object [] bindArgs);
		*/
  }

  public void beginTransaction() {
    oSQLiteDb_.beginTransaction();
  }

  public void endTransaction() {
    oSQLiteDb_.endTransaction();
  }

  public void setTransactionSuccessful() {
    oSQLiteDb_.setTransactionSuccessful();
  }

  public int delete(String table, String whereClause, String[] whereArgs) {
    return oSQLiteDb_.delete(table, whereClause, whereArgs);
  }

  public void execSQL(String sql) throws SQLException {
    oSQLiteDb_.execSQL(sql);
  }

  public void execSQL(String sql, Object[] bindArgs) throws SQLException {
    oSQLiteDb_.execSQL(sql, bindArgs);
  }

  public Cursor rawQuery(String sql, String[] selectionArgs) {
    Cursor c = oSQLiteDb_.rawQuery(sql, selectionArgs);
    if (c != null) {
      alResource_.add(c);
    }
    return c;
  }

  public long insert(String table, String nullColumnHack, ContentValues values) {
    return oSQLiteDb_.insert(table, nullColumnHack, values);
  }

  public SQLiteStatement compileStmt(String sSQL) {
    if (oStmt_ != null) {
      oStmt_.clearBindings();
    }
    oStmt_ = oSQLiteDb_.compileStatement(sSQL);
    return oStmt_;
  }

  public SQLiteStatement prepareStmt(String sSQL) {
    return compileStmt(sSQL);
  }

  public int executeQuery() {
    int _iResult = (int) oStmt_.simpleQueryForLong();
    return _iResult;
  }

  public String executeQueryString() {
    String _sValue = oStmt_.simpleQueryForString();
    return _sValue;
  }

  private SQLiteCursor oCursor_;

  public SQLiteCursor executeQuery(String sSQL) {
    oCursor_ = (SQLiteCursor) oSQLiteDb_.rawQuery(sSQL, null);
    return oCursor_;
  }

  public boolean first() {
    return oCursor_.moveToFirst();
  }

  public boolean last() {
    return oCursor_.moveToLast();
  }

  public boolean EOF() {
    return oCursor_.isAfterLast();
  }

  public boolean next() {
    return oCursor_.moveToNext();
  }

  public String getString(String name) throws Exception {
    int index = oCursor_.getColumnIndex(name);
    if (index == -1) throw new Exception("Unable to get column(s) index of:" + name);
    return oCursor_.getString(index);
  }

  public double getDouble(String name) throws Exception {
    int index = oCursor_.getColumnIndex(name);
    if (index == -1) throw new Exception("Unable to get column(d) index of:" + name);
    String s = oCursor_.getString(index);
    try {
      if (s == null) return 0;
      return Double.parseDouble(s);
    } catch (Exception e) {
      return 0;
    }
  }

  public int getInt(Cursor c, String name) throws Exception {
    int index = oCursor_.getColumnIndex(name);
    if (index == -1) throw new Exception("Unable to get column index of:" + name);
    String s = oCursor_.getString(index);
    try {
      if (s == null) return 0;
      return Integer.parseInt(s);
    } catch (Exception e) {
      LogKit.error(e, e);
      return 0;
    }
  }

  public void setString(int iFieldCount, String sValue) {
    oStmt_.bindString(iFieldCount, sValue);
  }

  public void setLong(int iFieldCount, long lValue) {
    oStmt_.bindLong(iFieldCount, lValue);
  }

  public void setDouble(int iFieldCount, double dValue) {
    oStmt_.bindDouble(iFieldCount, dValue);
  }

  public void clearBindings() {
    oStmt_.clearBindings();
  }

  public void close() {
    for (int i = 0; i < alResource_.size(); i++) {
      try {
        Cursor c = (Cursor) alResource_.get(i);
        c.close();
      } catch (Exception e) {
        LogKit.error(e, e);
      }
    }
    alResource_.clear();
  }
}
