package com.basic.code.core.web;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import com.basic.web.core.web.WebUIControllerImplBase;

import java.lang.ref.WeakReference;

/**
 * 如果你需要修改某一个Web 内部的某一个弹窗 ，请看下面的例子
 * 注意写法一定要参照 DefaultUIController 的写法 ，因为UI自由定制，但是回调的方式是固定的，并且一定要回调。
 */
public class UIController extends WebUIControllerImplBase {

    private WeakReference<Activity> mActivity;

    public UIController(Activity activity) {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public void onShowMessage(String message, String from) {
        super.onShowMessage(message, from);
        Log.i(TAG, "message:" + message);
    }

    @Override
    public void onSelectItemsPrompt(WebView view, String url, String[] items, Handler.Callback callback) {
        // 使用默认的UI
        super.onSelectItemsPrompt(view, url, items, callback);
    }

}
