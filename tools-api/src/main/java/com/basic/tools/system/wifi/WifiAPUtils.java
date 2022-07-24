
package com.basic.tools.system.wifi;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiManager;

import com.basic.tools.net.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <pre>
 *     desc   :	热点工具类

 *     time   : 2018/4/23 下午2:45
 * </pre>
 */
public final class WifiAPUtils {
	public static int WIFI_AP_STATE_DISABLING = 0;
	public static int WIFI_AP_STATE_DISABLED = 1;
	public static int WIFI_AP_STATE_ENABLING = 2;
	public static int WIFI_AP_STATE_ENABLED = 3;
	public static int WIFI_AP_STATE_FAILED = 4;
	
	static {
		int versionID = getAndroidSDKVersion();
		if (versionID <= 10) {
			WIFI_AP_STATE_DISABLING = 0;
			WIFI_AP_STATE_DISABLED = 1;
			WIFI_AP_STATE_ENABLING = 2;
			WIFI_AP_STATE_ENABLED = 3;
			WIFI_AP_STATE_FAILED = 4;
		} else {
			WIFI_AP_STATE_DISABLING = 10;
			WIFI_AP_STATE_DISABLED = 11;
			WIFI_AP_STATE_ENABLING = 12;
			WIFI_AP_STATE_ENABLED = 13;
			WIFI_AP_STATE_FAILED = 14;
		}
	}

	private WifiAPUtils() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}
	

	/**
	 * 打开热点
	 * @param wifiAPSsid
	 * @param wifiAPPassword
	 */
	public static void startWifiAp( String wifiAPSsid, String wifiAPPassword) {
		WifiManager wifiManager = NetworkUtils.getWifiManager();
		try {
			Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
			WifiConfiguration netConfig = getWifiApConfig(wifiAPSsid, wifiAPPassword);
			method.invoke(wifiManager, netConfig, true);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 关闭热点
	 * @param wifiAPSsid
	 * @param wifiAPPassword
	 */
	public static void stopWifiAp(String wifiAPSsid, String wifiAPPassword) {
		WifiManager wifiManager = NetworkUtils.getWifiManager();
		try {
			Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
			WifiConfiguration netConfig = getWifiApConfig(wifiAPSsid, wifiAPPassword);
			method.invoke(wifiManager, netConfig, false);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取热点连接的配置
	 * @param wifiApSsid  热点名称
	 * @param wifiApPassword  热点的连接密码
	 * @return
	 */
	private static WifiConfiguration getWifiApConfig(String wifiApSsid, String wifiApPassword) {
		WifiConfiguration netConfig = new WifiConfiguration();
		int securityType = getSecurityType();//通过反射，获取被隐藏的安全类型WPA2_PSK
		//wifi热点名字
		netConfig.SSID = wifiApSsid;
		//密码
		netConfig.preSharedKey = wifiApPassword;
		netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		netConfig.allowedKeyManagement.set(securityType);
		netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		
		return netConfig;
	}

	/**
	 * 获取热点的连接状态
	 * @return
	 */
	public static int getWifiApState() {
		WifiManager wifiManager = NetworkUtils.getWifiManager();
		try {
			Method method = wifiManager.getClass().getMethod("getWifiApState");
			int state = (Integer) method.invoke(wifiManager);
			return state;
		} catch (Exception e) {
			return WIFI_AP_STATE_FAILED;
		}
	}
	
	/**
	 * 热点是否打开
	 * @return
	 */
	public static boolean isWifiApEnable() {
		return getWifiApState() ==  WIFI_AP_STATE_ENABLED;
	}
	
	public static int getAndroidSDKVersion() { 
	   int version = 0; 
	   try { 
	     version = Integer.parseInt(android.os.Build.VERSION.SDK);
	   } catch (NumberFormatException e) { 

	   } 
	   return version; 
	}

	public static int getSecurityType(){
		int securityType = 0;
		try {
			securityType = (Integer)(KeyMgmt.class.getField("WPA2_PSK").get(null));
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return securityType;
	}
	
}