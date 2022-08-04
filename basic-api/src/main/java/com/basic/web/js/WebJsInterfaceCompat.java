
package com.basic.web.js;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;

import com.basic.web.core.Web;
import com.basic.web.utils.WebUtils;
import com.basic.web.utils.LogUtils;

import java.lang.ref.WeakReference;

public class WebJsInterfaceCompat {

	private WeakReference<Web> mReference = null;
	private WeakReference<Activity> mActivityWeakReference = null;
	private String TAG = this.getClass().getSimpleName();

	public WebJsInterfaceCompat(Web web, Activity activity) {
		mReference = new WeakReference<Web>(web);
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

			WebUtils.showFileChooserCompat(mActivityWeakReference.get(),
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
