package com.emis.venus.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import com.emis.venus.common.emisKeeper;
import com.emis.venus.db.emisDb;
import com.emis.venus.db.emisSQLiteWrapper;
import com.emis.venus.logic.Employ;
import com.emis.venus.util.emisUtil;
import com.emis.venus.util.log4j.LogKit;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * 数据库操作类<br>
 * 表资料的新增、删除、修改、更新<br>
 * 执行sql语句<br>
 * 查询sql语句<br>
 */
public class Entity {
  /**
   * 表名
   */
  String m_sTableName;
  /**
   * 数据存储集合（里面放Object[]）
   */
  Vector<Object[]> m_rows;
  /**
   * 列名所对应的index.<br>
   * 用于m_columnType取对应的类型<br>
   * 及Object[]取对应的数据
   */
  HashMap<String, Integer> m_columnIndexMap;
  /**
   * 列名(下标从1-m_columnCount)
   */
  String[] m_columnName;
  /**
   * 主键(下标从0开始)
   */
  String[] m_primaryKey;
  /**
   * 数据笔数
   */
  int m_rowCount;
  /**
   * 当前数据所在行
   */
  int m_currRow;// from 0 ~ m_rowCount-1
  /**
   * 实际列的笔数(Object[]的长度)
   */
  int m_columnCount_t;
  /**
   * 列的笔数(Object[]的长度)
   */
  int m_columnCount;
  /**
   * 列对应的数据类型java.sql.Types(下标从1-m_columnCount)
   */
  int[] m_columnType;
  /**
   * batch的最大笔数
   */
  int iMaxCommitCount = 200;

  /**
   * 空的构造方法<br>
   * 适用于要直接使用sql进行查询或更新
   */
  public Entity() {
    this.init();
  }

  /**
   * 传入表名的构造方法
   *
   * @param sTableName
   * @throws Exception
   */
  public Entity(String sTableName) {
    try {
      this.init();
      this.initTable(emisUtil.parseString(sTableName).toUpperCase());
    } catch (Exception e) {
      LogKit.error(e, e);
    }
  }

  /**
   * 根据 hashMap里的键值对查询，传入null,查询所有
   *
   * @param PK_Value
   * @throws Exception
   */
  public boolean load(HashMap<?, ?> PK_Value) throws Exception {
    this.cleanData();
    String sSQL = "select * from " + this.getM_sTableName();

    // 组where条件
    boolean hadCondition = false;
    Entry<?, ?>[] entry = null;
    String[] selectionArgs = null;
    if (PK_Value != null && !PK_Value.isEmpty()) {
      sSQL += " where ";
      entry = (Entry[]) PK_Value.entrySet().toArray(new Entry[PK_Value.entrySet().size()]);
      boolean first = true;
      selectionArgs = new String[entry.length];
      for (int i = 0; i < entry.length; i++) {
        Entry<?, ?> e = entry[i];
        Object key = e.getKey();
        if (first) {
          sSQL += key + "=?";
          first = false;
        } else {
          sSQL += " and " + key + "=?";
        }
        selectionArgs[i] = emisUtil.parseString(e.getValue());
      }
      hadCondition = true;
    }

    emisSQLiteWrapper db = null;
    try {
      db = emisDb.getInstance();
      doAuditSQL(sSQL, selectionArgs);

      SQLiteCursor cursor = (SQLiteCursor) db.rawQuery(sSQL, selectionArgs);
      try {
        initRS(cursor);
        if (this.m_rowCount > 0)
          return true;
        else
          return false;
      } finally {
        cursor.close();
        cursor = null;
      }
    } finally {
      if (db != null) {
        db.close();
        db = null;
      }
      entry = null;
      sSQL = null;
      selectionArgs = null;
    }
  }

  /**
   * 用于直接组sql查询.
   *
   * @param sSQL as "select * from part where a=''"
   * @throws Exception
   */
  public boolean loadBySql(emisSQLiteWrapper outSideDb, String sSQL) throws Exception {
    this.cleanData();

    emisSQLiteWrapper db = null;
    try {
      if (outSideDb == null) {
        db = emisDb.getInstance();
      } else {
        db = outSideDb;
      }
      doAuditSQL(sSQL, null);

      SQLiteCursor cursor = (SQLiteCursor) db.rawQuery(sSQL, null);
      try {
        initSqlRS(cursor);
        if (this.m_rowCount > 0)
          return true;
        else
          return false;
      } finally {
        cursor.close();
        cursor = null;
      }
    } finally {
      if (db != null && outSideDb == null) {
        db.close();
        db = null;
      }
    }
  }

  /**
   * 用于直接组sql查询.
   *
   * @param sSQL as "Select * from part where a=''"
   * @throws Exception
   */
  public boolean loadBySql(String sSQL) throws Exception {
    this.cleanData();

    emisSQLiteWrapper db = null;
    try {
      db = emisDb.getInstance();
      doAuditSQL(sSQL, null);

      SQLiteCursor cursor = (SQLiteCursor) db.rawQuery(sSQL, null);
      try {
        initSqlRS(cursor);
        if (this.m_rowCount > 0)
          return true;
        else
          return false;
      } finally {
        cursor.close();
        cursor = null;
      }
    } finally {
      if (db != null) {
        db.close();
        db = null;
      }
    }
  }

  /**
   * 传入 SQL 及对应字段值查询
   *
   * @param sSQL    as "select 字段 from Table where A=? and B=? "
   * @param sValues 对应的值
   * @throws Exception
   */
  public boolean loadBySql(String sSQL, String[] sValues) throws Exception {
    this.cleanData();

    emisSQLiteWrapper db = null;
    try {
      db = emisDb.getInstance();
      doAuditSQL(sSQL, sValues);

      SQLiteCursor cursor = (SQLiteCursor) db.rawQuery(sSQL, sValues);
      try {
        initSqlRS(cursor);
        if (this.m_rowCount > 0)
          return true;
        else
          return false;
      } finally {
        cursor.close();
        cursor = null;
      }
    } finally {
      if (db != null) {
        db.close();
        db = null;
      }
    }
  }

  /**
   * 更新当前m_currRow的数据,适应于单表 <br>
   * emisDb outSideDb参数给null,将重新new emisDb<br>
   *
   * @param outSideDb 传入值，可以保存事务一致
   * @return
   * @throws Exception
   */
  public boolean insert(emisSQLiteWrapper outSideDb) throws Exception {
    if (m_rowCount <= 0 || m_currRow < 0 || m_currRow >= m_rowCount)
      return false; // 无资料

    emisSQLiteWrapper db = null;
    try {
      if (outSideDb == null) {
        db = emisDb.getInstance();
      } else {
        db = outSideDb;
      }

      String sSql = "insert into " + this.getM_sTableName() + "(";
      for (int i = 0; i < m_columnCount; i++) {
        if (i == m_columnCount - 1) {
          sSql += (m_columnName[i] + ") values( ");
        } else {
          sSql += (m_columnName[i] + ",");
        }
      }
      for (int i = 0; i < m_columnCount; i++) {
        if (i > 0) {
          sSql += ",";
        }
        sSql += "?";
        if (i == m_columnCount - 1) {
          sSql += ")";
        }
      }
      SQLiteStatement stmt = db.compileStmt(sSql);
      try {
        for (int i = 0; i < m_columnCount; i++) {
          setPreStm(stmt, i + 1, m_columnType[i], m_columnName[i]);
        }
        // Log SQL
        doAuditSQL(sSql);
        long i = stmt.executeInsert();
        if (i > 0) {
          return true;
        } else {
          return false;
        }
      } finally {
        stmt.close();
        stmt = null;
      }
    } finally {
      if (db != null && outSideDb == null) {
        db.close();
        db = null;
      }
    }
  }

