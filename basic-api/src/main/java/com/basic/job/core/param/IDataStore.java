
package com.basic.job.core.param;

import java.util.Map;

/**
 * 数据存储仓库接口
 */
public interface IDataStore {

    /**
     * String的默认值
     */
    String DEFAULT_STRING = "";
    /**
     * Integer的默认值
     */
    int DEFAULT_INTEGER = -1;
    /**
     * Boolean的默认值
     */
    boolean DEFAULT_BOOLEAN = false;

    /**
     * 获取数据
     *
     * @param key 键
     * @return 值
     */
    Object get(String key);

    /**
     * 存储数据
     *
     * @param key   键
     * @param value 值
     * @return 数据存储仓库
     */
    IDataStore put(String key, Object value);

    /**
     * 获取目标数据
     *
     * @param key   键
     * @param clazz 目标数据类型
     * @return 值
     */
    <T> T getObject(String key, Class<T> clazz);

    /**
     * 获取目标数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    <T> T getObject(String key, T defaultValue);

    /**
     * 获取String类型数据
     *
     * @param key 键
     * @return 值
     */
    String getString(String key);

    /**
     * 获取String类型数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    String getString(String key, String defaultValue);

    /**
     * 获取Boolean类型数据
     *
     * @param key 键
     * @return 值
     */
    boolean getBoolean(String key);

    /**
     * 获取boolean类型数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * 获取Integer类型数据
     *
     * @param key 键
     * @return 值
     */
    int getInt(String key);

    /**
     * 获取int类型数据
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    int getInt(String key, int defaultValue);

    /**
     * 获取存储的所以信息
     *
     * @return 存储信息
     */
    Map<String, Object> getData();

    /**
     * 清除数据
     */
    void clear();

}
