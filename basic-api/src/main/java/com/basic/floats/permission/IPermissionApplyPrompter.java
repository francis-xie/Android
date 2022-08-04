package com.basic.floats.permission;

import android.app.Dialog;
import android.content.Context;

/**
 * 悬浮权限申请提示
 */
public interface IPermissionApplyPrompter {

    /**
     * 显示悬浮权限申请提示弹窗
     * @param context
     * @param message 提示消息
     * @param result 确认回调
     * @return
     */
    Dialog showPermissionApplyDialog(final Context context, final String message, final FloatWindowPermission.OnConfirmResult result);
}
