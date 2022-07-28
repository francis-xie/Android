package com.basic.code.core.web;

import android.view.KeyEvent;

import com.basic.web.core.Web;
import com.basic.code.core.BaseFragment;

/**
 * 基础web
 */
public abstract class BaseWebViewFragment extends BaseFragment {

    protected Web mWeb;

    //===================生命周期管理===========================//
    @Override
    public void onResume() {
        if (mWeb != null) {
            //恢复
            mWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mWeb != null) {
            //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mWeb.getWebLifeCycle().onResume(); 恢复。
            mWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mWeb != null && mWeb.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        if (mWeb != null) {
            mWeb.destroy();
        }
        super.onDestroyView();
    }
}
