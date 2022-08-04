
package com.basic.web.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.basic.web.utils.LogUtils;

/**
 
 * @date 2018/2/12
 */
public class NotificationCancelReceiver extends BroadcastReceiver {

	public static final String ACTION = "com.web.cancelled";

	public NotificationCancelReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(ACTION)) {
			try {
				String url = intent.getStringExtra("TAG");
				CancelDownloadInformer.getInformer().cancelAction(url);
			} catch (Throwable ignore) {
				if (LogUtils.isDebug()) {
					ignore.printStackTrace();
				}
			}

		}
	}
}