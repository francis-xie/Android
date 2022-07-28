
package com.basic.renew.proxy.impl;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.basic.renew._Update;
import com.basic.renew.entity.UpdateEntity;
import com.basic.renew.listener.IUpdateParseCallback;
import com.basic.renew.proxy.IUpdateChecker;
import com.basic.renew.proxy.IUpdateHttpService;
import com.basic.renew.proxy.IUpdateProxy;
import com.basic.renew.service.DownloadService;
import com.basic.renew.utils.UpdateUtils;

import java.util.Map;

import static com.basic.renew.entity.UpdateError.ERROR.CHECK_JSON_EMPTY;
import static com.basic.renew.entity.UpdateError.ERROR.CHECK_NET_REQUEST;
import static com.basic.renew.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;
import static com.basic.renew.entity.UpdateError.ERROR.CHECK_PARSE;
import static com.basic.renew.entity.UpdateError.ERROR.CHECK_UPDATING;

/**
 * 默认版本更新检查者
 */
public class DefaultUpdateChecker implements IUpdateChecker {

    @Override
    public void onBeforeCheck() {

    }

    @Override
    public void checkVersion(boolean isGet, @NonNull final String url, @NonNull Map<String, Object> params, final @NonNull IUpdateProxy updateProxy) {
        if (DownloadService.isRunning() || _Update.getCheckUrlStatus(url) || _Update.isPrompterShow(url)) {
            updateProxy.onAfterCheck();
            _Update.onUpdateError(CHECK_UPDATING);
            return;
        }

        _Update.setCheckUrlStatus(url, true);

        if (isGet) {
            updateProxy.getIUpdateHttpService().asyncGet(url, params, new IUpdateHttpService.Callback() {
                @Override
                public void onSuccess(String result) {
                    onCheckSuccess(url, result, updateProxy);
                }

                @Override
                public void onError(Throwable error) {
                    onCheckError(url, updateProxy, error);
                }
            });
        } else {
            updateProxy.getIUpdateHttpService().asyncPost(url, params, new IUpdateHttpService.Callback() {
                @Override
                public void onSuccess(String result) {
                    onCheckSuccess(url, result, updateProxy);
                }

                @Override
                public void onError(Throwable error) {
                    onCheckError(url, updateProxy, error);
                }
            });
        }
    }

    @Override
    public void onAfterCheck() {

    }

    /**
     * 查询成功
     *
     * @param url         查询地址
     * @param result      查询结果
     * @param updateProxy 更新代理
     */
    private void onCheckSuccess(String url, String result, @NonNull IUpdateProxy updateProxy) {
        _Update.setCheckUrlStatus(url, false);
        updateProxy.onAfterCheck();
        if (!TextUtils.isEmpty(result)) {
            processCheckResult(result, updateProxy);
        } else {
            _Update.onUpdateError(CHECK_JSON_EMPTY);
        }
    }

    /**
     * 查询失败
     *
     * @param url         查询地址
     * @param updateProxy 更新代理
     * @param error       错误
     */
    private void onCheckError(String url, @NonNull IUpdateProxy updateProxy, Throwable error) {
        _Update.setCheckUrlStatus(url, false);
        updateProxy.onAfterCheck();
        _Update.onUpdateError(CHECK_NET_REQUEST, error.getMessage());
    }

    @Override
    public void processCheckResult(final @NonNull String result, final @NonNull IUpdateProxy updateProxy) {
        try {
            if (updateProxy.isAsyncParser()) {
                //异步解析
                updateProxy.parseJson(result, new IUpdateParseCallback() {
                    @Override
                    public void onParseResult(UpdateEntity updateEntity) {
                        try {
                            UpdateUtils.processUpdateEntity(updateEntity, result, updateProxy);
                        } catch (Exception e) {
                            e.printStackTrace();
                            _Update.onUpdateError(CHECK_PARSE, e.getMessage());
                        }
                    }
                });
            } else {
                //同步解析
                UpdateUtils.processUpdateEntity(updateProxy.parseJson(result), result, updateProxy);
            }
        } catch (Exception e) {
            e.printStackTrace();
            _Update.onUpdateError(CHECK_PARSE, e.getMessage());
        }
    }

    @Override
    public void noNewVersion(Throwable throwable) {
        _Update.onUpdateError(CHECK_NO_NEW_VERSION, throwable != null ? throwable.getMessage() : null);
    }
}
