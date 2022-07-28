
package com.basic.router.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.basic.router.logs.RLog;
import static com.basic.router.utils.Consts.LAST_VERSION_CODE;
import static com.basic.router.utils.Consts.LAST_VERSION_NAME;
import static com.basic.router.utils.Consts.ROUTER_SP_CACHE_KEY;

/**
 * Android package工具类
 *

 * @since 2018/5/17 下午11:49
 */
public final class PackageUtils {
    private static String NEW_VERSION_NAME;
    private static int NEW_VERSION_CODE;

    private PackageUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
    /**
     * 当前应用是否是新版本
     * @param context
     * @return
     */
    public static boolean isNewVersion(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (null != packageInfo) {
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;

            SharedPreferences sp = context.getSharedPreferences(ROUTER_SP_CACHE_KEY, Context.MODE_PRIVATE);
            if (!versionName.equals(sp.getString(LAST_VERSION_NAME, null)) || versionCode != sp.getInt(LAST_VERSION_CODE, -1)) {
                // new version
                NEW_VERSION_NAME = versionName;
                NEW_VERSION_CODE = versionCode;
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 更新版本信息
     * @param context
     */
    public static void updateVersion(Context context) {
        if (!TextUtils.isEmpty(NEW_VERSION_NAME) && NEW_VERSION_CODE != 0) {
            SharedPreferences sp = context.getSharedPreferences(ROUTER_SP_CACHE_KEY, Context.MODE_PRIVATE);
            sp.edit().putString(LAST_VERSION_NAME, NEW_VERSION_NAME).putInt(LAST_VERSION_CODE, NEW_VERSION_CODE).apply();
        }
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (Exception ex) {
            RLog.e("Get package info error.", ex);
        }
        return packageInfo;
    }
}