  /**
   * 插入Entity里所有的数据,适应于单表 <br>
   * emisSQLiteWrapper outSideDb不能为null且必须手动提交 <br>
   *
   * @param outSideDb
   * @return
   * @throws Exception
   */
  public boolean insertAll(emisSQLiteWrapper outSideDb) throws Exception {
    boolean result = false;
    if (m_rowCount <= 0) {// 无资料
      return result;
    }
    if (m_columnName == null || m_columnCount < 1) {
      return result;
    }
    if (outSideDb == null) {
      return result;
    }

    String sSql = "insert into " + this.getM_sTableName() + "(";
    for (int i = 0; i < m_columnCount; i++) {
      if (i == m_columnCount - 1) {
        sSql += (m_columnName[i] + ") values( ");
      } else {
        sSql += (m_columnName[i] + ",");
      }
    }

    for (int i = 0; i < m_columnCount; i++) {
      if (i > 0) {
        sSql += ",";
      }
      sSql += "?";
      if (i == m_columnCount - 1) {
        sSql += ")";
      }
    }
    SQLiteStatement stmt = outSideDb.compileStmt(sSql);
    try {
      //outSideDb.beginTransaction();
      this.first();
      //boolean isHaveExecute = true;
      do {
        /*if (this.getM_currRow() >= iMaxCommitCount) {
          // 批量执行预定义SQL
          if (stmt.executeInsert() <= 0) {
            return false;
          }
          isHaveExecute = true;
        }*/
        stmt.clearBindings();
        for (int i = 0; i < m_columnCount; i++) {
          setPreStm(stmt, i + 1, m_columnType[i], m_columnName[i]);
        }
        // Log SQL
        doAuditSQL(sSql);
        if (stmt.executeInsert() <= 0) {
          return false;
        }
        // 添加一次预定义参数
        //stmt.addBatch();
        //isHaveExecute = false;
      } while (this.next());
      // 批量执行预定义SQL
      /*if (!isHaveExecute) {
        if (stmt.executeInsert() <= 0) {
          return false;
        }
      }*/
      //outSideDb.setTransactionSuccessful();
      result = true;
    } finally {
      //outSideDb.endTransaction();
      stmt.close();
      stmt = null;
    }
    return result;
  }

  /**
   * 插入Entity里所有的数据,适应于单表 <br>
   * emisSQLiteWrapper outSideDb不能为null且必须手动提交 <br>
   *
   * @param outSideDb
   * @return
   * @throws Exception
   */
  public boolean insertAllNoThrows(emisSQLiteWrapper outSideDb) {
    boolean result = false;
    if (m_rowCount <= 0) {// 无资料
      return result;
    }
    if (m_columnName == null || m_columnCount < 1) {
      return result;
    }
    if (outSideDb == null) {
      return result;
    }

    String sSql = "insert into " + this.getM_sTableName() + "(";
    for (int i = 0; i < m_columnCount; i++) {
      if (i == m_columnCount - 1) {
        sSql += (m_columnName[i] + ") values( ");
      } else {
        sSql += (m_columnName[i] + ",");
      }
    }

    for (int i = 0; i < m_columnCount; i++) {
      if (i > 0) {
        sSql += ",";
      }
      sSql += "?";
      if (i == m_columnCount - 1) {
        sSql += ")";
      }
    }
    SQLiteStatement stmt = null;
    int[] batch = null;
    try {
      stmt = outSideDb.compileStmt(sSql);
      //outSideDb.beginTransaction();
      this.first();
      boolean isHaveExecute = true;
      do {
        /*if (this.getM_currRow() >= iMaxCommitCount) {
          try {
            batch = stmt.executeBatch();
          } catch (Exception e) {
            LogKit.error("insertAll error:", e);
          }
          // 批量执行预定义SQL
          if (!checkBatch(batch)) {
            return false;
          }
          isHaveExecute = true;
        }*/
        stmt.clearBindings();
        for (int i = 0; i < m_columnCount; i++) {
          setPreStm(stmt, i + 1, m_columnType[i], m_columnName[i]);
        }
        stmt.executeInsert();
        // 添加一次预定义参数
        /*try {
          stmt.addBatch();
        } catch (Exception e) {
          LogKit.error("insertAll error:", e);
        }
        isHaveExecute = false;*/
      } while (this.next());
      // 批量执行预定义SQL
      /*if (!isHaveExecute) {
        try {
          batch = stmt.executeBatch();
          LogKit.info(batch);
        } catch (Exception e) {
          LogKit.error("insertAll error:", e);
        }
        // 批量执行预定义SQL
        if (!checkBatch(batch)) {
          return false;
        }
      }*/
      //outSideDb.setTransactionSuccessful();
      result = true;
    } catch (Exception e) {
      LogKit.error("insertAll error:", e);
    } finally {
      //outSideDb.endTransaction();
      if (stmt != null) {
        try {
          stmt.close();
          stmt = null;
        } catch (Exception e) {
          LogKit.error("close stmt error", e);
        }
      }
    }
    return result;
  }

  /**
   * 插入Entity里指定index的数据,适应于单表 <br>
   * 目前用于updataAll未更新到的数据，再做一次insert <br>
   * emisSQLiteWrapper outSideDb不能为null且必须手动提交 <br>
   *
   * @param outSideDb
   * @param sIndexs   :指定index的资料，下标用"-"号隔开，如：-1-2-9-10-
   * @return
   * @throws Exception
   */
  private boolean insert(emisSQLiteWrapper outSideDb, String sIndexs) throws Exception {
    boolean result = false;
    if (m_rowCount <= 0) {// 无资料
      return result;
    }
    if (outSideDb == null) {
      return result;
    }

    String sSql = "insert into " + this.getM_sTableName() + "(";
    for (int i = 0; i < m_columnCount; i++) {
      if (i == m_columnCount - 1) {
        sSql += (m_columnName[i] + ") values( ");
      } else {
        sSql += (m_columnName[i] + ",");
      }
    }
    for (int i = 0; i < m_columnCount; i++) {
      if (i > 0) {
        sSql += ",";
      }
      sSql += "?";
      if (i == m_columnCount - 1) {
        sSql += ")";
      }
    }
    SQLiteStatement stmt = outSideDb.compileStmt(sSql);
    try {
      //outSideDb.beginTransaction();
      this.first();
      boolean isHaveExecute = true;
      do {
        // 下标是否存在需要insert的字符串里
        if (sIndexs.indexOf("-" + this.getM_currRow() + "-") < 0) {
          continue;
        }
        /*if (this.getM_currRow() >= iMaxCommitCount) {
          // 批量执行预定义SQL
          if (!checkBatch(stmt.executeBatch())) {
            return false;
          }
          isHaveExecute = true;
        }*/
        stmt.clearBindings();
        for (int i = 0; i < m_columnCount; i++) {
          setPreStm(stmt, i + 1, m_columnType[i], m_columnName[i]);
        }
        stmt.executeInsert();
        // 添加一次预定义参数
        /*stmt.addBatch();
        isHaveExecute = false;*/
      } while (this.next());
      // 批量执行预定义SQL
      /*if (!isHaveExecute) {
        if (!checkBatch(stmt.executeBatch())) {
          return false;
        }
      }*/
      //outSideDb.setTransactionSuccessful();
      result = true;
    } finally {
      //outSideDb.endTransaction();
      stmt.close();
      stmt = null;
    }
    return result;
  }

  /**
   * 检查executeBatch后是否都执行正确
   *
   * @param batch
   * @return
   */
  private boolean checkBatch(int[] batch) {
    if (batch == null) {
      return false;
    }
    for (int i = 0; i < batch.length; i++) {
      if (emisUtil.parseInt(batch[i]) <= 0) {
        this.move(i);
        return false;
      }
    }
    return true;
  }

  /**
   * 更新当前m_currRow的数据,适应于单表且有主键 <br>
   * emisSQLiteWrapper outSideDb参数给null,将重新new emisDb<br>
   *
   * @param outSideDb 传入值，可以保存事务一致
   * @return
   * @throws Exception
   */
  public boolean update(emisSQLiteWrapper outSideDb) throws Exception {
    if (m_rowCount <= 0 || m_currRow < 0 || m_currRow >= m_rowCount)
      return false; // 无资料
    if (m_primaryKey == null || m_primaryKey[0] == null) {// 无主键
      return false;
    }

    emisSQLiteWrapper db = null;
    try {
      if (outSideDb == null) {
        db = emisDb.getInstance();
      } else {
        db = outSideDb;
      }

      String sSql = "update " + this.getM_sTableName() + " set ";
      for (int i = 0; i < m_columnCount; i++) {
        if (i > 0) {
          sSql += ",";
        }
        sSql += (m_columnName[i] + "=? ");
      }

      int primaryKeyLen = this.m_primaryKey.length;// 主键数组的实际长度
      sSql += " where ";
      for (int i = 0; i < primaryKeyLen; i++) {
        // 由于m_primaryKey长度默认给了5个。所以会有后面几项为null
        if (m_primaryKey[i] == null || "".equals(m_primaryKey[i])) {
          primaryKeyLen = i;
          break;
        }
        if (i > 0) {
          sSql += " AND ";
        }
        sSql += (m_primaryKey[i] + "=?");
      }
      SQLiteStatement stmt = db.compileStmt(sSql);
      try {
        int index = 1;
        for (int i = 0; i < m_columnCount; i++) {
          setPreStm(stmt, index, m_columnType[i], m_columnName[i]);
          index++;
        }
        for (int i = 0; i < primaryKeyLen; i++) {
          for (int j = 0; j < m_columnCount; j++) {
            if (m_columnName[j].equals(m_primaryKey[i])) {
              setPreStm(stmt, index, m_columnType[j], m_columnName[j]);
              index++;
              break;
            }
          }
        }
        // Log SQL
        doAuditSQL(sSql);
        int i = stmt.executeUpdateDelete();
        if (i > 0)
          return true;
        else
          return false;
      } finally {
        stmt.close();
        stmt = null;
      }
    } finally {
      if (db != null && outSideDb == null) {
        db.close();
        db = null;
      }
    }
  }

