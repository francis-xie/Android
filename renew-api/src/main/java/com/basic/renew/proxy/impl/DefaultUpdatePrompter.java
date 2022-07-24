
package com.basic.renew.proxy.impl;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.basic.renew.entity.PromptEntity;
import com.basic.renew.entity.UpdateEntity;
import com.basic.renew.logs.UpdateLog;
import com.basic.renew.proxy.IPrompterProxy;
import com.basic.renew.proxy.IUpdatePrompter;
import com.basic.renew.proxy.IUpdateProxy;
import com.basic.renew.widget.UpdateDialog;
import com.basic.renew.widget.UpdateDialogActivity;
import com.basic.renew.widget.UpdateDialogFragment;

/**
 * 默认的更新提示器
 *

 * @since 2018/7/2 下午4:05
 */
public class DefaultUpdatePrompter implements IUpdatePrompter {

    /**
     * 显示版本更新提示
     *
     * @param updateEntity 更新信息
     * @param updateProxy  更新代理
     * @param promptEntity 提示界面参数
     */
    @Override
    public void showPrompt(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy, @NonNull PromptEntity promptEntity) {
        Context context = updateProxy.getContext();
        if (context == null) {
            UpdateLog.e("showPrompt failed, context is null!");
            return;
        }
        beforeShowPrompt(updateEntity, promptEntity);
        UpdateLog.d("[DefaultUpdatePrompter] showPrompt, " + promptEntity);
        if (context instanceof FragmentActivity) {
            UpdateDialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), updateEntity, getPrompterProxy(updateProxy), promptEntity);
        } else if (context instanceof Activity) {
            UpdateDialog.newInstance(context, updateEntity, getPrompterProxy(updateProxy), promptEntity).show();
        } else {
            UpdateDialogActivity.show(context, updateEntity, getPrompterProxy(updateProxy), promptEntity);
        }
    }

    /**
     * 显示版本更新提示之前的处理【可自定义属于自己的显示逻辑】
     *
     * @param updateEntity 更新信息
     * @param promptEntity 提示界面参数
     */
    protected void beforeShowPrompt(@NonNull UpdateEntity updateEntity, @NonNull PromptEntity promptEntity) {
        // 如果是强制更新的话，默认设置是否忽略下载异常为true，保证即使是下载异常也不退出提示。
        if (updateEntity.isForce()) {
            promptEntity.setIgnoreDownloadError(true);
        }
    }

    /**
     * 构建版本更新提示器代理【可自定义属于自己的业务逻辑】
     *
     * @param updateProxy 版本更新代理
     * @return 版本更新提示器代理
     */
    protected IPrompterProxy getPrompterProxy(@NonNull IUpdateProxy updateProxy) {
        return new DefaultPrompterProxyImpl(updateProxy);
    }

}
