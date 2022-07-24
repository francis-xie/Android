
package com.basic.dbms.db;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;

/**
 * <pre>
 *     desc   : 应用内部数据库 实现接口

 *     time   : 2018/5/7 下午10:19
 * </pre>
 */
public interface IDatabase {

    /**
     * 数据库创建
     *
     * @param database         SQLite数据库
     * @param connectionSource 数据库连接
     */
    void onCreate(SQLiteDatabase database, ConnectionSource connectionSource);

    /**
     * 数据库升级和降级操作
     *
     * @param database         SQLite数据库
     * @param connectionSource 数据库连接
     * @param oldVersion       旧版本
     * @param newVersion       新版本
     */
    void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion);
}