  /**
   * 更新所有Entity里的数据,适应于单表且有主键 <br>
   * emisSQLiteWrapper outSideDb不能为null且必须手动提交 <br>
   *
   * @param outSideDb
   * @return
   * @throws Exception
   */
  public boolean updateAll(emisSQLiteWrapper outSideDb) throws Exception {
    return updateAllWithCond(outSideDb, null);
  }

  /**
   * 新增条件，更新所有Entity里的数据,适应于单表且有主键 <br>
   * emisSQLiteWrapper outSideDb不能为null且必须手动提交 <br>
   *
   * @param outSideDb
   * @param extCond   扩展的条件，不为空时只做update、没有更新到的下标也不会去做insert
   * @return
   * @throws Exception
   */
  public boolean updateAllWithCond(emisSQLiteWrapper outSideDb, String extCond) throws Exception {
    boolean result = false;
    if (m_rowCount <= 0) {// 无资料
      return result;
    }
    if (m_primaryKey == null || m_primaryKey[0] == null) {// 无主键
      return result;
    }
    if (outSideDb == null) {
      return result;
    }

    String sSql = "update " + this.getM_sTableName() + " set ";
    for (int i = 0; i < m_columnCount; i++) {
      if (i > 0) {
        sSql += ",";
      }
      sSql += (m_columnName[i] + "=? ");
    }
    int primaryKeyLen = this.m_primaryKey.length;// 主键数组的实际长度
    sSql += " where ";
    for (int i = 0; i < primaryKeyLen; i++) {
      // 由于m_primaryKey长度默认给了5个。所以会有后面几项为null
      if (m_primaryKey[i] == null || "".equals(m_primaryKey[i])) {
        primaryKeyLen = i;
        break;
      }
      if (i > 0) {
        sSql += " AND ";
      }
      sSql += (m_primaryKey[i] + "=?");
    }
    boolean isHadCond = false;
    if (!"".equals(emisUtil.parseString(extCond))) {
      sSql += " AND " + extCond;
      isHadCond = true;
    } else {
      isHadCond = false;
    }
    SQLiteStatement stmt = outSideDb.compileStmt(sSql);
    try {
      //outSideDb.beginTransaction();
      int index = 1;
      this.first();
      //boolean isHaveExecute = true;
      //int[] batch = null;
      //StringBuffer errorIndexSbf = null;// 没有更新到的下标，需要做insert。(下标用"-"号隔开，如：-1-2-9-10-)
      //int iTotalCount = 0, iCount = 0;
      do {
        /*batch = null;
        if (iCount >= iMaxCommitCount) {
          // 批量执行预定义SQL
          batch = stmt.executeBatch();
          isHaveExecute = true;
          for (int i = 0; i < batch.length; i++) {
            if (!isHadCond && batch[i] < 1) {// 没有更新到的需要做insert。
              int errorIndex = iTotalCount + i;
              // (下标用"-"号隔开，如：-1-2-9-10-)
              if (errorIndexSbf == null) {
                errorIndexSbf = new StringBuffer("-"
                  + errorIndex + "-");
              } else {
                errorIndexSbf.append(errorIndex + "-");
              }
            }
          }
          iTotalCount += iCount;
          iCount = 0;
        }*/
        index = 1;
        stmt.clearBindings();
        for (int i = 0; i < m_columnCount; i++) {
          setPreStm(stmt, index, m_columnType[i], m_columnName[i]);
          index++;
        }
        for (int i = 0; i < primaryKeyLen; i++) {
          for (int j = 0; j < m_columnCount; j++) {
            if (m_columnName[j].equals(m_primaryKey[i])) {
              setPreStm(stmt, index, m_columnType[j], m_columnName[j]);
              index++;
              break;
            }
          }
        }
        // Log SQL
        doAuditSQL(sSql);
        result = stmt.executeUpdateDelete() > 0;
        if (!result) {
          result = insert(outSideDb);
        }
        if (!result) {
          break;
        }
        // 添加一次预定义参数
        /*stmt.addBatch();
        isHaveExecute = false;
        iCount++;*/
      } while (this.next());
      // 批量执行预定义SQL
      /*if (!isHaveExecute) {
        batch = stmt.executeBatch();
        for (int i = 0; i < batch.length; i++) {
          if (!isHadCond && batch[i] < 1) {// 没有更新到的需要做insert。
            int errorIndex = iTotalCount + i;
            getRowObjectToString(errorIndex);
            // (下标用"-"号隔开，如：-1-2-9-10-)
            if (errorIndexSbf == null) {
              errorIndexSbf = new StringBuffer("-" + errorIndex
                + "-");
            } else {
              errorIndexSbf.append(errorIndex + "-");
            }
          }
        }
      }*/
      //outSideDb.setTransactionSuccessful();
      /*if (!isHadCond && errorIndexSbf != null) {
        result = insert(outSideDb, errorIndexSbf.toString());
      } else {
        result = true;
      }*/
    } finally {
      //outSideDb.endTransaction();
      stmt.close();
      stmt = null;
    }
    return result;
  }

  /**
   * 新增条件，更新所有Entity里的数据,适应于单表且有主键 <br>
   * emisSQLiteWrapper outSideDb不能为null且必须手动提交 <br>
   * 用的时候请谨慎用updateAllWithCond还是updateAllWithCond2
   *
   * @param outSideDb
   * @param extCond   扩展的条件，不为空时没有更新到的下标会去做insert
   * @return
   * @throws Exception
   */
  public boolean updateAllWithCond2(emisSQLiteWrapper outSideDb, String extCond) throws Exception {
    boolean result = false;
    if (m_rowCount <= 0) {// 无资料
      return result;
    }
    if (m_primaryKey == null || m_primaryKey[0] == null) {// 无主键
      return result;
    }
    if (outSideDb == null) {
      return result;
    }

    String sSql = "update " + this.getM_sTableName() + " set ";
    for (int i = 0; i < m_columnCount; i++) {
      if (i > 0) {
        sSql += ",";
      }
      sSql += (m_columnName[i] + "=? ");
    }
    int primaryKeyLen = this.m_primaryKey.length;// 主键数组的实际长度
    sSql += " where ";
    for (int i = 0; i < primaryKeyLen; i++) {
      // 由于m_primaryKey长度默认给了5个。所以会有后面几项为null
      if (m_primaryKey[i] == null || "".equals(m_primaryKey[i])) {
        primaryKeyLen = i;
        break;
      }
      if (i > 0) {
        sSql += " AND ";
      }
      sSql += (m_primaryKey[i] + "=?");
    }
    if (!"".equals(emisUtil.parseString(extCond))) {
      sSql += " AND " + extCond;
    }
    SQLiteStatement stmt = outSideDb.compileStmt(sSql);
    try {
      outSideDb.beginTransaction();
      int index = 1;
      this.first();
      //boolean isHaveExecute = true;
      //int[] batch = null;
      //StringBuffer errorIndexSbf = null;// 没有更新到的下标，需要做insert。(下标用"-"号隔开，如：-1-2-9-10-)
      //int iTotalCount = 0, iCount = 0;
      do {
        /*batch = null;
        if (iCount >= iMaxCommitCount) {
          // 批量执行预定义SQL
          batch = stmt.executeBatch();
          isHaveExecute = true;
          for (int i = 0; i < batch.length; i++) {
            if (batch[i] < 1) {// 没有更新到的需要做insert。
              int errorIndex = iTotalCount + i;
              // (下标用"-"号隔开，如：-1-2-9-10-)
              if (errorIndexSbf == null) {
                errorIndexSbf = new StringBuffer("-"
                  + errorIndex + "-");
              } else {
                errorIndexSbf.append(errorIndex + "-");
              }
            }
          }
          iTotalCount += iCount;
          iCount = 0;
        }*/
        index = 1;
        stmt.clearBindings();
        for (int i = 0; i < m_columnCount; i++) {
          setPreStm(stmt, index, m_columnType[i], m_columnName[i]);
          index++;
        }
        for (int i = 0; i < primaryKeyLen; i++) {
          for (int j = 0; j < m_columnCount; j++) {
            if (m_columnName[j].equals(m_primaryKey[i])) {
              setPreStm(stmt, index, m_columnType[j], m_columnName[j]);
              index++;
              break;
            }
          }
        }
        // Log SQL
        doAuditSQL(sSql);
        result = stmt.executeUpdateDelete() > 0;
        if (!result) {
          result = insert(outSideDb);
        }
        if (!result) {
          break;
        }
        // 添加一次预定义参数
        /*stmt.addBatch();
        isHaveExecute = false;
        iCount++;*/
      } while (this.next());
      // 批量执行预定义SQL
      /*if (!isHaveExecute) {
        batch = stmt.executeBatch();
        for (int i = 0; i < batch.length; i++) {
          if (batch[i] < 1) {// 没有更新到的需要做insert。
            int errorIndex = iTotalCount + i;
            getRowObjectToString(errorIndex);
            // (下标用"-"号隔开，如：-1-2-9-10-)
            if (errorIndexSbf == null) {
              errorIndexSbf = new StringBuffer("-" + errorIndex
                + "-");
            } else {
              errorIndexSbf.append(errorIndex + "-");
            }
          }
        }
      }*/
      //outSideDb.setTransactionSuccessful();
      /*if (errorIndexSbf != null) {
        result = insert(outSideDb, errorIndexSbf.toString());
      } else {
        result = true;
      }*/
    } finally {
      //outSideDb.endTransaction();
      stmt.close();
      stmt = null;
    }
    return result;
  }

