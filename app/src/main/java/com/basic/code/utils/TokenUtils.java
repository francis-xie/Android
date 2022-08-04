
package com.basic.code.utils;

import android.content.Context;

import com.tencent.mmkv.MMKV;
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

    /**
     * 初始化Token信息
     */
    public static void init(Context context) {
        MMKV.initialize(context);
        sToken = MMKV.defaultMMKV().decodeString(KEY_TOKEN, "");
    }

    public static void setToken(String token) {
        sToken = token;
        MMKV.defaultMMKV().putString(KEY_TOKEN, token);
    }

    public static void clearToken() {
        sToken = null;
        MMKV.defaultMMKV().remove(KEY_TOKEN);
    }

    public static String getToken() {
        return sToken;
    }

    public static boolean hasToken() {
        return MMKV.defaultMMKV().containsKey(KEY_TOKEN);
    }

    /**
     * 处理登录成功的事件
     *
     * @param token 账户信息
     */
    public static boolean handleLoginSuccess(String token) {
        if (!StringUtils.isEmpty(token)) {
            ToastUtils.success("登录成功！");
            MobclickAgent.onProfileSignIn("github", token);
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
        // 登出清除一下隐私政策
        SettingSPUtils.getInstance().setIsAgreePrivacy(false);
        //跳转到登录页
        ActivityUtils.startActivity(LoginActivity.class);
    }

}
