package com.basic.code.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.basic.code.activity.LoginActivity;
import com.basic.tools.app.ActivityUtils;
import com.basic.tools.common.StringUtils;

/**
 * Token管理工具
 */
public final class TokenUtils {

    private static String sToken;

    private static final String KEY_TOKEN = "com.basic.code.utils.KEY_TOKEN";

    private TokenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String KEY_PROFILE_CHANNEL = "github";

    /**
     * 初始化Token信息
     */
    public static void init(Context context) {
        MMKVUtils.init(context);
        sToken = MMKVUtils.getString(KEY_TOKEN, "");
    }

    public static void setToken(String token) {
        sToken = token;
        MMKVUtils.put(KEY_TOKEN, token);
    }

    public static void clearToken() {
        sToken = null;
        MMKVUtils.remove(KEY_TOKEN);
    }

    public static String getToken() {
        return sToken;
    }

    public static boolean hasToken() {
        return MMKVUtils.containsKey(KEY_TOKEN);
    }

    /**
     * 处理登录成功的事件
     *
     * @param token 账户信息
     */
    public static boolean handleLoginSuccess(String token) {
        if (!StringUtils.isEmpty(token)) {
            ToastUtils.success("登录成功！");
            MobclickAgent.onProfileSignIn(KEY_PROFILE_CHANNEL, token);
            setToken(token);
            return true;
        } else {
            ToastUtils.error("登录失败！");
            return false;
        }
    }

    /**
     * 处理登出的事件
     */
    public static void handleLogoutSuccess() {
        MobclickAgent.onProfileSignOff();
        //登出时，清除账号信息
        clearToken();
        ToastUtils.success("登出成功！");
        SettingUtils.setIsAgreePrivacy(false);
        //跳转到登录页
        ActivityUtils.startActivity(LoginActivity.class);
    }

}