  /**
   * 直接执行SQL（可以insert、update和delete）
   *
   * @param outSideDb
   * @return
   * @throws Exception
   */
  public boolean executeUpdate(emisSQLiteWrapper outSideDb, String sSql) throws Exception {
    emisSQLiteWrapper db = null;
    try {
      if (outSideDb == null) {
        db = emisDb.getInstance();
      } else {
        db = outSideDb;
      }

      doAuditSQL(sSql, null);
      SQLiteStatement stmt = db.compileStmt(sSql);
      try {
        String sSqlUpper = emisUtil.parseString(sSql).trim().toUpperCase();
        long i = 0;
        if ((sSqlUpper.startsWith("DELETE")) || (sSqlUpper.startsWith("UPDATE"))) {
          i = stmt.executeUpdateDelete();
        } else if (sSqlUpper.startsWith("INSERT")) {
          i = stmt.executeInsert();
        } else {
          stmt.execute();
          i = 1;
        }
        if (i > 0)
          return true;
        else
          return false;
      } finally {
        stmt.close();
        stmt = null;
      }
    } finally {
      if (db != null && outSideDb == null) {
        db.close();
        db = null;
      }
    }
  }

  /**
   * 删除当前Entity的数据,适应于单表且有主键 <br>
   * emisSQLiteWrapper outSideDb不能为null且必须手动提交 <br>
   *
   * @param outSideDb
   * @return
   * @throws Exception
   */
  public boolean deleteAll(emisSQLiteWrapper outSideDb) throws Exception {
    boolean result = false;
    if (m_rowCount <= 0) {// 无资料
      return result;
    }
    if (m_primaryKey == null || m_primaryKey[0] == null) {// 无主键
      return result;
    }
    if (outSideDb == null) {
      return result;
    }

    String sSql = "delete from " + this.getM_sTableName() + " ";
    sSql += " where ";
    int primaryKeyLen = this.m_primaryKey.length;// 主键数组的实际长度
    for (int i = 0; i < primaryKeyLen; i++) {
      if (m_primaryKey[i] == null || "".equals(m_primaryKey[i])) {
        primaryKeyLen = i;
        break;
      }
      if (i > 0) {
        sSql += " AND ";
      }
      sSql += (m_primaryKey[i] + "=?");
    }
    SQLiteStatement stmt = outSideDb.compileStmt(sSql);
    try {
      //outSideDb.beginTransaction();
      this.first();
      //boolean isHaveExecute = true;
      int index = 1;
      do {
        /*if (this.getM_currRow() >= iMaxCommitCount) {
          // 批量执行预定义SQL
          if (!checkBatch(stmt.executeBatch())) {
            return false;
          }
          isHaveExecute = true;
        }*/
        stmt.clearBindings();
        index = 1;
        for (int i = 0; i < primaryKeyLen; i++) {
          for (int j = 0; j < m_columnCount; j++) {
            if (m_columnName[j].equals(m_primaryKey[i])) {
              setPreStm(stmt, index, m_columnType[j], m_columnName[j]);
              index++;
              break;
            }
          }
        }
        // Log SQL
        doAuditSQL(sSql);
        result = stmt.executeUpdateDelete() > 0;
        if (!result)
          break;
        // 添加一次预定义参数
        /*stmt.addBatch();
        isHaveExecute = false;*/
      } while (this.next());
      // 批量执行预定义SQL
      /*if (!isHaveExecute) {
        if (!checkBatch(stmt.executeBatch())) {
          return false;
        }
      }*/
      //outSideDb.setTransactionSuccessful();
      //result = true;
    } finally {
      //outSideDb.endTransaction();
      stmt.close();
      stmt = null;
    }
    return result;
  }

  /**
   * 删除当前表的所有资料：delete from tableName
   *
   * @param outSideDb
   * @return
   * @throws Exception
   */
  public boolean executeDelete(emisSQLiteWrapper outSideDb) throws Exception {
    emisSQLiteWrapper db = null;
    try {
      if (outSideDb == null) {
        db = emisDb.getInstance();
      } else {
        db = outSideDb;
      }
      return this.executeUpdate(db, "delete from " + this.getM_sTableName());
    } finally {
      if (db != null && outSideDb == null) {
        db.close();
        db = null;
      }
    }
  }

  /**
   * 根据SL_KEY删除当前表的所有资料：delete from tableName where SL_KEY=''<br>
   * 用于sale_h，sale_d,Sale_dis,Sale_dis_log的删除<br>
   * 记得不能将entity清空，需要做insertAll
   *
   * @param outSideDb
   * @return
   * @throws Exception
   */
  public boolean executeDeleteBySL_KEY(emisSQLiteWrapper outSideDb, String SL_KEY) throws Exception {
    if ("".equals(emisUtil.parseString(SL_KEY))) {
      return true;
    }

    emisSQLiteWrapper db = null;
    try {
      if (outSideDb == null) {
        db = emisDb.getInstance();
      } else {
        db = outSideDb;
      }
      return this.executeUpdate(db, "delete from " + this.getM_sTableName() + " where SL_KEY='" + SL_KEY + "'");
    } finally {
      if (db != null && outSideDb == null) {
        db.close();
        db = null;
      }
    }
  }

  /**
   * 删除当前m_currRow的数据 <br>
   * emisSQLiteWrapper outSideDb参数给null,将重新new emisDb<br>
   *
   * @param outSideDb 传入值，可以保存事务一致
   * @return
   * @throws Exception
   */
  public boolean delete(emisSQLiteWrapper outSideDb) throws Exception {
    if (m_rowCount <= 0 || m_currRow < 0 || m_currRow >= m_rowCount) {// 无资料
      return false;
    }
    if (m_primaryKey == null || m_primaryKey[0] == null) {// 无主键
      return false;
    }

    emisSQLiteWrapper db = null;
    try {
      if (outSideDb == null) {
        db = emisDb.getInstance();
      } else {
        db = outSideDb;
      }
      String sSql = "delete from " + this.getM_sTableName() + " ";
      sSql += " where ";
      int primaryKeyLen = this.m_primaryKey.length;// 主键数组的实际长度
      for (int i = 0; i < primaryKeyLen; i++) {
        if (m_primaryKey[i] == null || "".equals(m_primaryKey[i])) {
          primaryKeyLen = i;
          break;
        }
        if (i > 0) {
          sSql += " AND ";
        }
        sSql += (m_primaryKey[i] + "=?");
      }
      SQLiteStatement stmt = db.compileStmt(sSql);
      try {
        stmt.clearBindings();
        int index = 1;
        for (int i = 0; i < primaryKeyLen; i++) {
          for (int j = 0; j < m_columnCount; j++) {
            if (m_columnName[j].equals(m_primaryKey[i])) {
              setPreStm(stmt, index, m_columnType[j], m_columnName[j]);
              index++;
              break;
            }
          }
        }
        int i = stmt.executeUpdateDelete();
        if (i > 0) {
          // 移除当前行资料
          m_rows.remove(m_currRow);
          // 资料笔数减一
          m_rowCount--;
          // 向前移动一行
          this.move(m_currRow - 1);
          return true;
        } else {
          return false;
        }
      } finally {
        stmt.close();
        stmt = null;
      }
    } finally {
      if (db != null && outSideDb == null) {
        db.close();
        db = null;
      }
    }
  }

