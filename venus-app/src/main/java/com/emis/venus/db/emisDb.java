package com.emis.venus.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import com.emis.venus.util.log4j.LogKit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * check table exists....
 * SELECT name FROM sqlite_master WHERE type='table' AND name='table_name';
 *
 * sample:
         emisSQLiteWrapper db = emisDb.getInstance();
        try {
            SQLiteCursor c = (SQLiteCursor) db.rawQuery(sSQL, null);
            Log.d("app", sSQL);
            c.moveToFirst();
            while (c.isAfterLast() == false) {

                emisSuitP s = new emisSuitP(c);
                Log.d("套餐明细项", s.P_NO + s.P_NAME_S + s.GROUP_NO);
                if (!m_Groups.contains(s.GROUP_NO)) {
                    m_Groups.add(s.GROUP_NO);
                }
                add2Hash(s);
                m_allSuitP.add(s);

                c.moveToNext();
            }
            Log.d("emisShitControl套餐總數量:", m_allSuitP.size() + "");

            // 如果某個 group 只有一個商品,那就直接給他 MIN Qty
            setDefaultQty();
        } finally {
            db.close();
        }
 */

/**
 * https://developer.android.google.cn/reference/android/database/sqlite/SQLiteOpenHelper
 */
public class emisDb extends SQLiteOpenHelper {

  // MY_DB_NAME要用完整路徑
  protected static final String MY_DB_NAME = "/data/data/com.emis.venus/databases/venus.db";
  //資料庫版本關係到App更新時，資料庫是否要調用onUpgrade()
  protected static final int VERSION = 1;//資料庫版本

  protected static emisDb g_DB = null;
  protected SQLiteDatabase m_db = null;

  public static emisDb createInstance(Context context) {
    if (g_DB == null) {
      g_DB = new emisDb(context, MY_DB_NAME);
      g_DB.init();
    }
    return g_DB;
  }

  /*public static SQLiteDatabase getInstance() {
    if (g_DB != null) {
      return g_DB.m_db;
    } else {
      return null;
    }
  }*/
  public static emisSQLiteWrapper getInstance() {
    if (g_DB != null) {
      return new emisSQLiteWrapper(g_DB.m_db);
    } else {
      return null;
    }
  }

  private emisDb(Context context, String name) {
    this(context, name, null, VERSION);
  }

  private emisDb(Context context, String name, int version) {
    this(context, name, null, version);
  }

  private emisDb(Context context, String name, CursorFactory factory, int version) {
    super(context, name, factory, version);
  }

  protected void init() {
    // 创建和/或打开一个用于读写的数据库。
    m_db = getWritableDatabase();
  }

  // 輔助類建立時運行該方法
  @Override
  public void onCreate(SQLiteDatabase db) {
    //在第一次创建数据库时调用。
    Log.d("venus", "emisDb.onCreate");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //当数据库需要升级时调用。
    Log.d("venus", "emisDb.onUpgrade:" + oldVersion + "=>" + newVersion);
    // oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
    //db.execSQL("DROP TABLE IF EXISTS newMemorandum"); // 刪除舊有的資料表
    //onCreate(db);
    /*switch (newVersion) {
      case 4:
        db.execSQL("alter table sale_h add column EO_TIME nvarchar(6) default ''");
      case 3:
        if (oldVersion >= 3) {
          break;
        }
        db.execSQL("alter table sale_h add column D_NO nvarchar(10)");
      case 2:
        if (oldVersion >= 2) {
          break;
        }
        db.execSQL("alter table sale_h add column OD_NO nvarchar(20)");
        db.execSQL("alter table NODCLOSE_SALE_H add column OD_NO nvarchar(20)");
    }*/
  }

  @Override
  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //当数据库需要降级时调用。
    Log.d("venus", "emisDb.onDowngrade:" + oldVersion + "=>" + newVersion);
  }

  @Override
  public void onOpen(SQLiteDatabase db) {
    super.onOpen(db); //当数据库被打开时调用。
    // 每次成功打開數據庫後首先被執行
    Log.d("venus", "emisDb.onOpen:" + db);
  }

  @Override
  public synchronized void close() {
    super.close(); //关闭任何打开的数据库对象。
    if (m_db != null) m_db.close();
    Log.d("venus", "emisDb.close:");
  }

  /**
   * Check if the database exist and can be read.
   *
   * @return true if it exists and can be read, false if it doesn't
   */
  public static boolean exists() {
    // SQLiteDatabase checkDB = null;

    boolean result = false;
    emisSQLiteWrapper db = emisDb.getInstance();
    try {
      // checkDB = SQLiteDatabase.openDatabase(MY_DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
      // _isExists = (checkDB.getVersion() > 0);
      // checkDB.close();

      db.prepareStmt("select count(1) CNT from sqlite_master where type in ('table','view')");
      // int _iTableCount = db.executeQuery();
      result = (db.executeQuery() > 1);
    } catch (SQLiteException e) {
      // database doesn't exist yet.
      Log.e("emisDb.exists()", "Selected file could not be opened as DB.");
      result = false;
    }
    return result;
  }

  public static void exportDB(final String targetFile) throws IOException {
    File currentDB = null;
    File backupDB = null;

    FileChannel source = null;
    FileChannel destination = null;
    try {
      currentDB = new File(MY_DB_NAME);
      backupDB = new File(targetFile);
      source = new FileInputStream(currentDB).getChannel();
      destination = new FileOutputStream(backupDB).getChannel();
      destination.transferFrom(source, 0, source.size());
    } finally {
      source.close();
      destination.close();
    }
  }

  public static String exportDB() {
    String result = "";

    File sd = Environment.getExternalStorageDirectory();
    // File data = Environment.getDataDirectory();

    String logPath = sd + "/log";  // Log目錄
    String fileName = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + ".db";  // like 20160112.db
    try {
      // 如果沒有Log資料夾則新增
      if (!(new File(logPath)).exists()) (new File(logPath)).mkdir();

      // 如果沒有Log年月資料夾則新增
      if (!(new File(logPath + "/" + fileName.substring(0, 6))).exists())
        (new File(logPath + "/" + fileName.substring(0, 6))).mkdir();

      result = logPath + "/" + fileName.substring(0, 6) + "/" + fileName;

      exportDB(result);
    } catch (Exception e) {
      LogKit.error(e, e);
    }
    return result;
  }
}
