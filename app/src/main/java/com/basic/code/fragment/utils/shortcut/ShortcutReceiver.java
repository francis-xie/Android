
package com.basic.code.fragment.utils.shortcut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.basic.code.utils.XToastUtils;
import com.basic.tools.common.logger.Logger;

/**
 * 快捷方式创建接收广播
 */
public class ShortcutReceiver extends BroadcastReceiver {

    private static final String TAG = "ShortcutReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.dTag(TAG, "onReceive:" + intent.getAction());
        XToastUtils.toast("开始创建快捷方式");
    }
}