  /**
   * 移动到第一行
   *
   * @return
   */
  public boolean first() {
    if (m_rowCount == 0)
      return false;
    m_currRow = 0;
    return true;
  }

  /**
   * 移动到下一行
   *
   * @return
   */
  public boolean next() {
    if (m_currRow + 1 < m_rowCount) {
      m_currRow++;
      return true;
    }
    return false;
  }

  /**
   * 移动上一行
   *
   * @return
   */
  public boolean previous() {
    if (m_currRow - 1 > -1) {
      m_currRow--;
      return true;
    }
    return false;
  }

  /**
   * 移动到某一行
   *
   * @param rowIndex
   * @return
   */
  public boolean move(int rowIndex) {
    if (rowIndex < 0) {
      this.first();
    } else if (rowIndex < (m_rowCount - 1)) {
      m_currRow = rowIndex;
      return true;
    } else {
      this.last();
    }
    return false;
  }

  /**
   * 移动到-1行，为了给next()使用
   *
   * @return
   */
  public void moveBeforeFirst() {
    m_currRow = -1;
  }

  /**
   * 移动到最后一行
   *
   * @return
   */
  public boolean last() {
    if (m_rowCount == 0)
      return false;
    m_currRow = m_rowCount - 1;
    return true;
  }

  /**
   * 是否是最后一行
   *
   * @return
   */
  public boolean isLast() {
    return (m_currRow == m_rowCount - 1);
  }

  /**
   * 是否是第一行
   *
   * @return
   */
  public boolean isFirst() {
    return (m_currRow == 0);
  }

  /**
   * 移动到某一行,不判断是否超过范围
   *
   * @param rowIndex
   * @return
   */
  public boolean moveIndex(int rowIndex) {
    m_currRow = rowIndex;
    return true;
  }

  /**
   * 删除一行,并且移动到下一行(并无操作到DB)
   *
   * @throws Exception
   */
  public void cleanOneRow(int rowIndex) throws Exception {
    this.move(rowIndex);
    cleanCurRow();
  }

  /**
   * 删除当前行(并无操作到DB) 有时下标为0的时候处理有点异常 考虑要加上 <br>
   * 20140425 if(this.isFirst()){ this.moveBeforeFisrt(); }
   *
   * @throws Exception
   */
  public void cleanCurRow() throws Exception {
    if ((m_currRow < 0) || (m_currRow >= m_rowCount))
      return;
    m_rows.remove(m_currRow);
    m_currRow--;
    m_rowCount--;
    this.move(m_currRow);
  }

  /**
   * 将数据清除(并无操作到DB)
   */
  public void cleanData() {
    this.m_rowCount = 0;
    this.m_rows.clear();
    this.m_currRow = -1;
  }

  /**
   * 清空对象，最后不用的时候调用，不能乱用
   */
  public void setNull() {
    try {
      this.cleanData();
      this.m_columnName = null;
      if (this.m_columnIndexMap != null) {
        this.m_columnIndexMap.clear();
        this.m_columnIndexMap = null;
      }
      this.m_columnType = null;
      this.m_primaryKey = null;
      this.m_rows = null;
    } catch (Exception e) {
      LogKit.error(e, e);
    }
  }

  /**
   * 创建一新行
   *
   * @throws Exception
   */
  public void createNewRow() {
    m_rowCount++;
    // 移动到最后一行
    m_currRow = m_rowCount - 1;
    Object[] oList = new Object[this.m_columnCount];
    m_rows.add(m_currRow, oList);
  }

  /**
   * 给所有的对应的属性赋一个值
   *
   * @param sName
   * @param oValue
   */
  public void setAllObject(String sName, Object oValue) {
    if (this.getM_rowCount() < 1) {
      return;
    }
    Integer iValue = (Integer) m_columnIndexMap.get(sName);
    if (iValue != null) {
      this.first();
      do {
        Object[] oList = (Object[]) m_rows.get(m_currRow);
        oList[iValue] = oValue;
        m_rows.remove(m_currRow);
        m_rows.add(m_currRow, oList);
      } while (this.next());
    }
  }

  /**
   * 给所有的对应的属性赋一个值 ,oNoValue的除外
   *
   * @param sName
   * @param oNoValue
   * @param oValue
   */
  public void setAllObject(String sName, Object oNoValue, Object oValue) {
    if (this.getM_rowCount() < 1) {
      return;
    }
    Integer iValue = (Integer) m_columnIndexMap.get(sName);
    if (iValue != null) {
      this.first();
      do {
        Object[] oList = (Object[]) m_rows.get(m_currRow);
        if (emisUtil.parseString(oNoValue).equals(emisUtil.parseString(oList[iValue]))) {
          continue;
        }
        oList[iValue] = oValue;
        m_rows.remove(m_currRow);
        m_rows.add(m_currRow, oList);
      } while (this.next());
    }
  }

  /**
   * 给所有的oReplacValue值对应的属性赋一个值
   *
   * @param sName
   * @param oReplacValue
   * @param oValue
   */
  public void setAllReplaceObject(String sName, Object oReplacValue, Object oValue) {
    if (this.getM_rowCount() < 1) {
      return;
    }
    Integer iValue = (Integer) m_columnIndexMap.get(sName);
    if (iValue != null) {
      this.first();
      do {
        Object[] oList = (Object[]) m_rows.get(m_currRow);
        if (emisUtil.parseString(oReplacValue).equals(emisUtil.parseString(oList[iValue]))) {
          oList[iValue] = oValue;
          m_rows.remove(m_currRow);
          m_rows.add(m_currRow, oList);
        }
      } while (this.next());
    }
  }

  /**
   * 给对应的属性赋值
   *
   * @param sName
   * @param oValue
   */
  public void setObject(String sName, Object oValue) {
    if ((m_currRow < 0) || (m_currRow >= m_rowCount))
      return; // out of bound
    sName = emisUtil.parseString(sName).trim();
    if (!m_columnIndexMap.containsKey(sName.toUpperCase())) {
      return;
    }
    Integer iValue = (Integer) m_columnIndexMap.get(sName);
    if (iValue != null) {
      Object[] oList = (Object[]) m_rows.get(m_currRow);
      if (iValue > oList.length) {
        return;
      }
      oList[iValue] = oValue;
      //m_rows.remove(m_currRow);
      //m_rows.add(m_currRow, oList);
    } else {
      // emis帐号才能看到log
      if (emisKeeper.getInstance().getEmploy() != null
        && (Employ.SUPPORT.equalsIgnoreCase(emisKeeper.getInstance().getEmploy().getString("OP_NO"))
        || Employ.ADMIN.equalsIgnoreCase(emisKeeper.getInstance().getEmploy().getString("OP_NO")))) {
        if ((getM_sTableName() == null) || (getM_sTableName().trim().length() == 0)) {
          LogKit.error("Can not find filed : [" + sName + "] in the entity.");
        } else {
          LogKit.error("Can not find filed : [" + sName + "] in the entity [" + getM_sTableName() + "]");
        }
      }
    }
  }

  /**
   * 给对应的属性赋值
   *
   * @param sName
   * @param sValue
   */
  public void setString(String sName, String sValue) {
    setObject(sName, sValue);
  }

  /**
   * 给对应的属性赋值
   *
   * @param sName
   * @param dValue
   */
  public void setDouble(String sName, Double dValue) {
    setObject(sName, dValue);
  }

  /**
   * 给对应的属性赋值
   *
   * @param sName
   * @param iValue
   */
  public void setInt(String sName, Integer iValue) {
    setObject(sName, iValue);
  }

  /**
   * 取出当前行的对象数组
   */
  public Object[] getRowObject() {
    if (m_rowCount <= 0 || m_currRow < 0 || m_currRow >= m_rowCount)
      return null; // out of bound
    return (Object[]) m_rows.get(m_currRow);
  }

