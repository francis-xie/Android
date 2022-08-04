package com.basic.dbms;

import android.content.Context;
import com.basic.dbms.db.DBService;
import com.basic.dbms.db.IDatabase;
import com.basic.dbms.logs.DBLog;

import java.lang.Class;
import java.lang.String;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>这是DataBaseRepositoryProcessor自动生成的类，用以管理应用SqlLite数据库。</p>
 */
public final class FACEDataBaseRepository {
  private static FACEDataBaseRepository sInstance;

  public static final String DATABASE_NAME = "FACE.db";

  public static final int DATABASE_VERSION = 1;

  private Context mContext;

  private IDatabase mIDatabase;

  private Map<String, DBService> mDBPool = new HashMap<>();

  private FACEDataBaseRepository() {
  }

  public void init(final Context context) {
    mContext = context.getApplicationContext();
  }

  public static FACEDataBaseRepository getInstance() {
    if (sInstance == null) {
        synchronized (FACEDataBaseRepository.class) {
            if (sInstance == null) {
                sInstance = new FACEDataBaseRepository();
            }
        }
    }
    return sInstance;
  }

  public FACEDataBaseRepository setIDatabase(final IDatabase iDatabase) {
    mIDatabase = iDatabase;
    return this;
  }

  public <T> DBService<T> getDataBase(final Class<T> clazz) {
    DBService<T> dbService = null;
    if (mDBPool.containsKey(clazz.getCanonicalName())) {
       dbService = mDBPool.get(clazz.getCanonicalName());
    } else {
       try {
           dbService = new DBService<T>(mContext, clazz, DATABASE_NAME, DATABASE_VERSION, mIDatabase);
       } catch (SQLException e) {
           DBLog.e(e);
       }
       mDBPool.put(clazz.getCanonicalName(), dbService);
    }
    return dbService;
  }
}
