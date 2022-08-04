
package com.basic.dbms.enums;

/**
 * <pre>
 *     desc   : 数据库的类型（内部还是外部)

 *     time   : 2018/5/9 上午12:28
 * </pre>
 */
public enum DataBaseType {
    /**
     * 内部存储的数据库(数据库根目录路径固定为:/data/data/package/databases/)
     */
    INTERNAL,
    /**
     * 外部存储的数据库(数据库根目录路径默认为:/storage/emulated/0/Android/dbms/databases/)
     */
    EXTERNAL

}
