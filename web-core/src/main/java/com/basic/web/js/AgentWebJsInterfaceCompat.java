
package com.basic.web.js;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;

import com.basic.web.core.AgentWeb;
import com.basic.web.utils.AgentWebUtils;
import com.basic.web.utils.LogUtils;

import java.lang.ref.WeakReference;

/**
 
 * @since 1.0.0
 */
public class AgentWebJsInterfaceCompat {

	private WeakReference<AgentWeb> mReference = null;
	private WeakReference<Activity> mActivityWeakReference = null;
	private String TAG = this.getClass().getSimpleName();

	public AgentWebJsInterfaceCompat(AgentWeb agentWeb, Activity activity) {
		mReference = new WeakReference<AgentWeb>(agentWeb);
		mActivityWeakReference = new WeakReference<Activity>(activity);
	}


	@JavascriptInterface
	public void uploadFile() {
		uploadFile("*/*");
	}

	@JavascriptInterface
	public void uploadFile(String acceptType) {
		LogUtils.i(TAG, acceptType + "  " + mActivityWeakReference.get() + "  " + mReference.get());
		if (mActivityWeakReference.get() != null && mReference.get() != null) {

			AgentWebUtils.showFileChooserCompat(mActivityWeakReference.get(),
					mReference.get().getWebCreator().getWebView(),
					null,
					null,
					mReference.get().getPermissionInterceptor(),
					null,
					acceptType,
					new Handler.Callback() {
						@Override
						public boolean handleMessage(Message msg) {
							if (mReference.get() != null) {
								mReference.get().getJsAccessEntrace()
										.quickCallJs("uploadFileResult",
												msg.obj instanceof String ? (String) msg.obj : null);
							}
							return true;
						}
					}
			);


		}
	}

}