  /**
   * 取出当前行的对象数组
   */
  public String getRowObjectToString() {
    return getRowObjectToString(m_currRow);
  }

  /**
   * 取出当前行的对象数组
   *
   * @param row
   */
  public String getRowObjectToString(int row) {
    if (m_rowCount <= 0 || row < 0 || row >= m_rowCount)
      return null; // out of bound
    Object[] objs = (Object[]) m_rows.get(row);
    StringBuffer str = new StringBuffer("");
    for (int i = 0; i < objs.length; i++) {
      str.append(objs[i] + ",");
    }
    // emis帐号才能看到log
    if (emisKeeper.getInstance().getEmploy() != null
      && (Employ.SUPPORT.equalsIgnoreCase(emisKeeper.getInstance().getEmploy().getString("OP_NO"))
      || Employ.ADMIN.equalsIgnoreCase(emisKeeper.getInstance().getEmploy().getString("OP_NO")))) {
      LogKit.info(str.toString());
    }
    return str.toString();
  }

  /**
   * 设置当前行的对象数组
   *
   * @param obj
   */
  public void setRowObject(Object[] obj) {
    if (m_rowCount <= 0 || m_currRow < 0 || m_currRow >= m_rowCount)
      return; // out of bound
    m_rows.remove(m_currRow);
    m_rows.add(m_currRow, obj);
  }

  /**
   * 取出对应属性的数据
   *
   * @param sName
   * @return
   */
  public Object getObject(String sName) {
    if (m_rowCount <= 0 || m_currRow < 0 || m_currRow >= m_rowCount)
      return ""; // out of bound
    sName = emisUtil.parseString(sName).trim();
    if (!m_columnIndexMap.containsKey(sName.toUpperCase())) {
      return "";
    }
    Integer iValue = (Integer) m_columnIndexMap.get(sName.toUpperCase());
    if (iValue != null) {
      Object[] oList = (Object[]) m_rows.get(m_currRow);
      if (iValue > oList.length) {
        return "";
      }
      return oList[iValue];
    } else {
      // emis帐号才能看到log
      if (emisKeeper.getInstance().getEmploy() != null
        && (Employ.SUPPORT.equalsIgnoreCase(emisKeeper.getInstance().getEmploy().getString("OP_NO"))
        || Employ.ADMIN.equalsIgnoreCase(emisKeeper.getInstance().getEmploy().getString("OP_NO")))) {
        if ((getM_sTableName() == null) || (getM_sTableName().trim().length() == 0)) {
          LogKit.error("Can not find filed : [" + sName + "] in the entity.");
        } else {
          LogKit.error("Can not find filed : [" + sName + "] in the entity [" + getM_sTableName() + "]");
        }
      }
    }
    return "";
  }

  /**
   * 取出对应属性的index
   *
   * @param sName
   * @return
   */
  public int getIndex(String sName) {
    return emisUtil.parseInt(m_columnIndexMap.get(sName));
  }

  /**
   * 取出对应属性的数据
   *
   * @param sName
   * @return
   */
  public String getString(String sName) {
    return emisUtil.parseString(getObject(sName));
  }

  /**
   * 取出对应属性的数据
   *
   * @param sName
   * @return
   */
  public boolean getBoolean(String sName) {
    return emisUtil.parseBoolean(getObject(sName));
  }

  /**
   * 取出对应属性的数据
   *
   * @param sName
   * @return
   */
  public double getDouble(String sName) {
    return emisUtil.parseDouble(getObject(sName));
  }

  /**
   * 取出对应属性的数据
   *
   * @param sName
   * @return
   */
  public int getInt(String sName) {
    return emisUtil.parseInt(getObject(sName));
  }

  /**
   * 取出对应index的数据
   *
   * @param index
   * @return
   */
  public Object getObject(int index) {
    if ((m_currRow < 0) || (m_currRow >= m_rowCount))
      return null; // out of bound
    Object[] oList = (Object[]) m_rows.get(m_currRow);
    return oList[index];
  }

  /**
   * 取出对应index的数据
   *
   * @param index
   * @return
   */
  public String getString(int index) {
    return emisUtil.parseString(getObject(index));
  }

  /**
   * 取出对应index的数据
   *
   * @param index
   * @return
   */
  public double getDouble(int index) {
    return emisUtil.parseDouble(getObject(index));
  }

  /**
   * 取出对应index的数据
   *
   * @param index
   * @return
   */
  public int getInt(int index) {
    return emisUtil.parseInt(getObject(index));
  }

  /**
   * 初始化
   */
  private void init() {
    this.m_rows = new Vector<Object[]>();
    this.m_columnIndexMap = new HashMap<String, Integer>();
  }

  /**
   * 初始化表格
   *
   * @param sTableName
   * @throws Exception
   */
  private void initTable(String sTableName) throws Exception {
    this.setM_sTableName(sTableName);
    this.initFiledType();
  }

  /**
   * 取出表字段的类型及主键,在初始化表格时调用
   *
   * @throws Exception
   */
  private void initFiledType() throws Exception {
    if (m_columnType != null) {
      return;
    }
    emisSQLiteWrapper db = null;
    Cursor cursor = null;
    try {
      db = emisDb.getInstance();
      //DatabaseMetaData md = db.getMetaData();
      //rs = md.getPrimaryKeys(null, null, this.getM_sTableName());
      // 获取表中的所有字段名
      cursor = db.rawQuery("pragma table_info(" + this.getM_sTableName() + ")", null);
      //m_meta = rs.getMetaData();
      m_columnCount = cursor.getCount();
      m_columnCount_t = cursor.getCount();
      m_columnType = new int[m_columnCount];
      m_columnName = new String[m_columnCount];
      // 先固定给5个。注循环时要判断是否为null或空，然后break
      m_primaryKey = new String[5];
      int index = 0;
      int i = 0;
      while (cursor.moveToNext()) {
        if ("1".equals(cursor.getString(cursor.getColumnIndex("pk")))) {
          m_primaryKey[index++] = cursor.getString(cursor.getColumnIndex("name")).toUpperCase();
        }
        if (cursor.getString(cursor.getColumnIndex("type")).startsWith("varchar")) {
          m_columnType[i] = java.sql.Types.VARCHAR;
        } else {
          m_columnType[i] = java.sql.Types.VARCHAR;
        }
        m_columnName[i] = cursor.getString(cursor.getColumnIndex("name")).toUpperCase();
        // build ColumnName & index mapping
        m_columnIndexMap.put(m_columnName[i], i);
        i++;
      }
    } finally {
      if (cursor != null) {
        cursor.close();
        cursor = null;
      }
      if (db != null) {
        db.close();
        db = null;
      }
    }
  }

  /**
   * 将resultSet中的数据放入集合中
   *
   * @param cursor
   * @throws Exception
   */
  private void initRS(SQLiteCursor cursor) throws Exception {
    int i;
    m_rowCount = 0;
    m_currRow = -1;
    while (cursor.moveToNext()) {
      Object[] oList = new Object[m_columnCount];
      for (i = 0; i < m_columnCount; i++) {
        switch (m_columnType[i]) {
          case java.sql.Types.SMALLINT:
          case java.sql.Types.INTEGER:
            oList[i] = emisUtil.parseInt(cursor.getInt(i));
            break;
          case java.sql.Types.NUMERIC:
          case java.sql.Types.DECIMAL:
          case java.sql.Types.FLOAT:
          case java.sql.Types.DOUBLE:
            oList[i] = emisUtil.parseDouble(cursor.getDouble(i));
            break;
          default:
            oList[i] = emisUtil.parseString(cursor.getString(i));
            break;
        }
      }
      m_rows.add(oList);
      m_rowCount++;
    }
  }

