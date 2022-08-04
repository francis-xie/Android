
package com.basic.code.base.web;

import android.view.KeyEvent;

import com.basic.web.core.Web;
import com.basic.code.base.BaseFragment;

/**
 * 基础web
 */
public abstract class BaseWebViewFragment extends BaseFragment {

    protected Web mWeb;

    @Override
    protected void initViews() {
        mWeb = createWeb();
    }

    /**
     * 创建Web
     *
     * @return Web
     */
    protected abstract Web createWeb();

    //===================生命周期管理===========================//
    @Override
    public void onResume() {
        if (mWeb != null) {
            mWeb.getWebLifeCycle().onResume();//恢复
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mWeb != null) {
            mWeb.getWebLifeCycle().onPause(); //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mWeb.getWebLifeCycle().onResume(); 恢复。
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
