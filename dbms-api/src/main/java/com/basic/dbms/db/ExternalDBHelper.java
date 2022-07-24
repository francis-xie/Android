
package com.basic.dbms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;

/**
 * <pre>
 *     desc   : 应用外部(如SD卡等)数据库打开助手

 *     time   : 2018/5/8 上午12:18
 * </pre>
 */
public class ExternalDBHelper extends OrmLiteSqliteOpenHelper {

    /**
     * 数据库存放的根目录
     */
    private String mDBDirPath;

    /**
     * 数据库文件的名称
     */
    private String mDBName;

    /**
     * 应用外部数据库 实现接口
     */
    private IExternalDataBase mIExternalDataBase;

    /**
     * @param context
     * @param dbDirPath         数据库存放的根目录
     * @param dbName            数据库文件的名称
     * @param databaseVersion   数据库版本号
     * @param iExternalDataBase 应用外部数据库 实现接口
     */
    public ExternalDBHelper(Context context, String dbDirPath, String dbName, int databaseVersion, IExternalDataBase iExternalDataBase) {
        super(context, null, null, databaseVersion);
        mDBDirPath = dbDirPath;
        mDBName = dbName;
        mIExternalDataBase = iExternalDataBase;

        mIExternalDataBase.createOrOpenDB(connectionSource);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        if (mIExternalDataBase != null) {
            mIExternalDataBase.onCreate(database, connectionSource);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (mIExternalDataBase != null) {
            mIExternalDataBase.onUpgrade(database, connectionSource, oldVersion, newVersion);
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return SQLiteDatabase.openDatabase(getFilePath(mDBDirPath, mDBName), null,
                SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return SQLiteDatabase.openDatabase(getFilePath(mDBDirPath, mDBName), null,
                SQLiteDatabase.OPEN_READWRITE);

    }

    /**
     * 获取文件的路径
     *
     * @param dirPath  目录
     * @param fileName 文件名
     * @return 拼接的文件的路径
     */
    private String getFilePath(String dirPath, String fileName) {
        if (TextUtils.isEmpty(dirPath)) return "";

        if (!dirPath.trim().endsWith(File.separator)) {
            dirPath = dirPath.trim() + File.separator;
        }
        return dirPath + fileName;
    }

}