  /**
   * 将resultSet中的数据放入集合中<br>
   * 此方法会重新取列对应的类型和名称 <br>
   * 在直接使用sql查询时候用此方法防止多次取列对应的类型和名称
   *
   * @param cursor
   * @throws Exception
   */
  private void initSqlRS(SQLiteCursor cursor) throws Exception {
    if (cursor == null || cursor.getCount() <= 0) return;
    int i;
    //m_meta = cursor.getMetaData();
    m_columnCount = cursor.getColumnCount();
    m_columnType = new int[m_columnCount];
    m_columnName = new String[m_columnCount];
    for (i = 0; i < m_columnCount; i++) {
      try {
        m_columnType[i] = cursor.getType(cursor.getColumnIndex(cursor.getColumnName(i)));
      } catch (Exception e) {
        m_columnType[i] = java.sql.Types.VARCHAR;
      }
      m_columnName[i] = cursor.getColumnName(i).toUpperCase();
      // build ColumnName & index mapping
      m_columnIndexMap.put(m_columnName[i], i);
    }

    m_rowCount = 0;
    m_currRow = -1;
    while (cursor.moveToNext()) {
      Object[] oList = new Object[m_columnCount];
      for (i = 0; i < m_columnCount; i++) {
        switch (m_columnType[i]) {
          case java.sql.Types.SMALLINT:
          case java.sql.Types.INTEGER:
            oList[i] = emisUtil.parseInt(cursor.getInt(i));
            break;
          case java.sql.Types.NUMERIC:
          case java.sql.Types.DECIMAL:
          case java.sql.Types.FLOAT:
          case java.sql.Types.DOUBLE:
            oList[i] = emisUtil.parseDouble(cursor.getDouble(i));
            break;
          default:
            oList[i] = emisUtil.parseString(cursor.getString(i));
            break;
        }
      }
      m_rows.add(oList);
      m_rowCount++;
    }
  }

  /**
   * 为SQLiteStatement设定值
   *
   * @param stmt
   * @param index 下标
   * @param iType 类型
   * @param sName 名称
   * @throws SQLException
   */
  private void setPreStm(SQLiteStatement stmt, int index, int iType, String sName) {
    switch (iType) {
      case java.sql.Types.SMALLINT:
      case java.sql.Types.INTEGER:
        stmt.bindLong(index, this.getInt(sName));
        break;
      case java.sql.Types.NUMERIC:
      case java.sql.Types.DECIMAL:
      case java.sql.Types.FLOAT:
      case java.sql.Types.DOUBLE:
        stmt.bindDouble(index, this.getDouble(sName));
        break;
      default:
        stmt.bindString(index, this.getString(sName));
        break;
    }
  }

  /**
   * 在控制台打出Entity 的各个字段的set和get方法，便于写代码时清楚各个字段的类型
   */
  protected void developUtil() {
    System.err.println("===============set=======================");
    for (int i = 0; i < m_columnCount; i++) {
      switch (m_columnType[i]) {
        case java.sql.Types.SMALLINT:
        case java.sql.Types.INTEGER:
          System.err.println("curEntity.setInt(" + "\"" + m_columnName[i] + "\", );");
          break;
        case java.sql.Types.NUMERIC:
        case java.sql.Types.DECIMAL:
        case java.sql.Types.FLOAT:
        case java.sql.Types.DOUBLE:
          System.err.println("curEntity.setDouble(" + "\"" + m_columnName[i] + "\", );");
          break;
        default:
          System.err.println("curEntity.setString(" + "\"" + m_columnName[i] + "\", );");
          break;
      }
    }
    System.err.println("===============set=======================");

    System.err.println("===============get=======================");
    for (int i = 0; i < m_columnCount; i++) {
      switch (m_columnType[i]) {
        case java.sql.Types.SMALLINT:
        case java.sql.Types.INTEGER:
          System.err.println("curEntity.getInt(" + "\"" + m_columnName[i] + "\");");
          break;
        case java.sql.Types.NUMERIC:
        case java.sql.Types.DECIMAL:
        case java.sql.Types.FLOAT:
        case java.sql.Types.DOUBLE:
          System.err.println("curEntity.getDouble(" + "\"" + m_columnName[i] + "\");");
          break;
        default:
          System.err.println("curEntity.getString(" + "\"" + m_columnName[i] + "\");");
          break;
      }
    }
    System.err.println("===============get=======================");

    System.err.println("===============set&get=======================");
    for (int i = 0; i < m_columnCount; i++) {
      switch (m_columnType[i]) {
        case java.sql.Types.SMALLINT:
        case java.sql.Types.INTEGER:
          System.err.println("curEntity.setInt(" + "\"" + m_columnName[i]
            + "\",curEntity2.getInt(" + "\"" + m_columnName[i]
            + "\"));");
          break;
        case java.sql.Types.NUMERIC:
        case java.sql.Types.DECIMAL:
        case java.sql.Types.FLOAT:
        case java.sql.Types.DOUBLE:
          System.err.println("curEntity.setDouble(" + "\""
            + m_columnName[i] + "\",curEntity2.getDouble(" + "\""
            + m_columnName[i] + "\"));");
          break;
        default:
          System.err.println("curEntity.setString(" + "\""
            + m_columnName[i] + "\",curEntity2.getString(" + "\""
            + m_columnName[i] + "\"));");
          break;
      }
    }
    System.err.println("===============set&get=======================");

    System.err.println("===============FILED=======================");
    for (int i = 0; i < m_columnCount; i++) {
      System.err.println(m_columnName[i]);
    }
    System.err.println("===============FILED=======================");
  }

  /**
   * 拷贝另一个entity的所有栏位(所有行的数据，里面已经循环处理了)
   *
   * @param oEntity
   */
  public Entity copyAllFiled(Entity oEntity) {
    if (emisUtil.isEmpty(oEntity)) {
      return this;
    }
    oEntity.first();
    do {
      this.createNewRow();
      setFiledValue(oEntity);
    } while (oEntity.next());
    this.first();
    return this;
  }

  /**
   * 拷贝另一个entity的当前行所有栏位
   *
   * @param oEntity
   */
  public Entity copyFiled(Entity oEntity) {
    if (emisUtil.isEmpty(oEntity)) {
      return this;
    }
    this.createNewRow();
    return setFiledValue(oEntity);
  }

  /**
   * 赋值 ，Entity不会创建新行
   *
   * @param oEntity
   */
  public Entity setFiledValue(Entity oEntity) {
    if (emisUtil.isEmpty(oEntity)) {
      return this;
    }
    for (int i = 0; i < m_columnCount; i++) {
      if (!oEntity.m_columnIndexMap.containsKey(m_columnName[i])) {
        continue;
      }
      switch (m_columnType[i]) {
        case java.sql.Types.SMALLINT:
        case java.sql.Types.INTEGER:
          this.setInt(m_columnName[i], oEntity.getInt(m_columnName[i]));
          break;
        case java.sql.Types.NUMERIC:
        case java.sql.Types.DECIMAL:
        case java.sql.Types.FLOAT:
        case java.sql.Types.DOUBLE:
          this.setDouble(m_columnName[i], oEntity.getDouble(m_columnName[i]));
          break;
        default:
          this.setString(m_columnName[i], oEntity.getString(m_columnName[i]));
          break;
      }
    }
    return this;
  }

  /**
   * 拷贝某一行的所有数据，并且创建新的一行
   *
   * @param iOldRow
   */
  public void copyRowFiled(int iOldRow) {
    this.move(iOldRow);
    Object[] obj = this.getRowObject();
    this.createNewRow();
    for (int i = 0; i < m_columnCount; i++) {
      switch (m_columnType[i]) {
        case java.sql.Types.SMALLINT:
        case java.sql.Types.INTEGER:
          this.setInt(m_columnName[i], emisUtil.parseInt(obj[i]));
          break;
        case java.sql.Types.NUMERIC:
        case java.sql.Types.DECIMAL:
        case java.sql.Types.FLOAT:
        case java.sql.Types.DOUBLE:
          this.setDouble(m_columnName[i], emisUtil.parseDouble(obj[i]));
          break;
        default:
          this.setString(m_columnName[i], emisUtil.parseString(obj[i]));
          break;
      }
    }
  }

  /**
   * 拷贝某一行的所有数据，并且创建新的一行
   */
  public void copyCurRowFiled() {
    copyRowFiled(this.getM_currRow());
  }

  /**
   * 创建一个新的Entity:根据数据库字段，拷贝数据
   */
  public Entity createNewEntity() {
    Entity oNewEntity = new Entity(this.m_sTableName);
    if (emisUtil.isEmpty(this)) {
      return oNewEntity;
    }
    this.first();
    do {
      oNewEntity.createNewRow();
      for (int i = 0; i < m_columnCount; i++) {
        switch (m_columnType[i]) {
          case java.sql.Types.SMALLINT:
          case java.sql.Types.INTEGER:
            oNewEntity.setInt(m_columnName[i], this.getInt(m_columnName[i]));
            break;
          case java.sql.Types.NUMERIC:
          case java.sql.Types.DECIMAL:
          case java.sql.Types.FLOAT:
          case java.sql.Types.DOUBLE:
            oNewEntity.setDouble(m_columnName[i], this.getDouble(m_columnName[i]));
            break;
          default:
            oNewEntity.setString(m_columnName[i], this.getString(m_columnName[i]));
            break;
        }
      }
    } while (this.next());
    oNewEntity.first();
    return oNewEntity;
  }

