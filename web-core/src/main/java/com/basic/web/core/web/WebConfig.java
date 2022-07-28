
package com.basic.web.core.web;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import com.basic.web.utils.WebUtils;
import com.basic.web.utils.LogUtils;

import java.io.File;

import static com.basic.web.utils.WebUtils.getWebFilePath;

public class WebConfig {


	public static final String FILE_CACHE_PATH = "web-cache";
	static final String WEB_CACHE_PATCH = File.separator + "web-cache";
	/**
	 * 缓存路径
	 */
	public static String WEB_FILE_PATH;
	/**
	 * DEBUG 模式 ， 如果需要查看日志请设置为 true
	 */
	public static boolean DEBUG = false;
	/**
	 * 当前操作系统是否低于 KITKAT
	 */
	public static final boolean IS_KITKAT_OR_BELOW_KITKAT = Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
	/**
	 * 默认 WebView  类型 。
	 */
	public static final int WEBVIEW_DEFAULT_TYPE = 1;
	/**
	 * 使用 WebView
	 */
	public static final int WEBVIEW_WEB_SAFE_TYPE = 2;
	/**
	 * 自定义 WebView
	 */
	public static final int WEBVIEW_CUSTOM_TYPE = 3;
	public static int WEBVIEW_TYPE = WEBVIEW_DEFAULT_TYPE;
	private static volatile boolean IS_INITIALIZED = false;
	private static final String TAG = WebConfig.class.getSimpleName();
	/**
	 * Web 的版本
	 */
	public static final String WEB_VERSION = " web/4.0.2 ";

	public static final String WEB_NAME="Web";
	/**
	 * 通过JS获取的文件大小， 这里限制最大为5MB ，太大会抛出 OutOfMemoryError
	 */
	public static int MAX_FILE_LENGTH = 1024 * 1024 * 5;


	//获取Cookie
	public static String getCookiesByUrl(String url) {
		return CookieManager.getInstance() == null ? null : CookieManager.getInstance().getCookie(url);
	}

	public static void debug() {
		DEBUG = true;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}
	}

	/**
	 * 删除所有已经过期的 Cookies
	 */
	public static void removeExpiredCookies() {
		CookieManager mCookieManager = null;
		if ((mCookieManager = CookieManager.getInstance()) != null) { //同步清除
			mCookieManager.removeExpiredCookie();
			toSyncCookies();
		}
	}

	/**
	 * 删除所有 Cookies
	 */
	public static void removeAllCookies() {
		removeAllCookies(null);
	}

	// 解决兼容 Android 4.4 java.lang.NoSuchMethodError: android.webkit.CookieManager.removeSessionCookies
	public static void removeSessionCookies() {
		removeSessionCookies(null);
	}

	/**
	 * 同步cookie
	 *
	 * @param url
	 * @param cookies
	 */
	public static void syncCookie(String url, String cookies) {

		CookieManager mCookieManager = CookieManager.getInstance();
		if (mCookieManager != null) {
			mCookieManager.setCookie(url, cookies);
			toSyncCookies();
		}
	}

	public static void removeSessionCookies(ValueCallback<Boolean> callback) {

		if (callback == null) {
			callback = getDefaultIgnoreCallback();
		}
		if (CookieManager.getInstance() == null) {
			callback.onReceiveValue(new Boolean(false));
			return;
		}
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			CookieManager.getInstance().removeSessionCookie();
			toSyncCookies();
			callback.onReceiveValue(new Boolean(true));
			return;
		}
		CookieManager.getInstance().removeSessionCookies(callback);
		toSyncCookies();

	}

	/**
	 * @param context
	 * @return WebView 的缓存路径
	 */
	public static String getCachePath(Context context) {
		return context.getCacheDir().getAbsolutePath() + WEB_CACHE_PATCH;
	}

	/**
	 * @param context
	 * @return Web 缓存路径
	 */
	public static String getExternalCachePath(Context context) {
		return getWebFilePath(context);
	}


	//Android  4.4  NoSuchMethodError: android.webkit.CookieManager.removeAllCookies
	public static void removeAllCookies(@Nullable ValueCallback<Boolean> callback) {

		if (callback == null) {
			callback = getDefaultIgnoreCallback();
		}
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			CookieManager.getInstance().removeAllCookie();
			toSyncCookies();
			callback.onReceiveValue(!CookieManager.getInstance().hasCookies());
			return;
		}
		CookieManager.getInstance().removeAllCookies(callback);
		toSyncCookies();
	}

	/**
	 * 清空缓存
	 *
	 * @param context
	 */
	public static synchronized void clearDiskCache(Context context) {
		try {

			WebUtils.clearCacheFolder(new File(getCachePath(context)), 0);
			String path = getExternalCachePath(context);
			if (!TextUtils.isEmpty(path)) {
				File mFile = new File(path);
				WebUtils.clearCacheFolder(mFile, 0);
			}
		} catch (Throwable throwable) {
			if (LogUtils.isDebug()) {
				throwable.printStackTrace();
			}
		}
	}


	public static synchronized void initCookiesManager(Context context) {
		if (!IS_INITIALIZED) {
			createCookiesSyncInstance(context);
			IS_INITIALIZED = true;
		}
	}

	private static void createCookiesSyncInstance(Context context) {


		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			CookieSyncManager.createInstance(context);
		}
	}

	private static void toSyncCookies() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			CookieSyncManager.getInstance().sync();
			return;
		}
		AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {

				CookieManager.getInstance().flush();

			}
		});
	}


	static String getDatabasesCachePath(Context context) {
		return context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
	}

	private static ValueCallback<Boolean> getDefaultIgnoreCallback() {
		return new ValueCallback<Boolean>() {
			@Override
			public void onReceiveValue(Boolean ignore) {
				LogUtils.i(TAG, "removeExpiredCookies:" + ignore);
			}
		};
	}
}
