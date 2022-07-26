package com.freak.printtool.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.emis.venus.activity.MainActivity;

/**
 * 开机启动广播
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
