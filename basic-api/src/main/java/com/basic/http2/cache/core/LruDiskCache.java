package com.basic.http2.cache.core;

import com.jakewharton.disklrucache.DiskLruCache;
import com.basic.http2.cache.converter.IDiskConverter;
import com.basic.http2.logs.HttpLog;
import com.basic.http2.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * 磁盘缓存实现类
 */
public class LruDiskCache extends BaseDiskCache {
    /**
     * 永久不过期
     */
    public static final long CACHE_NEVER_EXPIRE = -1;
    /**
     * 磁盘转化器
     */
    private IDiskConverter mDiskConverter;
    /**
     * 磁盘缓存
     */
    private DiskLruCache mDiskLruCache;
    /**
     * 磁盘缓存的目录
     */
    private File mDiskDir;
    /**
     * 缓存版本
     */
    private int mAppVersion;
    /**
     * 磁盘存储的最大空间
     */
    private long mDiskMaxSize;

    /**
     * 初始化磁盘缓存
     *
     * @param diskConverter 磁盘转化器
     * @param diskDir       磁盘目录
     * @param appVersion    缓存版本
     * @param diskMaxSize   磁盘最大空间
     */
    public LruDiskCache(IDiskConverter diskConverter, File diskDir, int appVersion, long diskMaxSize) {
        mDiskConverter = Utils.checkNotNull(diskConverter, "mDiskConverter ==null");
        mDiskDir = Utils.checkNotNull(diskDir, "mDiskDir ==null");
        mAppVersion = appVersion;
        mDiskMaxSize = diskMaxSize;
        openCache();
    }

    /**
     * 打开磁盘缓存
     */
    private void openCache() {
        try {
            mDiskLruCache = DiskLruCache.open(mDiskDir, mAppVersion, 1, mDiskMaxSize);
        } catch (IOException e) {
            e.printStackTrace();
            HttpLog.e(e);
        }
    }

    @Override
    protected <T> T doLoad(Type type, String key) {
        if (mDiskLruCache == null) {
            return null;
        }
        try {
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            if (edit == null) {
                return null;
            }
            InputStream source = edit.newInputStream(0);
            T value;
            if (source != null) {
                value = mDiskConverter.load(source, type);
                Utils.closeIO(source);
                edit.commit();
                return value;
            }
            edit.abort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected <T> boolean doSave(String key, T value) {
        if (mDiskLruCache == null) {
            return false;
        }
        try {
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            if (edit == null) {
                return false;
            }
            OutputStream sink = edit.newOutputStream(0);
            if (sink != null) {
                mDiskConverter.writer(sink, value);
                Utils.closeIO(sink);
                edit.commit();
                return true;
            }
            edit.abort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected boolean doContainsKey(String key) {
        if (mDiskLruCache == null) {
            return false;
        }
        try {
            return mDiskLruCache.get(key) != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected boolean doRemove(String key) {
        if (mDiskLruCache == null) {
            return false;
        }
        try {
            return mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    protected boolean doClear() {
        boolean status = false;
        try {
            mDiskLruCache.delete();
            openCache(); //清除缓存后需要重新打开缓存，否则可能无法正常使用
            status = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override
    protected boolean isExpiry(String key, long existTime) {
        if (mDiskLruCache == null) {
            return false;
        }
        if (existTime > CACHE_NEVER_EXPIRE) {//-1表示永久性存储 不用进行过期校验
            //为什么这么写，请了解DiskLruCache，看它的源码
            File file = new File(mDiskLruCache.getDirectory(), key + "." + 0);
            //没有获取到缓存,或者缓存已经过期!
            return isCacheDataFailure(file, existTime);
        }
        return false;
    }

    /**
     * 判断缓存是否已经失效
     */
    private boolean isCacheDataFailure(File dataFile, long time) {
        if (!dataFile.exists()) {
            return false;
        }
        long existTime = System.currentTimeMillis() - dataFile.lastModified();
        return existTime > time * 1000;
    }

}
