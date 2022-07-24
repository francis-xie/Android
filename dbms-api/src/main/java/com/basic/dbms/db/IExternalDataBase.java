
package com.basic.dbms.db;

import com.j256.ormlite.support.ConnectionSource;

/**
 * <pre>
 *     desc   : 应用外部数据库 实现接口

 *     time   : 2018/5/7 下午10:29
 * </pre>
 */
public interface IExternalDataBase extends IDatabase {

    /**
     * 创建或者打开数据库
     *
     * @param connectionSource
     */
    void createOrOpenDB(ConnectionSource connectionSource);
}
