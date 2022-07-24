
package com.basic.renew.proxy;

import androidx.annotation.NonNull;

import java.util.Map;

/**
 * 版本更新检查器
 *
 
 * @since 2018/6/29 下午8:29
 */
public interface IUpdateChecker {

    /**
     * 版本检查之前
     */
    void onBeforeCheck();

    /**
     * 检查应用的版本信息
     *
     * @param isGet       是否是get请求
     * @param url         版本更新的url地址
     * @param params      请求参数
     * @param updateProxy 版本更新代理
     */
    void checkVersion(boolean isGet, @NonNull String url, @NonNull Map<String, Object> params, @NonNull IUpdateProxy updateProxy);

    /**
     * 版本检查之后
     */
    void onAfterCheck();

    /**
     * 处理版本检查的结果
     *
     * @param result      检查返回的内容
     * @param updateProxy 版本更新代理
     */
    void processCheckResult(@NonNull String result, @NonNull IUpdateProxy updateProxy);

    /**
     * 未发现新版本
     *
     * @param throwable 未发现的原因
     */
    void noNewVersion(Throwable throwable);
}
