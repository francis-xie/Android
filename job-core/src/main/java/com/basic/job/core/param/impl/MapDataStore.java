
package com.basic.job.core.param.impl;

import com.basic.job.core.param.IDataStore;
import com.basic.job.logger.TaskLogger;
import com.basic.job.utils.CommonUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认数据存储仓库， 使用ConcurrentHashMap实现
 *

 * @since 2021/11/13 4:09 PM
 */
public class MapDataStore implements IDataStore {

    private static final String TAG = TaskLogger.getLogTag("MapDataStore");

    /**
     * 数据存储
     */
    private Map<String, Object> mData = new ConcurrentHashMap<>();

    @Override
    public Object get(String key) {
        if (key == null) {
            return null;
        }
        return mData.get(key);
    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        return CommonUtils.cast(get(key), clazz);
    }

    @Override
    public <T> T getObject(String key, T defaultValue) {
        return CommonUtils.cast(get(key), defaultValue);
    }

    @Override
    public String getString(String key) {
        return getString(key, DEFAULT_STRING);
    }

    @Override
    public String getString(String key, String defaultValue) {
        String value = getObject(key, String.class);
        return value != null ? value : defaultValue;
    }

    @Override
    public boolean getBoolean(String key) {
        return getBoolean(key, DEFAULT_BOOLEAN);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        Boolean value = getObject(key, Boolean.class);
        return value != null ? value : defaultValue;
    }

    @Override
    public int getInt(String key) {
        return getInt(key, DEFAULT_INTEGER);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        Integer value = getObject(key, Integer.class);
        return value != null ? value : defaultValue;
    }

    @Override
    public Map<String, Object> getData() {
        return mData;
    }

    @Override
    public void clear() {
        mData.clear();
    }

    @Override
    public MapDataStore put(String key, Object value) {
        if (key == null || value == null) {
            TaskLogger.eTag(TAG, "put param error, key or value is null!");
            return this;
        }
        mData.put(key, value);
        return this;
    }
}
