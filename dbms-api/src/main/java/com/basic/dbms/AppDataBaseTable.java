package com.basic.dbms;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>这是DatabaseTableProcessor自动生成的类，用以管理数据库表。</p>
 */
public class AppDataBaseTable {
  private static AppDataBaseTable sInstance;

  private List<String> mTables;

  private AppDataBaseTable() {
    mTables = new ArrayList<>();
    mTables.add("com.basic.code.base.db.entity.SearchRecord");
  }

  public static AppDataBaseTable getInstance() {
    if (sInstance == null) {
        synchronized (AppDataBaseTable.class) {
            if (sInstance == null) {
                sInstance = new AppDataBaseTable();
            }
        }
    }
    return sInstance;
  }

  public static List<String> getTables() {
    return getInstance().mTables;
  }
}