  /**
   * 得到列名字符串数组,下标从0开始
   *
   * @return
   */
  public String[] getM_columnName() {
    String[] columnName = new String[m_columnName.length];
    for (int i = 0; i < m_columnName.length; i++) {
      columnName[i] = m_columnName[i];
    }
    return columnName;
  }

  /**
   * 创建len笔数据，给初始值<br>
   * 然后可以操作Entity,赋值
   *
   * @param len
   */
  public void createData(int len) {
    // 先清除资料
    this.cleanData();
    int i;
    m_rowCount = 0;
    m_currRow = -1;
    for (int j = 0; j < len; j++) {
      Object[] oList = new Object[m_columnCount];
      for (i = 0; i < m_columnCount; i++) {
        switch (m_columnType[i]) {
          case java.sql.Types.SMALLINT:
          case java.sql.Types.INTEGER:
            oList[i] = 0;
            break;
          case java.sql.Types.NUMERIC:
          case java.sql.Types.DECIMAL:
          case java.sql.Types.FLOAT:
          case java.sql.Types.DOUBLE:
            oList[i] = 0;
            break;
          default:
            oList[i] = "";
            break;
        }
      }
      m_rows.add(oList);
      m_rowCount++;
    }
  }

  public void setM_sTableName(String m_sTableName) {
    this.m_sTableName = m_sTableName;
  }

  public String getM_sTableName() {
    return m_sTableName;
  }

  /**
   * 将m_columnName转成用，号隔开的字符串
   *
   * @return
   */
  public String columnToStr() {
    if (this.getM_columnName() == null && this.getM_columnName().length < 1) {
      return "";
    }
    StringBuffer strBuf = null;
    for (int i = 0; i < this.getM_columnName().length; i++) {
      if (strBuf == null) {
        strBuf = new StringBuffer(this.getM_columnName()[i]);
      } else {
        strBuf.append("," + this.getM_columnName()[i]); //$NON-NLS-1$
      }
    }
    return strBuf.toString();
  }

  public String[] getM_columnName_1() {
    return m_columnName;
  }

  public int getM_columnCount() {
    return m_columnCount;
  }

  public int[] getM_columnType() {
    return m_columnType;
  }

  protected void doAuditSQL(String sSql, String[] selectionArgs) {
    // emis帐号才能看到log
    if (emisKeeper.getInstance().getEmploy() != null
      && (Employ.SUPPORT.equalsIgnoreCase(emisKeeper.getInstance().getEmploy().getString("OP_NO"))
      || Employ.ADMIN.equalsIgnoreCase(emisKeeper.getInstance().getEmploy().getString("OP_NO")))) {
      StringBuffer sbSql = new StringBuffer(sSql);
      if (selectionArgs != null) {
        for (int idx, i = 0; i < selectionArgs.length; i++) {
          if ((idx = sbSql.indexOf("?")) == -1)
            break;
          // 修正参数有问号时替换错位
          sbSql.replace(idx, idx + 1, "'" + selectionArgs[i]
            .replaceAll("\\?", "@PARAM@") + "'");
        }
      }
      LogKit.info(sbSql.toString().replaceAll("@PARAM@", "\\?"));
      sbSql = null;
    }
  }

  /**
   * 为PreparedStatement SQL 替换参数用于调试
   *
   * @param sSql
   */
  protected void doAuditSQL(String sSql) {
    if (1 == 1 || emisUtil.isDev()) {
      StringBuffer sbSql = new StringBuffer(sSql);
      boolean isUpd = sSql.trim().startsWith("update ");
      for (int idx, i = 0; i < m_columnCount; i++) {
        if ((idx = sbSql.indexOf("?")) == -1)
          break;
        // if (isUpd && isPrimaryKey(m_columnName[i]))
        // continue;
        switch (m_columnType[i]) {
          case java.sql.Types.SMALLINT:
          case java.sql.Types.INTEGER:
          case java.sql.Types.NUMERIC:
          case java.sql.Types.DECIMAL:
          case java.sql.Types.FLOAT:
          case java.sql.Types.DOUBLE:
            sbSql.replace(idx, idx + 1, emisUtil.parseString(this.getObject(m_columnName[i]), "0"));
            break;
          default:
            // 修正参数有问号时替换错位
            sbSql.replace(idx, idx + 1, "'" + this.getString(m_columnName[i])
              .replaceAll("\\?", "@PARAM@") + "'");
            break;
        }
      }
      if (isUpd) {
        for (int idx, j = 0; j < m_primaryKey.length; j++) {
          if (m_primaryKey[j] == null || "".equals(m_primaryKey[j]))
            continue;
          if ((idx = sbSql.indexOf("?")) == -1)
            break;

          switch (m_columnType[j]) {
            case java.sql.Types.SMALLINT:
            case java.sql.Types.INTEGER:
            case java.sql.Types.NUMERIC:
            case java.sql.Types.DECIMAL:
            case java.sql.Types.FLOAT:
            case java.sql.Types.DOUBLE:
              sbSql.replace(idx, idx + 1, emisUtil.parseString(this.getObject(m_columnName[j]), "0"));
              break;
            default:
              // 修正参数有问号时替换错位
              sbSql.replace(idx, idx + 1, "'" + this.getString(m_primaryKey[j])
                .replaceAll("\\?", "@PARAM@") + "'");
              break;
          }
        }
      }
      LogKit.info(sbSql.toString().replaceAll("@PARAM@", "\\?"));
      sbSql = null;
    }
  }

  private boolean isPrimaryKey(String fieldName) {
    boolean isPrimaryKey = false;
    for (int j = 0; j < m_primaryKey.length; j++) {
      if (m_primaryKey[j] == null || "".equals(m_primaryKey[j]))
        continue;
      if (m_primaryKey[j].equalsIgnoreCase(fieldName)) {
        isPrimaryKey = true;
        break;
      }
    }
    return isPrimaryKey;
  }

  /**
   * 把当前行数据转成新增SQL语句
   *
   * @return
   * @throws Exception
   */
  public String getM_currRowInsertSql() throws Exception {
    if (m_rowCount <= 0 || m_currRow < 0 || m_currRow >= m_rowCount)
      return ""; // 无资料
    try {
      String sSql = "insert into " + this.getM_sTableName() + "(";
      for (int i = 0; i < m_columnCount; i++) {
        if (i == m_columnCount - 1) {
          sSql += (m_columnName[i] + ") values( ");
        } else {
          sSql += (m_columnName[i] + ",");
        }
      }
      for (int i = 0; i < m_columnCount; i++) {
        if (i > 0) {
          sSql += ",";
        }
        sSql += getColumnValue(m_columnType[i], m_columnName[i]);
        if (i == m_columnCount - 1) {
          sSql += ") ";
        }
      }
      // Log SQL
      doAuditSQL(sSql);

      return sSql;
    } finally {
    }
  }

  /**
   * 传入列类型、列名获取对应类型值
   *
   * @param iType 类型
   * @param sName 名称
   * @throws SQLException
   */
  private Object getColumnValue(int iType, String sName) throws SQLException {
    Object obj = null;
    switch (iType) {
      case java.sql.Types.SMALLINT:
      case java.sql.Types.INTEGER:
        obj = this.getInt(sName);
        break;
      case java.sql.Types.NUMERIC:
      case java.sql.Types.DECIMAL:
      case java.sql.Types.FLOAT:
      case java.sql.Types.DOUBLE:
        obj = this.getDouble(sName);
        break;
      default:
        obj = "'" + this.getString(sName) + "'";
        break;
    }
    return obj;
  }

  /**
   * 返回数据存储的集合，便于tableView绑定数据 com.emis.venus.ui.EntityContent
   * com.emis.venus.ui.EntityLabelProvider
   *
   * @return
   */
  public Vector<Object[]> getM_rows() {
    return m_rows;
  }

  /**
   * 数据笔数
   *
   * @return
   */
  public int getM_rowCount() {
    return m_rowCount;
  }

  /**
   * 当前数据所在行
   *
   * @return
   */
  public int getM_currRow() {
    return m_currRow;
  }

  /**
   * 列名所对应的index.<br>
   * 用于m_columnType取对应的类型<br>
   * 及Object[]取对应的数据
   */
  public HashMap<String, Integer> getM_columnIndexMap() {
    return m_columnIndexMap;
  }
}
